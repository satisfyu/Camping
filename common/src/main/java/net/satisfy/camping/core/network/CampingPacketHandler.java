package net.satisfy.camping.core.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.client.screen.BackpackScreenHandler;
import net.satisfy.camping.core.inventory.BackpackContainer;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.world.block.entity.BackpackBlockEntity;
import net.satisfy.camping.platform.Services;

public class CampingPacketHandler {

    public static void openEnderPackMenu(ServerPlayer player) {

        ItemStack equipped = Services.PLATFORM.getEquippedBackpack(player);
        if (equipped == null || equipped == ItemStack.EMPTY) return;

        // HANDLE ENDERPACKS
        if (equipped.is(CampingItems.ENDERPACK) || equipped.is(CampingItems.ENDERBAG)) {
            player.openMenu(new SimpleMenuProvider((i, inventory, playerX) -> {
                return ChestMenu.threeRows(i, inventory, player.getEnderChestInventory());
            }, Component.translatable("container.camping.enderpack")));
        }

        // HANDLE BACKPACKS
        boolean isBackpack = false;
        for (Item backpack : CampingItems.BACKPACKS) {
            if (equipped.is(backpack)) isBackpack = true;
        }
        if (!isBackpack) return;

        CompoundTag blockEntityTag = BlockItem.getBlockEntityData(equipped);
        BlockPos playerPos = player.blockPosition();

        if (blockEntityTag != null) {
            if (blockEntityTag.contains("Items", 9)) {
                NonNullList<ItemStack> itemStacks = NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY);
                ContainerHelper.loadAllItems(blockEntityTag, itemStacks);

                player.openMenu(new SimpleMenuProvider(
                        (i, inventory, p) -> new BackpackScreenHandler(i, inventory, new BackpackContainer(itemStacks, p), playerPos),
                        Component.translatable("container.camping.backpack")
                ));
            }
        } else {
            CompoundTag compoundTag = new CompoundTag();
            ContainerHelper.saveAllItems(compoundTag, NonNullList.withSize(24, ItemStack.EMPTY));
            equipped.addTagElement("BlockEntityTag", compoundTag);

            NonNullList<ItemStack> itemStacks = NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY);
            blockEntityTag = BlockItem.getBlockEntityData(equipped);
            ContainerHelper.loadAllItems(blockEntityTag, itemStacks);

            player.openMenu(new SimpleMenuProvider(
                    (i, inventory, p) -> new BackpackScreenHandler(i, inventory, new BackpackContainer(itemStacks, p), playerPos),
                    Component.translatable("container.camping.backpack")
            ));
        }
    }
}
