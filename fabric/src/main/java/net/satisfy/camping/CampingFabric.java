package net.satisfy.camping;

import net.fabricmc.api.ModInitializer;
import net.satisfy.camping.core.network.CampingMessagesFabric;
import net.satisfy.camping.core.registry.RegistryFabric;

public class CampingFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        Camping.init();
        RegistryFabric.register();
        CampingMessagesFabric.registerC2SPackets();
    }
}
