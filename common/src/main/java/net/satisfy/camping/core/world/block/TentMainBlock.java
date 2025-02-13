package net.satisfy.camping.core.world.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.camping.core.registry.CampingBlocks;
import net.satisfy.camping.core.util.CampingUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class TentMainBlock extends TentBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF;
    private final DyeColor color;
    private static final Supplier<VoxelShape> bottomVoxelShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.8125, 0, 0, 1, 1, 0.1875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.8125, 0, 0.1875, 0.875, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0.9375, 0.9375, 1, 1), BooleanOp.OR);
        return shape;
    };
    public static final Map<Direction, VoxelShape> BOTTOM_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, CampingUtil.rotateShape(Direction.NORTH, direction, bottomVoxelShapeSupplier.get()));
        }
    });
    private static final Supplier<VoxelShape> topVoxelShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.8125, 0, 0, 1, 0.8125, 0.1875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.8125, 0, 0.1875, 0.875, 0.8125, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.8125, 0, 1, 1, 0.1875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.8125, 0.8125, 0.1875, 1, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.8125, 0.1875, 0.8125, 0.875, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.8125, 0.1875, 0.125, 0.9375, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0.9375, 0.9375, 0.8125, 1), BooleanOp.OR);
        return shape;
    };
    public static final Map<Direction, VoxelShape> TOP_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, CampingUtil.rotateShape(Direction.NORTH, direction, topVoxelShapeSupplier.get()));
        }
    });

    static {
        HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    }

    public TentMainBlock(Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
        this.registerDefaultState(this.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER));
    }

    public DyeColor getColor() {
        return this.color;
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (blockState.getValue(HALF) == DoubleBlockHalf.LOWER) {
            level.setBlockAndUpdate(blockPos.above(), blockState.setValue(HALF, DoubleBlockHalf.UPPER));
        }
    }

    public @NotNull BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        DoubleBlockHalf doubleBlockHalf = blockState.getValue(HALF);
        if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP)) {
            return blockState2.is(this) && blockState2.getValue(HALF) != doubleBlockHalf ? blockState.setValue(FACING, blockState2.getValue(FACING)) : Blocks.AIR.defaultBlockState();
        } else {
            return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !blockState.canSurvive(levelAccessor, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        Direction facing = blockState.getValue(FACING);
        BlockPos backPos = blockPos.relative(facing.getOpposite());
        BlockPos sidePos = blockPos.relative(facing.getCounterClockWise());
        BlockPos diagonalPos = sidePos.relative(facing.getOpposite());

        level.removeBlock(backPos, false);
        level.removeBlock(sidePos, false);
        level.removeBlock(diagonalPos, false);
        level.removeBlock(blockPos, false);

        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Level level = blockPlaceContext.getLevel();
        BlockPos mainPos = blockPlaceContext.getClickedPos();
        BlockState blockState = super.getStateForPlacement(blockPlaceContext);
        if (blockState == null) return null;
        Direction facing = blockState.getValue(FACING);
        BlockPos backPos = mainPos.relative(facing.getOpposite());
        BlockPos sidePos = mainPos.relative(facing.getCounterClockWise());
        BlockPos diagonalPos = sidePos.relative(facing.getOpposite());
        BlockPos topPos = diagonalPos.above();
        boolean placeable = canPlace(level, backPos, sidePos, diagonalPos, topPos);
        return placeable ? blockState : null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
        if (level.isClientSide) return;
        Direction facing = blockState.getValue(FACING);
        BlockPos backPos = blockPos.relative(facing.getOpposite());
        BlockPos sidePos = blockPos.relative(facing.getCounterClockWise());
        BlockPos diagonalPos = sidePos.relative(facing.getOpposite());
        BlockPos topPos = diagonalPos.above();
        if (!canPlace(level, backPos, sidePos, diagonalPos, topPos)) return;
        level.setBlock(backPos, CampingBlocks.TENT_MAIN_HEAD.get(this.color.getName()).defaultBlockState().setValue(FACING, facing), 3);
        level.setBlock(sidePos, CampingBlocks.TENT_RIGHT.get(this.color.getName()).defaultBlockState().setValue(FACING, facing), 3);
        level.setBlock(diagonalPos, CampingBlocks.TENT_HEAD_RIGHT.get(this.color.getName()).defaultBlockState().setValue(FACING, facing), 3);
    }

    private boolean canPlace(Level level, BlockPos... blockPoses) {
        for (BlockPos blockPos : blockPoses) {
            if (!level.getBlockState(blockPos).isAir()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        DoubleBlockHalf half = state.getValue(HALF);
        Direction facing = state.getValue(FACING);

        if (half == DoubleBlockHalf.LOWER) {
            return BOTTOM_SHAPE.get(facing);
        } else {
            return TOP_SHAPE.get(facing);
        }
    }
}
