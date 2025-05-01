package net.satisfy.camping.optional.trinkets;

import dev.emi.trinkets.api.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.core.world.item.BackpackBlockItem;
import net.satisfy.camping.core.world.item.EnderpackBlockItem;

import java.util.Map;
import java.util.Optional;

public class BackpackTrinket implements Trinket {

    @Override
    public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return false;
        }
        return !alreadyEquippedBackpack(player);
    }

    /**
     * Checks if the player has a BackpackItem or EnderpackItem already equipped
     * in any armor or trinket slots.
     *
     * @param player The player to check for equipped backpacks.
     * @return True if a backpack or enderbag is already equipped; otherwise, false.
     */
    public static boolean alreadyEquippedBackpack(Player player) {

        for (ItemStack itemStack : player.getArmorSlots()) {
            if (itemStack.getItem() instanceof BackpackBlockItem || itemStack.getItem() instanceof EnderpackBlockItem) {

                return true;
            }
        }


        Optional<TrinketComponent> componentOptional = TrinketsApi.getTrinketComponent(player);
        if (componentOptional.isPresent()) {
            TrinketComponent component = componentOptional.get();
            Map<String, Map<String, TrinketInventory>> inventoryMap = component.getInventory();

            for (Map<String, TrinketInventory> trinketGroup : inventoryMap.values()) {
                for (TrinketInventory trinketInventory : trinketGroup.values()) {
                    for (int i = 0; i < trinketInventory.getContainerSize(); i++) {
                        ItemStack trinketStack = trinketInventory.getItem(i);
                        if (trinketStack != null && (trinketStack.getItem() instanceof BackpackBlockItem || trinketStack.getItem() instanceof EnderpackBlockItem)) {

                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
