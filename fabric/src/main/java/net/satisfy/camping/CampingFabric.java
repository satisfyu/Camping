package net.satisfy.camping;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.satisfy.camping.core.config.FabricCampingConfig;
import net.satisfy.camping.core.network.FabricCampingNetwork;
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

        EntitySleepEvents.ALLOW_SETTING_SPAWN.register(CampingFabric::onPlayerSetSpawn);
    }

    public static boolean onPlayerSetSpawn(Player player, BlockPos blockPos) {
        return !(player.level().getBlockState(blockPos).getBlock() instanceof SleepingBagBlock);
    }
}
