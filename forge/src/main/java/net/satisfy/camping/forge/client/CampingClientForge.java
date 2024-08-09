package net.satisfy.camping.forge.client;

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
import net.minecraftforge.registries.RegisterEvent;
import net.satisfy.camping.Camping;
import net.satisfy.camping.client.CampingClient;
import net.satisfy.camping.client.model.*;
import net.satisfy.camping.forge.client.renderer.player.layers.*;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = Camping.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CampingClientForge {
    public static final ModelLayerLocation SMALL_BACKPACK_LAYER = new ModelLayerLocation(new ResourceLocation(Camping.MODID, "small_backpack"), "main");
    public static final ModelLayerLocation LARGE_BACKPACK_LAYER = new ModelLayerLocation(new ResourceLocation(Camping.MODID, "large_backpack"), "main");
    public static final ModelLayerLocation WANDERER_BACKPACK_LAYER = new ModelLayerLocation(new ResourceLocation(Camping.MODID, "wanderer_backpack"), "main");
    public static final ModelLayerLocation WANDERER_BAG_LAYER = new ModelLayerLocation(new ResourceLocation(Camping.MODID, "wanderer_bag"), "main");
    public static final ModelLayerLocation SHEEPBAG_LAYER = new ModelLayerLocation(new ResourceLocation(Camping.MODID, "sheepbag"), "main");
    public static final ModelLayerLocation GOODYBAG_LAYER = new ModelLayerLocation(new ResourceLocation(Camping.MODID, "goodybag"), "main");
    public static final ModelLayerLocation ENDERPACK_LAYER = new ModelLayerLocation(new ResourceLocation(Camping.MODID, "enderpack"), "main");
    public static final ModelLayerLocation ENDERBAG_LAYER = new ModelLayerLocation(new ResourceLocation(Camping.MODID, "enderbag"), "main");

    @SubscribeEvent
    public static void beforeClientSetup(RegisterEvent event) {
        CampingClient.preInitClient();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        CampingClient.onInitializeClient();
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SMALL_BACKPACK_LAYER, SmallBackpackModel::createBodyLayer);
        event.registerLayerDefinition(LARGE_BACKPACK_LAYER, LargeBackpackModel::createBodyLayer);
        event.registerLayerDefinition(WANDERER_BACKPACK_LAYER, WandererBackpackModel::createBodyLayer);
        event.registerLayerDefinition(WANDERER_BAG_LAYER, WandererBagModel::createBodyLayer);
        event.registerLayerDefinition(SHEEPBAG_LAYER, SheepbagModel::createBodyLayer);
        event.registerLayerDefinition(GOODYBAG_LAYER, GoodybagModel::createBodyLayer);
        event.registerLayerDefinition(ENDERPACK_LAYER, EnderpackModel::createBodyLayer);
        event.registerLayerDefinition(ENDERBAG_LAYER, EnderbagModel::createBodyLayer);
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
