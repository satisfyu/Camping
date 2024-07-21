package net.satisfy.camping.forge.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegisterEvent;
import net.satisfy.camping.Camping;
import net.satisfy.camping.client.CampingClient;

@Mod.EventBusSubscriber(modid = Camping.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HerbalbrewsClientForge {
    @SubscribeEvent
    public static void beforeClientSetup(RegisterEvent event) {
        CampingClient.preInitClient();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        CampingClient.onInitializeClient();
    }
}
