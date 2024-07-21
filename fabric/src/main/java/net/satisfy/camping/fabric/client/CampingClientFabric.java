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
import net.satisfy.camping.registry.ObjectRegistry;

import java.util.List;

public class CampingClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CampingClient.preInitClient();
        CampingClient.onInitializeClient();

        ItemTooltipCallback.EVENT.register(this::onItemTooltip);

        ArmorRenderer.register(new BackpackArmorRenderer(), ObjectRegistry.SMALL_BACKPACK_ITEM.get(), ObjectRegistry.LARGE_BACKPACK_ITEM.get(), ObjectRegistry.WANDERER_BACKPACK_ITEM.get());
        ArmorRenderer.register(new EnderpackArmorRenderer(), ObjectRegistry.ENDERPACK_ITEM.get());

        TrinketRendererRegistry.registerRenderer(ObjectRegistry.SMALL_BACKPACK_ITEM.get(), new BackpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(ObjectRegistry.LARGE_BACKPACK_ITEM.get(), new BackpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(ObjectRegistry.WANDERER_BACKPACK_ITEM.get(), new BackpackTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(ObjectRegistry.ENDERPACK_ITEM.get(), new BackpackTrinketRenderer());
    }

    private void onItemTooltip(ItemStack itemStack, TooltipFlag context, List<Component> tooltip) {
        CampingUtil.Grilling.addGrilledTooltip(itemStack, tooltip);
    }
}
