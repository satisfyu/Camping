package net.satisfy.camping.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.satisfy.camping.Util.CampingUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;


@SuppressWarnings("deprecation")
public class TentRightBlock extends TentBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF;
    private static final Supplier<VoxelShape> bottomVoxelShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 0.1875, 1, 0.1875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.1875, 0.1875, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.9375, 0.125, 1, 1), BooleanOp.OR);
        return shape;
    };
    public static final Map<Direction, VoxelShape> BOTTOM_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, CampingUtil.rotateShape(Direction.NORTH, direction, bottomVoxelShapeSupplier.get()));
        }
    });
    private static final Supplier<VoxelShape> topVoxelShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 0.1875, 0.8125, 0.1875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.1875, 0.1875, 0.8125, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.8125, 0, 1, 1, 0.1875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.8125, 0.1875, 0.1875, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.8125, 0.1875, 0.875, 0.875, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0.8125, 0.1875, 1, 0.9375, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.9375, 0.125, 0.8125, 1), BooleanOp.OR);
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

    public TentRightBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER));
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
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        Direction facing = blockState.getValue(FACING);
        BlockPos backPos = blockPos.relative(facing.getOpposite());
        BlockPos sidePos = blockPos.relative(facing.getClockWise());
        BlockPos diagonalPos = sidePos.relative(facing.getOpposite());

        level.removeBlock(backPos, false);
        level.removeBlock(sidePos, false);
        level.removeBlock(diagonalPos, false);
        level.removeBlock(blockPos, false);


        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
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