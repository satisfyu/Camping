package net.satisfy.camping.core.world.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WalkingStickItem extends Item {

    public WalkingStickItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotIndex, boolean isSelected) {

        if (!isSelected || !(entity instanceof Player player)) return;

        if (player.hasEffect(MobEffects.MOVEMENT_SPEED) && player.getEffect(MobEffects.MOVEMENT_SPEED).getAmplifier() >= 0) return;

        // duration in ticks (0.5 seconds), amplifier (0, base level)
        // tickDuration, amplifier, ambient, showParticles, showIcon
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 0, false, false, false));
    }
}
