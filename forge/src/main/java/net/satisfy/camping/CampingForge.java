package net.satisfy.camping;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.satisfy.camping.core.registry.RegistryForge;

@Mod(Constants.MOD_ID)
public class CampingForge {

    public static IEventBus EVENT_BUS = null;

    public CampingForge() {
        Camping.init();
        CampingForge.EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        RegistryForge.register(CampingForge.EVENT_BUS);
    }
}