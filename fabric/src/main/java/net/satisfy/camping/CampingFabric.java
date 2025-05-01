package net.satisfy.camping;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.core.callback.LivingEvents;
import net.satisfy.camping.core.config.FabricCampingConfig;
import net.satisfy.camping.core.network.FabricCampingNetwork;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.registry.RegistryFabric;
import net.fabricmc.api.ModInitializer;
import net.satisfy.camping.core.world.block.SleepingBagBlock;
import net.satisfy.camping.optional.trinkets.TrinketsHelper;
import net.satisfy.camping.platform.Services;

public class CampingFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        AutoConfig.register(FabricCampingConfig.class, GsonConfigSerializer::new);
        Camping.init();
        RegistryFabric.register();

        FabricCampingNetwork.registerServerPacketReceivers();

        if (Services.PLATFORM.isModLoaded("trinkets")) TrinketsHelper.registerItemsAsTrinkets();

        LivingEvents.LIVING_HURT.register(CampingFabric::onLivingHurt);
        EntitySleepEvents.ALLOW_SETTING_SPAWN.register(CampingFabric::onPlayerSetSpawn);
    }

    public static boolean onPlayerSetSpawn(Player player, BlockPos blockPos) {
        return !(player.level().getBlockState(blockPos).getBlock() instanceof SleepingBagBlock);
    }

    public static void onLivingHurt(DamageSource source, float amount, LivingEntity attackedEntity) {
        if (!(source.getEntity() instanceof LivingEntity livingAttacker)) return;
        ItemStack stack = livingAttacker.getMainHandItem();
        if (stack.is(CampingItems.MARSHMALLOW_ON_A_STICK) || stack.is(CampingItems.ROASTED_MARSHMALLOW_ON_A_STICK)) {
            if (!attackedEntity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) attackedEntity.addEffect(new MobEffectInstance(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 3, 2, false, true, false)));
        }
    }
}
