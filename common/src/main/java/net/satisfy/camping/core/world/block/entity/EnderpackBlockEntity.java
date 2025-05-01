package net.satisfy.camping.core.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.camping.core.registry.CampingBlockEntities;

public class EnderpackBlockEntity extends BlockEntity implements BlockEntityTicker<EnderpackBlockEntity> {

    public EnderpackBlockEntity(BlockPos pos, BlockState state) {
        super(CampingBlockEntities.ENDERPACK, pos, state);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, EnderpackBlockEntity enderpack) {
        if (level.random.nextFloat() <= 0.00001f) {
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.ENDER_PEARL)));
        }
    }
}
