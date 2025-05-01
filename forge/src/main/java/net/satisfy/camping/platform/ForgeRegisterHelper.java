package net.satisfy.camping.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.camping.platform.services.IRegisterHelper;

import java.util.function.BiFunction;

public class ForgeRegisterHelper implements IRegisterHelper {

    @Override
    public <T extends BlockEntity> BlockEntityType<T> blockEntity(BiFunction<BlockPos, BlockState, T> func, Block... blocks) {
        return BlockEntityType.Builder.of(func::apply, blocks).build(null);
    }
}
