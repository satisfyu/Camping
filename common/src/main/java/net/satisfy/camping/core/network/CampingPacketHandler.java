package net.satisfy.camping.core.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.satisfy.camping.client.screen.BackpackScreenHandler;
import net.satisfy.camping.core.inventory.BackpackContainer;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.world.block.entity.BackpackBlockEntity;
import net.satisfy.camping.platform.PlatformHelper;

public class CampingPacketHandler {

    public static void openEnderPackMenu(Player player) {

        ItemStack equipped = PlatformHelper.getEquippedBackpack(player);
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

        CustomData blockEntityTag = equipped.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY);
        BlockPos playerPos = player.blockPosition();

        if (blockEntityTag.copyTag() != null) {
            if (blockEntityTag.copyTag().contains("Items", 9)) {
                NonNullList<ItemStack> itemStacks = NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY);
                ContainerHelper.loadAllItems(blockEntityTag.copyTag(), itemStacks, player.level().registryAccess());

                player.openMenu(new SimpleMenuProvider(
                        (i, inventory, p) -> new BackpackScreenHandler(i, inventory, new BackpackContainer(itemStacks, p), playerPos),
                        Component.translatable("container.camping.backpack")
                ));
            }
        } else {
            CompoundTag compoundTag = new CompoundTag();
            ContainerHelper.saveAllItems(compoundTag, NonNullList.withSize(24, ItemStack.EMPTY), player.level().registryAccess());
            equipped.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(compoundTag));

            NonNullList<ItemStack> itemStacks = NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY);
            blockEntityTag = equipped.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY);
            ContainerHelper.loadAllItems(blockEntityTag.copyTag(), itemStacks, player.level().registryAccess());

            player.openMenu(new SimpleMenuProvider(
                    (i, inventory, p) -> new BackpackScreenHandler(i, inventory, new BackpackContainer(itemStacks, p), playerPos),
                    Component.translatable("container.camping.backpack")
            ));
        }
    }
}
