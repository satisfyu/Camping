package net.satisfy.camping.client;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.satisfy.camping.client.keymap.OpenKeyFabric;
import net.satisfy.camping.client.model.*;
import net.satisfy.camping.client.renderer.BackpackTrinketRenderer;
import net.satisfy.camping.client.renderer.EnderpackTrinketRenderer;
import net.satisfy.camping.client.renderer.GrillRenderer;
import net.satisfy.camping.core.registry.CampingBlockEntities;
import net.satisfy.camping.core.registry.CampingBlocks;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.util.CampingUtil;

import java.util.List;
import java.util.stream.Stream;

public class CampingClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CampingClient.init();
        OpenKeyFabric.register();
        adjustBlockRenderLayers();
        registerEntityModelLayers();
        BlockEntityRenderers.register(CampingBlockEntities.GRILL, GrillRenderer::new);
    }

    public static void adjustBlockRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(CampingBlocks.GRILL, RenderType.cutout());
        for (Block block : Stream.concat(CampingBlocks.TENT_MAIN.values().stream(), Stream.concat(CampingBlocks.TENT_MAIN_HEAD.values().stream(), Stream.concat(CampingBlocks.TENT_RIGHT.values().stream(), CampingBlocks.TENT_HEAD_RIGHT.values().stream()))).toList()) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
        }
    }

    public static void registerEntityModelLayers() {
        EntityModelLayerRegistry.registerModelLayer(SmallBackpackModel.LAYER_LOCATION, SmallBackpackModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(LargeBackpackModel.LAYER_LOCATION, LargeBackpackModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(WandererBackpackModel.LAYER_LOCATION, WandererBackpackModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(WandererBagModel.LAYER_LOCATION, WandererBagModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(GoodybagModel.LAYER_LOCATION, GoodybagModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(SheepbagModel.LAYER_LOCATION, SheepbagModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(EnderpackModel.LAYER_LOCATION, EnderpackModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(EnderbagModel.LAYER_LOCATION, EnderbagModel::createBodyLayer);

        TrinketRendererRegistry.registerRenderer(CampingItems.SMALL_BACKPACK, new BackpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(CampingItems.LARGE_BACKPACK, new BackpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(CampingItems.WANDERER_BACKPACK, new BackpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(CampingItems.WANDERER_BAG, new BackpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(CampingItems.SHEEPBAG, new BackpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(CampingItems.GOODYBAG, new BackpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(CampingItems.ENDERPACK, new EnderpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(CampingItems.ENDERBAG, new EnderpackTrinketRenderer());
    }

    private void onItemTooltip(ItemStack itemStack, TooltipFlag context, List<Component> tooltip) {
        CampingUtil.Grilling.addGrilledTooltip(itemStack, tooltip);
    }
}
