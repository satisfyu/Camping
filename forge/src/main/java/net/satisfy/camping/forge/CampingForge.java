package net.satisfy.camping.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.satisfy.camping.Camping;
import net.satisfy.camping.forge.integration.CuriosBackpack;
import net.satisfy.camping.registry.ObjectRegistry;
import top.theillusivec4.curios.api.CuriosApi;

@Mod(Camping.MODID)
public class CampingForge {
    public CampingForge() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Camping.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        Camping.init();
        eventBus.addListener(this::setup);

    }

    private void setup(final FMLCommonSetupEvent evt) {
        CuriosApi.registerCurio(ObjectRegistry.SMALL_BACKPACK_ITEM.get(), new CuriosBackpack());
    }
}
