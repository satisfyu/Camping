package net.satisfy.camping.fabric.compat.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.item.BackpackItem;
import net.satisfy.camping.item.EnderpackItem;

import java.util.Optional;

public class BackpackTrinket implements Trinket {

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        final Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(entity);

        component.ifPresent(trinketComponent -> trinketComponent.getInventory().forEach((s, stringTrinketInventoryMap) ->
                stringTrinketInventoryMap.forEach((s1, trinketInventory) -> {
                    trinketInventory.removeItem(slot.index(), 1);
                    trinketInventory.markUpdate();
                })
        ));
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        return !alreadyEquippedBackpack((Player) entity);
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        Player player = (Player) entity;

        if (alreadyEquippedBackpack(player)) {
            dropChestplateItemStack(player);
        }
    }

    private static void dropChestplateItemStack(Player player) {
        ItemStack chestplateItemStack = player.getItemBySlot(EquipmentSlot.CHEST).copy();

        if (!chestplateItemStack.isEmpty()) {
            player.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
            player.getInventory().setItem(EquipmentSlot.CHEST.getIndex(), ItemStack.EMPTY);

            if (!player.level().isClientSide) {
                ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY() + 1, player.getZ(), chestplateItemStack);
                player.level().addFreshEntity(itemEntity);
            }
        }
    }

    public static boolean alreadyEquippedBackpack(Player player) {
        boolean[] alreadyEquipped = {false};

        player.getArmorSlots().forEach(itemStack -> {
            if (itemStack.getItem() instanceof BackpackItem || itemStack.getItem() instanceof EnderpackItem) {
                alreadyEquipped[0] = true;
            }
        });

        Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
        component.ifPresent(trinketComponent -> trinketComponent.getInventory().forEach((s, stringTrinketInventoryMap) ->
                stringTrinketInventoryMap.forEach((s1, trinketInventory) -> {
                    for (int i = 0; i < trinketInventory.getContainerSize(); i++) {
                        ItemStack trinketStack = trinketInventory.getItem(i);
                        if (trinketStack.getItem() instanceof BackpackItem || trinketStack.getItem() instanceof EnderpackItem) {
                            alreadyEquipped[0] = true;
                            break;
                        }
                    }
                })
        ));

        return alreadyEquipped[0];
    }
}
