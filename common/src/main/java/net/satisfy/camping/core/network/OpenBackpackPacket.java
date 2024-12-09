package net.satisfy.camping.core.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.satisfy.camping.Camping;
import net.satisfy.camping.core.block.entity.BackpackBlockEntity;
import net.satisfy.camping.client.screen.BackpackScreenHandler;
import net.satisfy.camping.core.inventory.BackpackContainer;
import net.satisfy.camping.core.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class OpenBackpackPacket {
    public static final ResourceLocation ID = new ResourceLocation(Camping.MODID, "open_backpack");
    private final ItemStack backpackItem;

    public OpenBackpackPacket(ItemStack backpackItem) {
        this.backpackItem = backpackItem;
    }

    public OpenBackpackPacket(FriendlyByteBuf buf) {
        this.backpackItem = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItem(backpackItem);
    }

    @SuppressWarnings("all")
    public void handle(Supplier<NetworkManager.PacketContext> contextSupplier) {
        Player player = contextSupplier.get().getPlayer();
        Level level = player.level();
        ItemStack itemStack = PlatformHelper.getEquippedBackpack(player);

        if (level.isClientSide()) {
            return;
        }

        CompoundTag blockEntityTag = BlockItem.getBlockEntityData(itemStack);
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
            itemStack.addTagElement("BlockEntityTag", compoundTag);

            NonNullList<ItemStack> itemStacks = NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY);
            blockEntityTag = BlockItem.getBlockEntityData(itemStack);
            ContainerHelper.loadAllItems(blockEntityTag, itemStacks);

            player.openMenu(new SimpleMenuProvider(
                    (i, inventory, p) -> new BackpackScreenHandler(i, inventory, new BackpackContainer(itemStacks, p), playerPos),
                    Component.translatable("container.camping.backpack")
            ));
        }
    }

    public static void register() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, ID, (buf, contextSupplier) -> {
            OpenBackpackPacket packet = new OpenBackpackPacket(buf);
            packet.handle(() -> contextSupplier);
        });
    }
}
