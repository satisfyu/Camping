package net.satisfy.camping.platform.forge;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.item.BackpackItem;

public class PlatformHelperImpl {

    public static boolean isBackpackEquipped(Player player) {
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack.getItem() instanceof BackpackItem) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack getEquippedBackpack(Player player) {
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack.getItem() instanceof BackpackItem) {
                return itemStack;
            }
        }
        return ItemStack.EMPTY;
    }
}
