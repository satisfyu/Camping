package net.satisfy.camping.core.world.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.satisfy.camping.core.registry.CampingItems;

import java.util.List;
import java.util.Set;

public class MarshmallowItem extends Item {

    private final boolean roasted;
    public MarshmallowItem(boolean roasted, Properties $$0) {
        super($$0);
        this.roasted = roasted;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotIndex, boolean isSelected) {
        if (!(entity instanceof ServerPlayer player)) return;
        if (player.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) return;

        for (ItemStack hand : List.of(player.getMainHandItem(), player.getOffhandItem())) {
            if (!hand.is(CampingItems.MARSHMALLOW) && !hand.is(CampingItems.ROASTED_MARSHMALLOW)) return;
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, this.roasted ? 1 : 2, false, false, false)); // new MobEffectInstance(effect, duration, amplifier, ambient, visible, showIcon)
        }
    }
}
