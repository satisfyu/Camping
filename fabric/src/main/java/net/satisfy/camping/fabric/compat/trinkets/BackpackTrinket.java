package net.satisfy.camping.fabric.compat.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.core.item.BackpackItem;
import net.satisfy.camping.core.item.EnderpackItem;

import java.util.Map;
import java.util.Optional;

public class BackpackTrinket implements Trinket {

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
    }

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
            if (itemStack.getItem() instanceof BackpackItem || itemStack.getItem() instanceof EnderpackItem) {

                return true;
            }
        }


        Optional<TrinketComponent> componentOptional = TrinketsApi.getTrinketComponent(player);
        if (componentOptional.isPresent()) {
            TrinketComponent component = componentOptional.get();
            Map<String, Map<String, dev.emi.trinkets.api.TrinketInventory>> inventoryMap = component.getInventory();

            for (Map<String, dev.emi.trinkets.api.TrinketInventory> trinketGroup : inventoryMap.values()) {
                for (dev.emi.trinkets.api.TrinketInventory trinketInventory : trinketGroup.values()) {
                    for (int i = 0; i < trinketInventory.getContainerSize(); i++) {
                        ItemStack trinketStack = trinketInventory.getItem(i);
                        if (trinketStack != null && (trinketStack.getItem() instanceof BackpackItem || trinketStack.getItem() instanceof EnderpackItem)) {

                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
