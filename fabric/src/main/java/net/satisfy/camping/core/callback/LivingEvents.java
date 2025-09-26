package net.satisfy.camping.core.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class LivingEvents {
    public static final Event<LivingHurt> LIVING_HURT = EventFactory.createArrayBacked(LivingHurt.class, callbacks -> (source, amount, entity) -> {
        for (LivingHurt callback : callbacks) {
            if (!entity.isInvulnerableTo(source)) callback.onUpdate(source, amount, entity);
        }
    });

    @FunctionalInterface
    public interface LivingHurt {
        void onUpdate(DamageSource damageSource, float damageAmount, LivingEntity entity);
    }
}
