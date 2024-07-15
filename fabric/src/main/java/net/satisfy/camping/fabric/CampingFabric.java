package net.satisfy.camping.fabric;

import net.satisfy.camping.Camping;
import net.fabricmc.api.ModInitializer;

public class CampingFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Camping.init();
    }
}
