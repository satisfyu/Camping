package net.satisfy.camping;

import dev.emi.trinkets.api.TrinketsApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.camping.compat.trinkets.TrinketsCompatibility;
import net.satisfy.camping.core.config.ConfigFabric;
import net.satisfy.camping.core.network.CampingMessagesFabric;
import net.satisfy.camping.core.registry.RegistryFabric;
import net.satisfy.camping.core.world.block.SleepingBagBlock;
import net.satisfy.camping.platform.Services;

public class CampingFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        Camping.init();
        AutoConfig.register(ConfigFabric.class, GsonConfigSerializer::new);
        RegistryFabric.register();
        CampingMessagesFabric.registerC2SPackets();

        if (Services.PLATFORM.isModLoaded("trinkets")) TrinketsCompatibility.load();

        EntitySleepEvents.ALLOW_SETTING_SPAWN.register((player, sleepingPos) -> {
            boolean onClient = player.level().isClientSide;
            BlockState blockState = player.level().getBlockState(sleepingPos);
            return !(!onClient && blockState.getBlock() instanceof SleepingBagBlock);
        });
    }
}
