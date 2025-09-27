package net.satisfy.camping;

import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.satisfy.camping.client.gui.screens.inventory.BackpackScreen;
import net.satisfy.camping.client.model.EnderbagModel;
import net.satisfy.camping.client.model.EnderpackModel;
import net.satisfy.camping.client.model.GoodybagModel;
import net.satisfy.camping.client.model.LargeBackpackModel;
import net.satisfy.camping.client.model.SheepbagModel;
import net.satisfy.camping.client.model.SmallBackpackModel;
import net.satisfy.camping.client.model.WandererBackpackModel;
import net.satisfy.camping.client.model.WandererBagModel;
import net.satisfy.camping.client.renderer.entity.layers.BackpackRenderLayer;
import net.satisfy.camping.client.renderer.entity.layers.EnderpackRenderLayer;
import net.satisfy.camping.core.registry.CampingScreenHandlers;
import net.satisfy.camping.optional.NeoForgeCuriosHelper;
import net.satisfy.camping.platform.Services;

import java.util.function.Consumer;

public class CampingClientNeoForge {

    public CampingClientNeoForge(IEventBus modEventBus) {
        modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> event.enqueueWork(() -> {
            CampingClient.init();
            if (Services.PLATFORM.isModLoaded("curios")) NeoForgeCuriosHelper.registerRenderersForCurios();
        }));
        modEventBus.addListener((Consumer<RegisterMenuScreensEvent>) event -> {
            event.register(CampingScreenHandlers.BACKPACK, BackpackScreen::new);
        });
        modEventBus.addListener((Consumer<EntityRenderersEvent.RegisterLayerDefinitions>) event -> {
            event.registerLayerDefinition(EnderpackModel.LAYER_LOCATION, EnderpackModel::createBodyLayer);
            event.registerLayerDefinition(EnderbagModel.LAYER_LOCATION, EnderbagModel::createBodyLayer);
            event.registerLayerDefinition(GoodybagModel.LAYER_LOCATION, GoodybagModel::createBodyLayer);
            event.registerLayerDefinition(LargeBackpackModel.LAYER_LOCATION, LargeBackpackModel::createBodyLayer);
            event.registerLayerDefinition(SheepbagModel.LAYER_LOCATION, SheepbagModel::createBodyLayer);
            event.registerLayerDefinition(SmallBackpackModel.LAYER_LOCATION, SmallBackpackModel::createBodyLayer);
            event.registerLayerDefinition(WandererBackpackModel.LAYER_LOCATION, WandererBackpackModel::createBodyLayer);
            event.registerLayerDefinition(WandererBagModel.LAYER_LOCATION, WandererBagModel::createBodyLayer);
        });
        modEventBus.addListener((Consumer<EntityRenderersEvent.AddLayers>) event -> {
            PlayerRenderer wide = event.getSkin(PlayerSkin.Model.WIDE);
            if (wide != null) {
                wide.addLayer(new EnderpackRenderLayer(wide));
                wide.addLayer(new BackpackRenderLayer(wide));
            }
            PlayerRenderer slim = event.getSkin(PlayerSkin.Model.SLIM);
            if (slim != null) {
                slim.addLayer(new EnderpackRenderLayer(slim));
                slim.addLayer(new BackpackRenderLayer(slim));
            }
        });
    }
}
