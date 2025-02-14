package net.satisfy.camping.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.world.item.WalkingStickItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Redirect(method = "knockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getAttributeValue(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D"))
    private double camping$onGetKnockBackResistance(LivingEntity instance, Attribute attribute) {
        if (instance instanceof Player player && player.getMainHandItem().getItem() instanceof WalkingStickItem) {
            if (!player.getCooldowns().isOnCooldown(CampingItems.WALKING_STICK)) {
                return 10.0D;
            }
        }
        return instance.getAttributeValue(attribute);
    }

    @Inject(method = "knockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDeltaMovement()Lnet/minecraft/world/phys/Vec3;"))
    private void camping$onSetKnockBackMovement(double x, double y, double z, CallbackInfo ci) {
        LivingEntity instance = (LivingEntity) (Object) this;
        if (instance instanceof Player player && player.getMainHandItem().getItem() instanceof WalkingStickItem) {
            if (!player.getCooldowns().isOnCooldown(CampingItems.WALKING_STICK)) {
                player.setDeltaMovement(0, 0, 0);
                // player.getCooldowns().addCooldown(CampingItems.WALKING_STICK, 100); // 5 second cooldown
            }
        }
    }
}
