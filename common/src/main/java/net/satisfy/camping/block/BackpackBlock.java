package net.satisfy.camping.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.camping.Camping;
import net.satisfy.camping.registry.ObjectRegistry;
import net.satisfy.camping.util.CampingUtil;
import net.satisfy.camping.block.entity.BackpackBlockEntity;
import net.satisfy.camping.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static net.minecraft.world.level.Level.OVERWORLD;

@SuppressWarnings("deprecation")
public class BackpackBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private final BackpackType backpackType;

    // shulker-related
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
                    return InteractionResult.CONSUME;
                }

                player.openMenu(backpackBlockEntity);
                player.awardStat(Stats.OPEN_SHULKER_BOX);
                PiglinAi.angerNearbyPiglins(player, true);
                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    /* BlockEntity */

    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {

        System.out.println("called playerWillDestroy");

        System.out.println(this.backpackType);

        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof BackpackBlockEntity backpackBlockEntity) {
            if (!level.isClientSide && !player.isCreative()) {

                ItemStack itemStack;

                switch (this.getBackpackType()) {
                    case WANDERER_BACKPACK -> itemStack = new ItemStack(ObjectRegistry.WANDERER_BACKPACK_ITEM.get());
                    case LARGE_BACKPACK -> itemStack = new ItemStack(ObjectRegistry.LARGE_BACKPACK_ITEM.get());
                    case SMALL_BACKPACK -> itemStack = new ItemStack(ObjectRegistry.SMALL_BACKPACK_ITEM.get());
                    case WANDERER_BAG -> itemStack = new ItemStack(ObjectRegistry.WANDERER_BAG_ITEM.get());
                    case SHEEPBAG -> itemStack = new ItemStack(ObjectRegistry.SHEEPBAG_ITEM.get());
                    case GOODYBAG -> itemStack = new ItemStack(ObjectRegistry.GOODYBAG_ITEM.get());
                    default -> itemStack = new ItemStack(Items.BUNDLE);
                }

                System.out.println(itemStack.getDisplayName());

                blockEntity.saveToItem(itemStack);
                if (backpackBlockEntity.hasCustomName()) {
                    itemStack.setHoverName(backpackBlockEntity.getCustomName());
                }

                ItemEntity itemEntity = new ItemEntity(level, (double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5, itemStack);
                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);
            }
        }

        super.playerWillDestroy(level, blockPos, blockState, player);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack pItemStack) {

        System.out.println("called playerDestroy");

        if (blockEntity instanceof BackpackBlockEntity backpackBlockEntity) {
            if (!level.isClientSide && player.isCreative() && !backpackBlockEntity.isEmpty()) {

                ItemStack itemStack = ItemStack.EMPTY;

                switch (this.getBackpackType()) {
                    case WANDERER_BACKPACK -> itemStack = new ItemStack(ObjectRegistry.WANDERER_BACKPACK_ITEM.get());
                    case LARGE_BACKPACK -> itemStack = new ItemStack(ObjectRegistry.LARGE_BACKPACK_ITEM.get());
                    case SMALL_BACKPACK -> itemStack = new ItemStack(ObjectRegistry.SMALL_BACKPACK_ITEM.get());
                    case WANDERER_BAG -> itemStack = new ItemStack(ObjectRegistry.WANDERER_BAG_ITEM.get());
                    case SHEEPBAG -> itemStack = new ItemStack(ObjectRegistry.SHEEPBAG_ITEM.get());
                    case GOODYBAG -> itemStack = new ItemStack(ObjectRegistry.GOODYBAG_ITEM.get());
                }

                System.out.println(itemStack.getDisplayName());

                blockEntity.saveToItem(itemStack);
                if (backpackBlockEntity.hasCustomName()) {
                    itemStack.setHoverName(backpackBlockEntity.getCustomName());
                }

                ItemEntity itemEntity = new ItemEntity(level, (double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5, itemStack);
                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);
            }
        }

        super.playerDestroy(level, player, blockPos, blockState, blockEntity, pItemStack);
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        ItemStack itemStack = super.getCloneItemStack(blockGetter, blockPos, blockState);
        blockGetter.getBlockEntity(blockPos, EntityTypeRegistry.BACKPACK_BLOCK_ENTITY.get()).ifPresent((backpack) -> {
            backpack.saveToItem(itemStack);
        });
        return itemStack;
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!blockState.is(blockState2.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof BackpackBlockEntity) {
                level.updateNeighbourForOutputSignal(blockPos, blockState.getBlock());
            }

            super.onRemove(blockState, level, blockPos, blockState2, bl);
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        if (itemStack.hasCustomHoverName()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof BackpackBlockEntity) {
                ((BackpackBlockEntity)blockEntity).setCustomName(itemStack.getHoverName());
            }
        }
    }

    @Override
    public @NotNull List<ItemStack> getDrops(BlockState blockState, LootParams.Builder builder) {

        BlockEntity blockEntity = (BlockEntity)builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);

        if (blockEntity instanceof BackpackBlockEntity backpackBlockEntity) {
            builder = builder.withDynamicDrop(CONTENTS, (consumer) -> {
                for(int i = 0; i < backpackBlockEntity.getContainerSize(); ++i) {
                    consumer.accept(backpackBlockEntity.getItem(i));
                }

            });
        }

        return super.getDrops(blockState, builder);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BackpackBlockEntity(blockPos, blockState);
    }

    // Unused as we are not animating
//    @Override
//    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
//        return createTickerHelper(blockEntityType, BlockEntityType.SHULKER_BOX, ShulkerBoxBlockEntity::tick);
//    }

    /* BlockState */

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
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }

    /* VoxelShape */

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
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES.get(backpackType).get(state.getValue(FACING));
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
}
