package net.satisfy.camping.platform.fabric;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.satisfy.camping.util.CampingUtil;
import net.satisfy.camping.fabric.config.ConfigFabric;
import net.satisfy.camping.item.BackpackItem;
import net.satisfy.camping.item.EnderpackItem;
import net.satisfy.camping.registry.ObjectRegistry;

import java.util.List;
import java.util.Optional;

public class PlatformHelperImpl {
    public static void addRegenEffect(Player player, MobEffectInstance effectInstance) {
        if (ConfigFabric.enableEffect) {
            player.addEffect(effectInstance);
        }
    }

    public static void setGrilled(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putBoolean("Grilled", true);
        CampingUtil.Grilling.increaseFoodValue(itemStack);
    }

    public static boolean isBackpackEquipped(Player player) {
        ItemStack chestSlotItem = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chestSlotItem.getItem() instanceof BackpackItem || chestSlotItem.getItem() instanceof EnderpackItem) {
            return true;
        }
        Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
        if (component.isPresent()) {
            TrinketComponent trinketComponent = component.get();
            return trinketComponent.isEquipped(ObjectRegistry.SMALL_BACKPACK_ITEM.get()) ||
                    trinketComponent.isEquipped(ObjectRegistry.LARGE_BACKPACK_ITEM.get()) ||
                    trinketComponent.isEquipped(ObjectRegistry.WANDERER_BACKPACK_ITEM.get()) ||
                    trinketComponent.isEquipped(ObjectRegistry.WANDERER_BAG_ITEM.get()) ||
                    trinketComponent.isEquipped(ObjectRegistry.SHEEPBAG_ITEM.get()) ||
                    trinketComponent.isEquipped(ObjectRegistry.GOODYBAG_ITEM.get()) ||
                    trinketComponent.isEquipped(ObjectRegistry.ENDERPACK_ITEM.get()) ||
                    trinketComponent.isEquipped(ObjectRegistry.ENDERBAG_ITEM.get());

        }
        return false;
    }

    public static ItemStack getEquippedBackpack(Player player) {
        ItemStack chestSlotItem = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chestSlotItem.getItem() instanceof BackpackItem || chestSlotItem.getItem() instanceof EnderpackItem) {
            return chestSlotItem;
        }
        Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
        if (component.isPresent()) {
            TrinketComponent trinketComponent = component.get();
            List<Tuple<SlotReference, ItemStack>> equippedItems;

            equippedItems = trinketComponent.getEquipped(ObjectRegistry.SMALL_BACKPACK_ITEM.get());
            if (!equippedItems.isEmpty()) {
                return equippedItems.get(0).getB();
            }

            equippedItems = trinketComponent.getEquipped(ObjectRegistry.LARGE_BACKPACK_ITEM.get());
            if (!equippedItems.isEmpty()) {
                return equippedItems.get(0).getB();
            }

            equippedItems = trinketComponent.getEquipped(ObjectRegistry.WANDERER_BACKPACK_ITEM.get());
            if (!equippedItems.isEmpty()) {
                return equippedItems.get(0).getB();
            }

            equippedItems = trinketComponent.getEquipped(ObjectRegistry.WANDERER_BAG_ITEM.get());
            if (!equippedItems.isEmpty()) {
                return equippedItems.get(0).getB();
            }

            equippedItems = trinketComponent.getEquipped(ObjectRegistry.SHEEPBAG_ITEM.get());
            if (!equippedItems.isEmpty()) {
                return equippedItems.get(0).getB();
            }

            equippedItems = trinketComponent.getEquipped(ObjectRegistry.GOODYBAG_ITEM.get());
            if (!equippedItems.isEmpty()) {
                return equippedItems.get(0).getB();
            }

            equippedItems = trinketComponent.getEquipped(ObjectRegistry.ENDERPACK_ITEM.get());
            if (!equippedItems.isEmpty()) {
                return equippedItems.get(0).getB();
            }

            equippedItems = trinketComponent.getEquipped(ObjectRegistry.ENDERBAG_ITEM.get());
            if (!equippedItems.isEmpty()) {
                return equippedItems.get(0).getB();
            }
        }
        return ItemStack.EMPTY;
    }
}