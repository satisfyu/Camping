package net.satisfy.camping.platform.forge;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.item.BackpackItem;
import net.satisfy.camping.util.CampingUtil;

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

    public static void addRegenEffect(Player player, MobEffectInstance effectInstance) {
    }

    public static void setGrilled(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putBoolean("Grilled", true);
        CampingUtil.Grilling.increaseFoodValue(itemStack);
    }
}
