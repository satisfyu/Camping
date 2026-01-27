package net.satisfy.camping.optional.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
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
        return !alreadyEquippedBackpack(player, slot);
    }

    public static boolean alreadyEquippedBackpack(Player player, SlotReference ignoredSlot) {
        for (ItemStack armorStack : player.getArmorSlots()) {
            if (isBackpackItem(armorStack)) {
                return true;
            }
        }

        Optional<TrinketComponent> componentOptional = TrinketsApi.getTrinketComponent(player);
        if (componentOptional.isEmpty()) {
            return false;
        }

        TrinketComponent component = componentOptional.get();
        Map<String, Map<String, TrinketInventory>> inventoryMap = component.getInventory();

        for (Map<String, TrinketInventory> trinketGroup : inventoryMap.values()) {
            for (TrinketInventory trinketInventory : trinketGroup.values()) {
                for (int slotIndex = 0; slotIndex < trinketInventory.getContainerSize(); slotIndex++) {
                    if (ignoredSlot != null && ignoredSlot.inventory() == trinketInventory && ignoredSlot.index() == slotIndex) {
                        continue;
                    }
                    ItemStack trinketStack = trinketInventory.getItem(slotIndex);
                    if (isBackpackItem(trinketStack)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static boolean isBackpackItem(ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() instanceof BackpackBlockItem || stack.getItem() instanceof EnderpackBlockItem);
    }
} 