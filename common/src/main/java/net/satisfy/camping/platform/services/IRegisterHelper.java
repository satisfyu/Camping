package net.satisfy.camping.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public interface IRegisterHelper {

    <T extends BlockEntity> BlockEntityType<T> blockEntity(BiFunction<BlockPos, BlockState, T> func, Block... blocks);
}
