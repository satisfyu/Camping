package net.satisfy.camping.core.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.world.block.entity.BackpackBlockEntity;
import net.satisfy.camping.core.util.CampingUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class BackpackBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private final BackpackType backpackType;

    public static final ResourceLocation CONTENTS = new ResourceLocation("contents");

    public BackpackBlock(Properties properties, BackpackType backpackType) {
        super(properties);
        this.backpackType = backpackType;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else if (player.isSpectator()) {
            return InteractionResult.CONSUME;
        } else {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof BackpackBlockEntity backpackBlockEntity) {
                if (player.isShiftKeyDown()) {
                    level.destroyBlock(blockPos, true);
                    dropBlockWithContents(level, blockPos, backpackBlockEntity);
                    return InteractionResult.CONSUME;
                }

                player.openMenu(backpackBlockEntity);
                player.awardStat(Stats.OPEN_SHULKER_BOX);
                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    private void dropBlockWithContents(Level level, BlockPos blockPos, BackpackBlockEntity backpackBlockEntity) {
        ItemStack itemStack = new ItemStack(getBackpackItem());
        backpackBlockEntity.saveToItem(itemStack);
        if (backpackBlockEntity.hasCustomName()) {
            itemStack.setHoverName(backpackBlockEntity.getCustomName());
        }
        ItemEntity itemEntity = new ItemEntity(level, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, itemStack);
        itemEntity.setDefaultPickUpDelay();
        level.addFreshEntity(itemEntity);
    }

    private Item getBackpackItem() {
        return switch (this.getBackpackType()) {
            case WANDERER_BACKPACK -> CampingItems.WANDERER_BACKPACK;
            case LARGE_BACKPACK -> CampingItems.LARGE_BACKPACK;
            case SMALL_BACKPACK -> CampingItems.SMALL_BACKPACK;
            case WANDERER_BAG -> CampingItems.WANDERER_BAG;
            case SHEEPBAG -> CampingItems.SHEEPBAG;
            case GOODYBAG -> CampingItems.GOODYBAG;
        };
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof BackpackBlockEntity backpackBlockEntity) {
            dropBlockWithContents(level, blockPos, backpackBlockEntity);
        }
        super.playerWillDestroy(level, blockPos, blockState, player);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity, ItemStack pItemStack) {
        super.playerDestroy(level, player, blockPos, blockState, blockEntity, pItemStack);
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootParams.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof BackpackBlockEntity backpackBlockEntity) {
            builder = builder.withDynamicDrop(CONTENTS, consumer -> {
                for (int i = 0; i < backpackBlockEntity.getContainerSize(); ++i) {
                    consumer.accept(backpackBlockEntity.getItem(i));
                }
            });
        }
        return super.getDrops(blockState, builder);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BackpackBlockEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    public BackpackType getBackpackType() {
        return this.backpackType;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(blockPos));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }

    public static final Map<BackpackType, Map<Direction, VoxelShape>> SHAPES = net.minecraft.Util.make(new HashMap<>(), map -> {
        map.put(BackpackType.SMALL_BACKPACK, generateShapes(BackpackBlockShapes.SMALL_BACKPACK));
        map.put(BackpackType.LARGE_BACKPACK, generateShapes(BackpackBlockShapes.LARGE_BACKPACK));
        map.put(BackpackType.WANDERER_BACKPACK, generateShapes(BackpackBlockShapes.WANDERER_BACKPACK));
        map.put(BackpackType.WANDERER_BAG, generateShapes(BackpackBlockShapes.WANDERER_BAG));
        map.put(BackpackType.GOODYBAG, generateShapes(BackpackBlockShapes.GOODYBAG));
        map.put(BackpackType.SHEEPBAG, generateShapes(BackpackBlockShapes.SHEEPBAG));
    });

    private static Map<Direction, VoxelShape> generateShapes(Supplier<VoxelShape> shapeSupplier) {
        Map<Direction, VoxelShape> shapeMap = new HashMap<>();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            shapeMap.put(direction, CampingUtil.rotateShape(Direction.NORTH, direction, shapeSupplier.get()));
        }
        return shapeMap;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES.get(backpackType).get(state.getValue(FACING));
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
}
