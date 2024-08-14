package net.satisfy.camping.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.camping.Camping;
import net.satisfy.camping.block.SleepingBagBlock;
import net.satisfy.camping.fabric.compat.trinkets.TrinketsCompatibility;
import net.satisfy.camping.fabric.config.ConfigFabric;

public class CampingFabric implements ModInitializer {

    public static boolean trinketsLoaded;

    @Override
    public void onInitialize() {
        AutoConfig.register(ConfigFabric.class, GsonConfigSerializer::new);
        Camping.init();

        CampingFabric.trinketsLoaded = FabricLoader.getInstance().isModLoaded("trinkets");
        

        if (trinketsLoaded) {
           TrinketsCompatibility.load();
        }

        EntitySleepEvents.ALLOW_SETTING_SPAWN.register((player, sleepingPos) -> {
            boolean onClient = player.level().isClientSide;
            BlockState blockState = player.level().getBlockState(sleepingPos);
            return !(!onClient && blockState.getBlock() instanceof SleepingBagBlock);
        });
    }

    public static boolean trinketsLoaded() {
        return CampingFabric.trinketsLoaded;
    }
}
