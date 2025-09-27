package net.satisfy.camping.core.network.packet;

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
import net.minecraftforge.network.NetworkHooks;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.world.BackpackContainer;
import net.satisfy.camping.core.world.block.entity.BackpackBlockEntity;
import net.satisfy.camping.core.world.inventory.ForgeBackpackScreenHandler;
import net.satisfy.camping.platform.Services;

public final class ForgeBackpackMenuOpener {

    public static void open(ServerPlayer player) {
        ItemStack equipped = Services.PLATFORM.getEquippedBackpack(player);
        if (equipped == null || equipped == ItemStack.EMPTY) return;

        if (equipped.is(CampingItems.ENDERPACK) || equipped.is(CampingItems.ENDERBAG)) {
            NetworkHooks.openScreen(player, new SimpleMenuProvider(
                    (id, inv, p) -> ChestMenu.threeRows(id, inv, player.getEnderChestInventory()),
                    Component.translatable("container.camping.enderpack")
            ));
            return;
        }

        boolean isBackpack = false;
        for (Item backpack : CampingItems.BACKPACKS.get()) {
            if (equipped.is(backpack)) {
                isBackpack = true;
                break;
            }
        }
        if (!isBackpack) return;

        CompoundTag tag = BlockItem.getBlockEntityData(equipped);
        NonNullList<ItemStack> items = NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY);
        if (tag != null && tag.contains("Items", 9)) {
            ContainerHelper.loadAllItems(tag, items);
        } else {
            CompoundTag init = new CompoundTag();
            ContainerHelper.saveAllItems(init, NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY));
            equipped.addTagElement("BlockEntityTag", init);
        }

        BlockPos pos = player.blockPosition();
        NetworkHooks.openScreen(player, new SimpleMenuProvider(
                (id, inv, p) -> new ForgeBackpackScreenHandler(id, inv, new BackpackContainer(items, p), pos),
                Component.translatable("container.camping.backpack")
        ));
    }

    private ForgeBackpackMenuOpener() {}
}
