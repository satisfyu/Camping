package net.satisfy.camping.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.camping.Util.CampingUtil;
import net.satisfy.camping.block.entity.BackpackBlockEntity;
import net.satisfy.camping.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class BackpackBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING;
    public static final ResourceLocation CONTENTS;
    public static final BooleanProperty WATERLOGGED;
    private final BackpackType backpackType;

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        CONTENTS = new ResourceLocation("contents");
    }

    public BackpackBlock(Properties properties, BackpackType backpackType) {
        super(properties);
        this.backpackType = backpackType;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    private static final Supplier<VoxelShape> SMALL_BACKPACK = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.4375, 0.375, 0.375, 0.5625, 0.5, 0.4375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0, 0.4375, 0.75, 0.625, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.3125, 0.4375), BooleanOp.OR);
        return shape;
    };

    private static final Supplier<VoxelShape> LARGE_BACKPACK = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.4375, 0.8125, 0.75, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0, 0.3125, 0.75, 0.375, 0.4375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.4375, 0.5, 0.375, 0.5625, 0.625, 0.4375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.75, 0.375, 0.875, 1.0625, 0.6875), BooleanOp.OR);
        return shape;
    };

    private static final Supplier<VoxelShape> WANDERER_BACKPACK = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.125, 0.5, 0.4375, 0.875, 0.6875, 0.625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.25, 0.3125, 0.8125, 0.5, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.3125, 0.8125, 0.5, 0.6875), BooleanOp.OR);
        return shape;
    };

    private static final Supplier<VoxelShape> WANDERER_BAG = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.25, 0, 0.3125, 0.75, 0.625, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.375, 0.25, 0.25, 0.625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.75, 0, 0.375, 0.875, 0.25, 0.625), BooleanOp.OR);
        return shape;
    };

    private static final Supplier<VoxelShape> SHEEPBAG = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.34375, 0.4375, 0.40625, 0.65625, 0.75, 0.71875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.28125, 0, 0.46875, 0.71875, 0.4375, 0.71875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.28125, 0, 0.34375, 0.40625, 0.125, 0.46875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.59375, 0, 0.34375, 0.71875, 0.125, 0.46875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.28125, 0.3125, 0.34375, 0.40625, 0.4375, 0.46875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.59375, 0.3125, 0.34375, 0.71875, 0.4375, 0.46875), BooleanOp.OR);
        return shape;
    };

    private static final Supplier<VoxelShape> GOODYBAG = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.25, 0.75, 0.625, 0.625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.75, 0, 0.25, 0.9375, 0.3125, 0.625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.25, 0.125, 0.3125, 0.625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.125, 0.6875, 0.3125, 0.25), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.625, 0.5, 0.75, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.4375, 0.25, 0.75, 0.625, 0.625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.375, 0.375, 0.1875, 0.5, 0.5, 0.25), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.003125, 0.125, 0.3125, 0.128125, 0.5, 0.4375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.003125, 0.125, 0.4375, 0.128125, 0.75, 0.5625), BooleanOp.OR);
        return shape;
    };

    public static final Map<BackpackType, Map<Direction, VoxelShape>> SHAPES = net.minecraft.Util.make(new HashMap<>(), map -> {
        map.put(BackpackType.SMALL_BACKPACK, generateShapes(SMALL_BACKPACK));
        map.put(BackpackType.LARGE_BACKPACK, generateShapes(LARGE_BACKPACK));
        map.put(BackpackType.WANDERER_BACKPACK, generateShapes(WANDERER_BACKPACK));
        map.put(BackpackType.WANDERER_BAG, generateShapes(WANDERER_BAG));
        map.put(BackpackType.GOODYBAG, generateShapes(GOODYBAG));
        map.put(BackpackType.SHEEPBAG, generateShapes(SHEEPBAG));
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

    public BackpackType getBackpackType() {
        return this.backpackType;
    }

    public enum BackpackType {
        WANDERER_BACKPACK, LARGE_BACKPACK, SMALL_BACKPACK, WANDERER_BAG, SHEEPBAG, GOODYBAG
    }
/*
    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        else {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof BackpackBlockEntity) {

                if (this.backpackType != BackpackType.ENDERPACK) {
                    player.openMenu((BackpackBlockEntity) blockEntity);
                } else {
                    player.openMenu(new SimpleMenuProvider((containerID, inventory, contextualPlayer) -> {
                        return ChestMenu.threeRows(containerID, inventory, contextualPlayer.getEnderChestInventory());
                    }, Component.translatable("container.camping.ender_backpack")));
                }

            }
            return InteractionResult.CONSUME;
        }
    }
*/
    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof BackpackBlockEntity) {
                ItemStack itemStack = new ItemStack(blockState.getBlock());
                CompoundTag tag = new CompoundTag();
                ((BackpackBlockEntity) blockEntity).saveAdditional(tag);
                itemStack.addTagElement("BlockEntityTag", tag);
                double x = blockPos.getX() + 0.5;
                double y = blockPos.getY() + 0.5;
                double z = blockPos.getZ() + 0.5;
                ItemEntity itemEntity = new ItemEntity(level, x, y, z, itemStack);
                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);
            }
        }
        super.playerWillDestroy(level, blockPos, blockState, player);
    }

    @Override
    public @NotNull List<ItemStack> getDrops(BlockState blockState, LootParams.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof BackpackBlockEntity) {
            builder = builder.withDynamicDrop(CONTENTS, (consumer) -> {
                for (int i = 0; i < ((BackpackBlockEntity) blockEntity).getContainerSize(); ++i) {
                    consumer.accept(((BackpackBlockEntity) blockEntity).getItems().get(i));
                }
            });
        }
        return super.getDrops(blockState, builder);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        if (itemStack.hasCustomHoverName()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof BackpackBlockEntity) {
                ((BackpackBlockEntity) blockEntity).setCustomName(itemStack.getHoverName());
            }
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if (!blockState.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof BackpackBlockEntity) {
                level.updateNeighbourForOutputSignal(blockPos, blockState.getBlock());
            }
            super.onRemove(blockState, level, blockPos, newState, isMoving);
        }
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        ItemStack itemStack = super.getCloneItemStack(blockGetter, blockPos, blockState);
        blockGetter.getBlockEntity(blockPos, EntityTypeRegistry.BACKPACK_BLOCK_ENTITY.get()).ifPresent(blockEntity -> {
            CompoundTag tag = new CompoundTag();
            blockEntity.saveAdditional(tag);
            itemStack.addTagElement("BlockEntityTag", tag);
        });
        return itemStack;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
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

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BackpackBlockEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }
}
