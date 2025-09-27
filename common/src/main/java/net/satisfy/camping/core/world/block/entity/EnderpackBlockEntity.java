package net.satisfy.camping.core.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.camping.core.registry.CampingBlockEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnderpackBlockEntity extends BlockEntity implements BlockEntityTicker<EnderpackBlockEntity> {

    private Component customName;

    public EnderpackBlockEntity(BlockPos pos, BlockState state) {
        super(CampingBlockEntities.ENDERPACK, pos, state);
    }

    @Override
    public void tick(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull EnderpackBlockEntity enderpack) {
        if (level.random.nextFloat() <= 0.00001f) {
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.ENDER_PEARL)));
        }
    }

    public boolean hasCustomName() {
        return this.customName != null;
    }

    public void setCustomName(@Nullable Component name) {
        this.customName = name;
    }

    public @Nullable Component getCustomName() {
        return this.customName;
    }
}
