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
        Direction facing = state.hasProperty(BlockStateProperties.FACING) ? state.getValue(BlockStateProperties.FACING) : state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        Direction newDirection = shiftKeyDown ? facing.getCounterClockWise() : facing.getClockWise();

        BlockState newState = state.setValue(BlockStateProperties.HORIZONTAL_FACING, newDirection);
        level.setBlock(pos, newState, Block.UPDATE_ALL);
        spawnBlockParticles(level, pos, newState);
    }

    private static void spawnBlockParticles(Level level, BlockPos pos, BlockState state) {
        for (Direction direction : Direction.values()) {
            double x = pos.getX() + 0.5 + 0.5 * direction.getStepX();
            double y = pos.getY() + 0.5 + 0.5 * direction.getStepY();
            double z = pos.getZ() + 0.5 + 0.5 * direction.getStepZ();

            level.addParticle(
                    new BlockParticleOption(ParticleTypes.BLOCK, state),
                    x, y, z,
                    direction.getStepX() * 0.1,
                    direction.getStepY() * 0.1,
                    direction.getStepZ() * 0.1
            );
        }
    }
}
