package net.satisfy.camping.fabric.client;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.satisfy.camping.Util.CampingUtil;
import net.satisfy.camping.client.CampingClient;
import net.satisfy.camping.fabric.client.renderer.BackpackArmorRenderer;
import net.satisfy.camping.fabric.client.renderer.BackpackTrinketRenderer;
import net.satisfy.camping.fabric.client.renderer.EnderpackArmorRenderer;
import net.satisfy.camping.fabric.client.renderer.EnderpackTrinketRenderer;

import java.util.List;

import static net.satisfy.camping.registry.ObjectRegistry.*;

public class CampingClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CampingClient.preInitClient();
        CampingClient.onInitializeClient();

        ItemTooltipCallback.EVENT.register(this::onItemTooltip);

        ArmorRenderer.register(new BackpackArmorRenderer(), SMALL_BACKPACK_ITEM.get(), LARGE_BACKPACK_ITEM.get(), WANDERER_BACKPACK_ITEM.get(), WANDERER_BAG_ITEM.get(), SHEEPBAG_ITEM.get(), GOODYBAG_ITEM.get());
        ArmorRenderer.register(new EnderpackArmorRenderer(), ENDERPACK_ITEM.get(), ENDERBAG_ITEM.get());

        TrinketRendererRegistry.registerRenderer(SMALL_BACKPACK_ITEM.get(), new BackpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(LARGE_BACKPACK_ITEM.get(), new BackpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(WANDERER_BACKPACK_ITEM.get(), new BackpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(WANDERER_BAG_ITEM.get(), new BackpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(SHEEPBAG_ITEM.get(), new BackpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(GOODYBAG_ITEM.get(), new BackpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(ENDERPACK_ITEM.get(), new EnderpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(ENDERBAG_ITEM.get(), new EnderpackTrinketRenderer());
    }

    private void onItemTooltip(ItemStack itemStack, TooltipFlag context, List<Component> tooltip) {
        CampingUtil.Grilling.addGrilledTooltip(itemStack, tooltip);
    }
}
