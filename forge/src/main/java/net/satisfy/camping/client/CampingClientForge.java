package net.satisfy.camping.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.satisfy.camping.Camping;
import net.satisfy.camping.Constants;
import net.satisfy.camping.client.model.*;
import net.satisfy.camping.client.renderer.GrillRenderer;
import net.satisfy.camping.client.renderer.player.layers.*;
import net.satisfy.camping.core.registry.CampingBlockEntities;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.integration.CuriosBackpackRenderer;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import java.util.function.Function;

public class CampingClientForge {

    public static final ModelLayerLocation SMALL_BACKPACK_LAYER = new ModelLayerLocation(Camping.identifier("small_backpack"), "main");
    public static final ModelLayerLocation LARGE_BACKPACK_LAYER = new ModelLayerLocation(Camping.identifier("large_backpack"), "main");
    public static final ModelLayerLocation WANDERER_BACKPACK_LAYER = new ModelLayerLocation(Camping.identifier("wanderer_backpack"), "main");
    public static final ModelLayerLocation WANDERER_BAG_LAYER = new ModelLayerLocation(Camping.identifier("wanderer_bag"), "main");
    public static final ModelLayerLocation SHEEPBAG_LAYER = new ModelLayerLocation(Camping.identifier("sheepbag"), "main");
    public static final ModelLayerLocation GOODYBAG_LAYER = new ModelLayerLocation(Camping.identifier("goodybag"), "main");
    public static final ModelLayerLocation ENDERPACK_LAYER = new ModelLayerLocation(Camping.identifier("enderpack"), "main");
    public static final ModelLayerLocation ENDERBAG_LAYER = new ModelLayerLocation(Camping.identifier("enderbag"), "main");

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
            LivingEntityRenderer renderer = event.getSkin(skinName);
            if (renderer != null) renderer.addLayer(factory.apply(renderer));
        }
    }
}
