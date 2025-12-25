package net.satisfy.camping.core.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class MultitoolUtil {

    public static void rotateBlock(Level level, BlockPos pos, BlockState state, boolean shiftKeyDown) {
        Direction newDirection;

        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            newDirection = shiftKeyDown ? facing.getCounterClockWise() : facing.getClockWise();
            state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, newDirection);
        } else if (state.hasProperty(BlockStateProperties.FACING)) {
            Direction facing = state.getValue(BlockStateProperties.FACING);
            if (facing.getAxis().isVertical()) return;
            newDirection = shiftKeyDown ? facing.getCounterClockWise() : facing.getClockWise();
            state = state.setValue(BlockStateProperties.FACING, newDirection);
        } else {
            return;
        }

        level.setBlock(pos, state, Block.UPDATE_ALL);
        spawnBlockParticles(level, pos, state);
    }

    private static void spawnBlockParticles(Level level, BlockPos pos, BlockState state) {
        for (Direction direction : Direction.values()) {
            double x = pos.getX() + 0.5 + 0.5 * direction.getStepX();
            double y = pos.getY() + 0.5 + 0.5 * direction.getStepY();
            double z = pos.getZ() + 0.5 + 0.5 * direction.getStepZ();

            level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), x, y, z, direction.getStepX() * 0.1, direction.getStepY() * 0.1, direction.getStepZ() * 0.1
            );
        }
    }
}