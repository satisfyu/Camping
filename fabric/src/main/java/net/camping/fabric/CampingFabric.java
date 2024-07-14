package net.camping.fabric;

import net.camping.Camping;
import net.fabricmc.api.ModInitializer;

public class CampingFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Camping.init();
    }
}
