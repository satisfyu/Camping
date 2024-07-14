package net.camping.forge;

import dev.architectury.platform.forge.EventBuses;
import net.camping.Camping;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Camping.MOD_ID)
public class CampingForge {
    public CampingForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(Camping.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Camping.init();
    }
}
