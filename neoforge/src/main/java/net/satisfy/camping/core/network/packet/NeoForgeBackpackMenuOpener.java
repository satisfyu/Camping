package net.satisfy.camping.core.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.world.BackpackContainer;
import net.satisfy.camping.core.world.block.entity.BackpackBlockEntity;
import net.satisfy.camping.core.world.inventory.ForgeBackpackScreenHandler;
import net.satisfy.camping.platform.Services;

public final class NeoForgeBackpackMenuOpener {
    public static void open(ServerPlayer player) {
        ItemStack equipped = Services.PLATFORM.getEquippedBackpack(player);
        if (equipped == null || equipped.isEmpty()) return;
        if (equipped.is(CampingItems.ENDERPACK) || equipped.is(CampingItems.ENDERBAG)) {
            player.openMenu(new SimpleMenuProvider(
                    (id, inv, p) -> ChestMenu.threeRows(id, inv, player.getEnderChestInventory()),
                    Component.translatable("container.camping.enderpack")
            ));
            return;
        }
        boolean isBackpack = false;
        for (Item backpack : CampingItems.BACKPACKS.get()) {
            if (equipped.is(backpack)) { isBackpack = true; break; }
        }
        if (!isBackpack) return;

        NonNullList<ItemStack> items = NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY);
        var provider = player.registryAccess();
        CustomData beData = equipped.get(DataComponents.BLOCK_ENTITY_DATA);
        if (beData != null) {
            CompoundTag tag = beData.copyTag();
            if (tag.contains("Items", 9)) {
                ContainerHelper.loadAllItems(tag, items, provider);
            }
        } else {
            CompoundTag init = new CompoundTag();
            ContainerHelper.saveAllItems(init, NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY), true, provider);
            equipped.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(init));
        }

        BlockPos pos = player.blockPosition();
        player.openMenu(new SimpleMenuProvider(
                (id, inv, p) -> new ForgeBackpackScreenHandler(id, inv, new BackpackContainer(items, p, EquipmentSlot.CHEST), pos),
                Component.translatable("container.camping.backpack")
        ));
    }


    private NeoForgeBackpackMenuOpener() {}
}
