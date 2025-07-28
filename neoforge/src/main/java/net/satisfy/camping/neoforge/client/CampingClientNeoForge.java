package net.satisfy.camping.neoforge.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.satisfy.camping.Camping;
import net.satisfy.camping.Constants;
import net.satisfy.camping.client.CampingClient;
import net.satisfy.camping.neoforge.client.keymap.OpenKeyNeoForge;
import net.satisfy.camping.client.model.*;
import net.satisfy.camping.client.renderer.GrillRenderer;
import net.satisfy.camping.neoforge.client.renderer.player.layers.EnderbagLayer;
import net.satisfy.camping.neoforge.client.renderer.player.layers.EnderpackLayer;
import net.satisfy.camping.neoforge.client.renderer.player.layers.GoodybagLayer;
import net.satisfy.camping.neoforge.client.renderer.player.layers.LargeBackpackLayer;
import net.satisfy.camping.neoforge.client.renderer.player.layers.SheepbagLayer;
import net.satisfy.camping.neoforge.client.renderer.player.layers.SmallBackpackLayer;
import net.satisfy.camping.neoforge.client.renderer.player.layers.WandererBackpackLayer;
import net.satisfy.camping.neoforge.client.renderer.player.layers.WandererBagLayer;
import net.satisfy.camping.neoforge.core.network.NetworkEventHandler;
import net.satisfy.camping.neoforge.core.network.packet.NeoForgeOpenEnderPackC2SPacket;
import net.satisfy.camping.core.registry.CampingBlockEntities;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.neoforge.integration.CuriosBackpackRenderer;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import java.util.function.Function;

public class CampingClientNeoForge {

    public static final ModelLayerLocation SMALL_BACKPACK_LAYER = new ModelLayerLocation(Camping.identifier("small_backpack"), "main");
    public static final ModelLayerLocation LARGE_BACKPACK_LAYER = new ModelLayerLocation(Camping.identifier("large_backpack"), "main");
    public static final ModelLayerLocation WANDERER_BACKPACK_LAYER = new ModelLayerLocation(Camping.identifier("wanderer_backpack"), "main");
    public static final ModelLayerLocation WANDERER_BAG_LAYER = new ModelLayerLocation(Camping.identifier("wanderer_bag"), "main");
    public static final ModelLayerLocation SHEEPBAG_LAYER = new ModelLayerLocation(Camping.identifier("sheepbag"), "main");
    public static final ModelLayerLocation GOODYBAG_LAYER = new ModelLayerLocation(Camping.identifier("goodybag"), "main");
    public static final ModelLayerLocation ENDERPACK_LAYER = new ModelLayerLocation(Camping.identifier("enderpack"), "main");
    public static final ModelLayerLocation ENDERBAG_LAYER = new ModelLayerLocation(Camping.identifier("enderbag"), "main");

    @EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModBusEvents {

        @SubscribeEvent
        public static void onClientSetup(final FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                CampingClient.init();

                CuriosRendererRegistry.register(
                        CampingItems.SMALL_BACKPACK,
                        () -> new CuriosBackpackRenderer(CuriosBackpackRenderer.BackpackType.SMALL_BACKPACK)
                );
                CuriosRendererRegistry.register(
                        CampingItems.ENDERPACK,
                        () -> new CuriosBackpackRenderer(CuriosBackpackRenderer.BackpackType.ENDERPACK)
                );
                CuriosRendererRegistry.register(
                        CampingItems.ENDERBAG,
                        () -> new CuriosBackpackRenderer(CuriosBackpackRenderer.BackpackType.ENDERBAG)
                );
                CuriosRendererRegistry.register(
                        CampingItems.GOODYBAG,
                        () -> new CuriosBackpackRenderer(CuriosBackpackRenderer.BackpackType.GOODYBAG)
                );
                CuriosRendererRegistry.register(
                        CampingItems.LARGE_BACKPACK,
                        () -> new CuriosBackpackRenderer(CuriosBackpackRenderer.BackpackType.LARGE_BACKPACK)
                );
                CuriosRendererRegistry.register(
                        CampingItems.SHEEPBAG,
                        () -> new CuriosBackpackRenderer(CuriosBackpackRenderer.BackpackType.SHEEPBAG)
                );
                CuriosRendererRegistry.register(
                        CampingItems.WANDERER_BACKPACK,
                        () -> new CuriosBackpackRenderer(CuriosBackpackRenderer.BackpackType.WANDERER_BACKPACK)
                );
                CuriosRendererRegistry.register(
                        CampingItems.WANDERER_BAG,
                        () -> new CuriosBackpackRenderer(CuriosBackpackRenderer.BackpackType.WANDERER_BAG)
                );
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

        @SubscribeEvent
        public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(CampingBlockEntities.GRILL, GrillRenderer::new);
        }

        @SubscribeEvent
        public static void constructLayers(EntityRenderersEvent.AddLayers event) {
            addLayerToPlayerSkin(event, "default", SmallBackpackLayer::new);
            addLayerToPlayerSkin(event, "slim", SmallBackpackLayer::new);
            addLayerToPlayerSkin(event, "default", LargeBackpackLayer::new);
            addLayerToPlayerSkin(event, "slim", LargeBackpackLayer::new);
            addLayerToPlayerSkin(event, "default", WandererBackpackLayer::new);
            addLayerToPlayerSkin(event, "slim", WandererBackpackLayer::new);
            addLayerToPlayerSkin(event, "default", WandererBagLayer::new);
            addLayerToPlayerSkin(event, "slim", WandererBagLayer::new);
            addLayerToPlayerSkin(event, "default", SheepbagLayer::new);
            addLayerToPlayerSkin(event, "slim", SheepbagLayer::new);
            addLayerToPlayerSkin(event, "default", GoodybagLayer::new);
            addLayerToPlayerSkin(event, "slim", GoodybagLayer::new);
            addLayerToPlayerSkin(event, "default", EnderpackLayer::new);
            addLayerToPlayerSkin(event, "slim", EnderpackLayer::new);
            addLayerToPlayerSkin(event, "default", EnderbagLayer::new);
            addLayerToPlayerSkin(event, "slim", EnderbagLayer::new);
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        private static <E extends Player, M extends HumanoidModel<E>>
        void addLayerToPlayerSkin(EntityRenderersEvent.AddLayers event, String skinName, Function<LivingEntityRenderer<E, M>, ? extends RenderLayer<E, M>> factory) {
            LivingEntityRenderer renderer = event.getSkin(PlayerSkin.Model.byName(skinName));
            if (renderer != null) renderer.addLayer(factory.apply(renderer));
        }

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(OpenKeyNeoForge.OPEN_KEY);
        }
    }

    @EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class ForgeBusEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (OpenKeyNeoForge.OPEN_KEY.consumeClick()) {
                NetworkEventHandler.sendToServer(new NeoForgeOpenEnderPackC2SPacket());
            }
        }
    }
}
