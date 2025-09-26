package net.satisfy.camping.core.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.core.world.BackpackContainer;
import net.satisfy.camping.core.world.block.entity.BackpackBlockEntity;
import net.satisfy.camping.core.world.inventory.BackpackScreenHandler;
import net.satisfy.camping.core.world.item.BackpackBlockItem;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.ParametersAreNullableByDefault;

@ParametersAreNonnullByDefault
@ParametersAreNullableByDefault
public class FabricOpenBackpackC2SPacket {
    public static void receive(ServerPlayer player) {
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (isBackpack(chest)) {
            openPortable(player, chest);
            return;
        }
        ItemStack trinket = getTrinketBackpack();
        if (isBackpack(trinket)) {
            openPortable(player, trinket);
        }
    }

    private static boolean isBackpack(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof BackpackBlockItem;
    }

    private static ItemStack getTrinketBackpack() {
        return ItemStack.EMPTY;
    }

    private static void openPortable(ServerPlayer player, ItemStack stack) {
        BackpackContainer container = new BackpackContainer(NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY));
        loadFromItem();
        MenuConstructor ctor = (syncId, inv, p) -> new BackpackScreenHandler(syncId, inv, container, BlockPos.ZERO);
        MenuProvider provider = new MenuProvider() {
            @Override
            public @NotNull Component getDisplayName() {
                return stack.getHoverName();
            }
            @Override
            public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int syncId, net.minecraft.world.entity.player.Inventory inv, net.minecraft.world.entity.player.Player p) {
                return ctor.createMenu(syncId, inv, p);
            }
        };
        player.openMenu(provider);
    }

    private static void loadFromItem() {
    }
}
