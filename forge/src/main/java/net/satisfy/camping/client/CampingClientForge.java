package net.satisfy.camping.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.satisfy.camping.Constants;
import net.satisfy.camping.client.model.*;
import net.satisfy.camping.client.renderer.GrillRenderer;
import net.satisfy.camping.core.registry.CampingBlockEntities;

public class CampingClientForge {

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModBusEvents {

        @SubscribeEvent
        public static void onClientSetup(final FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                CampingClient.init();
            });
        }

        @SubscribeEvent
        public static void onRegisterEntityModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(SmallBackpackModel.LAYER_LOCATION, SmallBackpackModel::createBodyLayer);
            event.registerLayerDefinition(LargeBackpackModel.LAYER_LOCATION, LargeBackpackModel::createBodyLayer);
            event.registerLayerDefinition(WandererBackpackModel.LAYER_LOCATION, WandererBackpackModel::createBodyLayer);
            event.registerLayerDefinition(WandererBagModel.LAYER_LOCATION, WandererBagModel::createBodyLayer);
            event.registerLayerDefinition(GoodybagModel.LAYER_LOCATION, GoodybagModel::createBodyLayer);
            event.registerLayerDefinition(SheepbagModel.LAYER_LOCATION, SheepbagModel::createBodyLayer);
            event.registerLayerDefinition(EnderpackModel.LAYER_LOCATION, EnderpackModel::createBodyLayer);
            event.registerLayerDefinition(EnderbagModel.LAYER_LOCATION, EnderbagModel::createBodyLayer);
        }
    }

    @SubscribeEvent
    public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CampingBlockEntities.GRILL, GrillRenderer::new);
    }
}
