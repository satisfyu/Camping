package net.satisfy.camping;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.satisfy.camping.client.gui.screens.inventory.BackpackScreen;
import net.satisfy.camping.client.keys.FabricOpenBackpackKey;
import net.satisfy.camping.client.model.*;
import net.satisfy.camping.client.world.block.renderer.GrillRenderer;
import net.satisfy.camping.core.registry.CampingBlockEntities;
import net.satisfy.camping.core.registry.CampingBlocks;
import net.satisfy.camping.core.registry.CampingScreenHandlers;
import net.satisfy.camping.core.util.CampingUtil;
import net.satisfy.camping.core.world.inventory.BackpackScreenHandler;
import net.satisfy.camping.optional.trinkets.TrinketsHelper;
import net.satisfy.camping.platform.Services;

import java.util.List;
import java.util.stream.Stream;

public class CampingClientFabric implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        CampingClient.init();

        FabricOpenBackpackKey.register();

        // item tooltips
        ItemTooltipCallback.EVENT.register(this::onItemTooltip);

        // block entity renderers
        BlockEntityRenderers.register(CampingBlockEntities.GRILL, GrillRenderer::new);

        // block texture layer modification
        BlockRenderLayerMap.INSTANCE.putBlock(CampingBlocks.GRILL, RenderType.cutout());
        for (Block block : Stream.concat(CampingBlocks.TENT_MAIN.values().stream(), Stream.concat(CampingBlocks.TENT_MAIN_HEAD.values().stream(), Stream.concat(CampingBlocks.TENT_RIGHT.values().stream(), CampingBlocks.TENT_HEAD_RIGHT.values().stream()))).toList()) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
        }

        // entity layer registration
        EntityModelLayerRegistry.registerModelLayer(EnderpackModel.LAYER_LOCATION, EnderpackModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(EnderbagModel.LAYER_LOCATION, EnderbagModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(GoodybagModel.LAYER_LOCATION, GoodybagModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(LargeBackpackModel.LAYER_LOCATION, LargeBackpackModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(SheepbagModel.LAYER_LOCATION, SheepbagModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(SmallBackpackModel.LAYER_LOCATION, SmallBackpackModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(WandererBackpackModel.LAYER_LOCATION, WandererBackpackModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(WandererBagModel.LAYER_LOCATION, WandererBagModel::createBodyLayer);

        if (Services.PLATFORM.isModLoaded("trinkets")) TrinketsHelper.registerRenderersForTrinkets();

        // screens for screen handlers / menus
        MenuScreens.<BackpackScreenHandler, BackpackScreen>register(CampingScreenHandlers.BACKPACK, BackpackScreen::new);
    }

    private void onItemTooltip(ItemStack itemStack, TooltipFlag context, List<Component> tooltip) {
        CampingUtil.Grilling.addGrilledTooltip(itemStack, tooltip);
    }
}
