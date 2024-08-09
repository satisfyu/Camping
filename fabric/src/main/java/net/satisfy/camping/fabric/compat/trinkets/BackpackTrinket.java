package net.satisfy.camping.fabric.compat.trinkets;

import dev.emi.trinkets.api.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.satisfy.camping.item.BackpackItem;
import net.satisfy.camping.item.EnderpackItem;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class BackpackTrinket implements Trinket {

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {

        final Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(entity);

        component.ifPresent(trinketComponent -> trinketComponent.getInventory().forEach(((s, stringTrinketInventoryMap) -> stringTrinketInventoryMap.forEach(((s1, trinketInventory) -> {
            trinketInventory.removeItem(slot.index(), 1);
            trinketInventory.markUpdate();
        })))));
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        return !alreadyEquippedBackpack((Player) entity);
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {

        final Player player = (Player) entity;

        if (alreadyEquippedBackpack(player)) {
            dropChestplateItemStack(player);
        }
    }

    private static void dropChestplateItemStack(Player player) {

        final ItemStack chestplateItemStack = player.getItemBySlot(EquipmentSlot.CHEST).copy();

        try (Level level = player.level()) {
            if (level.addFreshEntity(new ItemEntity(level, player.xo, player.yo + 1, player.zo, chestplateItemStack))) {
                player.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
                player.getInventory().setItem(EquipmentSlot.CHEST.getIndex(), ItemStack.EMPTY);
            } else if (player.addItem(chestplateItemStack)) {
                player.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
                player.getInventory().setItem(EquipmentSlot.CHEST.getIndex(), ItemStack.EMPTY);
            }
        } catch (IOException e) {
            // NO-OP
        }
    }

    public static boolean alreadyEquippedBackpack(Player player) {

        final AtomicBoolean alreadyEquipped = new AtomicBoolean(false);

        player.getArmorSlots().forEach((itemStack -> {
            if (itemStack.getItem() instanceof BackpackItem || itemStack.getItem() instanceof EnderpackItem) alreadyEquipped.set(true);
        }));

        final Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
        component.ifPresent(trinketComponent -> trinketComponent.getInventory().forEach(((s, stringTrinketInventoryMap) -> stringTrinketInventoryMap.forEach(((s1, trinketInventory) -> {

            for (int i = 0; i < trinketInventory.getContainerSize(); i++) {

                if (trinketInventory.getItem(i).getItem() instanceof BackpackItem || trinketInventory.getItem(i).getItem() instanceof BackpackItem) {
                    dropChestplateItemStack(player);
                }
            }

            trinketInventory.markUpdate();

        })))));

        return alreadyEquipped.get();
    }
}
