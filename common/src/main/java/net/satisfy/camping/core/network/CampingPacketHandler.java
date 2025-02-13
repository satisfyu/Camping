package net.satisfy.camping.core.network;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.platform.Services;

public class CampingPacketHandler {

    public static void openEnderPackMenu(ServerPlayer player) {

        ItemStack equipped = Services.PLATFORM.getEquippedBackpack(player);
        if (equipped == null || equipped == ItemStack.EMPTY) return;

        if (equipped.is(CampingItems.ENDERPACK) || equipped.is(CampingItems.GOODYBAG)) {
            player.openMenu(new SimpleMenuProvider((i, inventory, playerX) -> {
                return ChestMenu.threeRows(i, inventory, player.getEnderChestInventory());
            }, Component.translatable("container.camping.enderpack")));
        }
    }
}
