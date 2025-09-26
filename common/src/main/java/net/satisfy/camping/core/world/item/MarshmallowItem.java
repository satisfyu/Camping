package net.satisfy.camping.core.world.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MarshmallowItem extends Item {
    private final boolean roasted;

    public MarshmallowItem(boolean roasted, Properties properties) {
        super(properties);
        this.roasted = roasted;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, Level level, LivingEntity entity) {
        ItemStack out = super.finishUsingItem(stack, level, entity);
        if (!level.isClientSide && roasted) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 4 * 20, 1));
        }
        return out;
    }
}
