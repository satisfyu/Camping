package net.satisfy.camping.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.satisfy.camping.core.callback.LivingEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class FabricLivingEntityMixin {

    @Inject(method = "actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At(value = "HEAD"))
    private void injected(DamageSource damageSource, float damageAmount, CallbackInfo ci) {
        LivingEntity instance = (LivingEntity) (Object) this;
        if (instance.isInvulnerableTo(damageSource)) return;
        LivingEvents.LIVING_HURT.invoker().onUpdate(damageSource, damageAmount, instance);
    }
}
