package net.satisfy.camping.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.satisfy.camping.Camping;
import net.satisfy.camping.fabric.config.ConfigFabric;

public class CampingFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        AutoConfig.register(ConfigFabric.class, GsonConfigSerializer::new);
        Camping.init();
    }
}
