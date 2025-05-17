package net.satisfy.camping.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.world.entity.Mosquito;
import net.satisfy.camping.core.world.item.WalkingStickItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;


@Mixin(LivingEntity.class)
public class LivingEntityMixin {

//    @Redirect(method = "knockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getAttributeValue(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D"))
//    private double camping$onGetKnockBackResistance(LivingEntity instance, Attribute attribute) {
//        if (instance instanceof Player player && player.getMainHandItem().getItem() instanceof WalkingStickItem) {
//            if (!player.getCooldowns().isOnCooldown(CampingItems.WALKING_STICK)) {
//                return 10.0D;
//            }
//        }
//        return instance.getAttributeValue(attribute);
//    }

    @Unique
    private static final int untag_cycle = 20 * 60; // one minute

    @Inject(method = "tick()V", at = @At("HEAD"))
    private void camping$tick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        Level level = entity.level();
        if (!(entity instanceof Player) || level.isClientSide()) return;

        if (level.getGameTime() % 20 != 0) return; // operate once per second
        if (level.getGameTime() % untag_cycle == 0 && entity.getTags().contains("mosquito.spawned")) entity.removeTag("mosquito.spawned");

        float additionalChance = level.getBiome(entity.blockPosition()).is(Biomes.SWAMP) || level.getBiome(entity.blockPosition()).is(Biomes.MANGROVE_SWAMP) ? 0.20f : 0;

        RandomSource random = entity.getRandom();
        if (!entity.getTags().contains("mosquito.spawned") && random.nextFloat() < (0.005 + additionalChance)) { // 0.5% normally or 20.5% chance if in swamp
            Mosquito mosquito = new Mosquito(level);
            Supplier<Integer> randomInversion = () -> random.nextFloat() >= 0.5 ? 1 : -1;
            mosquito.moveTo(entity.getX() + (random.nextInt(10) * randomInversion.get()), entity.getY() + 1, entity.getZ() + (random.nextInt(10) * randomInversion.get()));
            level.addFreshEntity(mosquito);
            entity.addTag("mosquito.spawned");
        }
    }

    @Inject(method = "getAttributeValue(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D", at = @At("RETURN"), cancellable = true)
    private void injected(Attribute attribute, CallbackInfoReturnable<Double> cir) {
        if (attribute != Attributes.KNOCKBACK_RESISTANCE) return;
        LivingEntity instance = (LivingEntity) (Object) this;
        if (instance instanceof Player player && player.getMainHandItem().getItem() instanceof WalkingStickItem) {
            if (!player.getCooldowns().isOnCooldown(CampingItems.WALKING_STICK)) {
                cir.setReturnValue(10.0D);
            }
        }
    }

    @Inject(method = "knockback(DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDeltaMovement()Lnet/minecraft/world/phys/Vec3;"))
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
