package net.satisfy.camping;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
import net.satisfy.camping.optional.ForgeCuriosHelper;
import net.satisfy.camping.platform.Services;

import java.util.function.Consumer;
import java.util.function.Function;

public class CampingClientForge {

    public CampingClientForge(IEventBus modEventBus) {
        modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> event.enqueueWork(() -> {
            CampingClient.init();
            MenuScreens.register(CampingScreenHandlers.BACKPACK, BackpackScreen::new);
            if (Services.PLATFORM.isModLoaded("curios")) ForgeCuriosHelper.registerRenderersForCurios();
        }));
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
            addLayerToPlayerSkin(event, "default", EnderpackRenderLayer::new);
            addLayerToPlayerSkin(event, "slim", EnderpackRenderLayer::new);
            addLayerToPlayerSkin(event, "default", BackpackRenderLayer::new);
            addLayerToPlayerSkin(event, "slim", BackpackRenderLayer::new);
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <E extends Player, M extends HumanoidModel<E>> void addLayerToPlayerSkin(EntityRenderersEvent.AddLayers event, String skinName, Function<LivingEntityRenderer<E, M>, ? extends RenderLayer<E, M>> factory) {
        LivingEntityRenderer renderer = event.getSkin(skinName);
        if (renderer != null) renderer.addLayer(factory.apply(renderer));
    }
}
