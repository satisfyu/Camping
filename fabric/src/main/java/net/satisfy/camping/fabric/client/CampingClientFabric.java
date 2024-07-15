package net.satisfy.camping.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.satisfy.camping.Util.CampingUtil;
import net.satisfy.camping.client.CampingClient;

import java.util.List;

public class CampingClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CampingClient.preInitClient();
        CampingClient.onInitializeClient();

        ItemTooltipCallback.EVENT.register(this::onItemTooltip);
    }

    private void onItemTooltip(ItemStack itemStack, TooltipFlag context, List<Component> tooltip) {
        CampingUtil.Grilling.addGrilledTooltip(itemStack, tooltip);
    }
}
