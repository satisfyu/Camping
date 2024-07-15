package net.satisfy.camping.forge;

import dev.architectury.platform.forge.EventBuses;
import net.satisfy.camping.Camping;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Camping.MODID)
public class CampingForge {
    public CampingForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(Camping.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        Camping.init();
    }
}
