package net.satisfy.camping.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.Camping;
import net.satisfy.camping.block.BackpackContainer;
import net.satisfy.camping.client.screen.BackpackScreenHandler;
import net.satisfy.camping.item.BackpackItem;
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

    public void handle(Supplier<NetworkManager.PacketContext> contextSupplier) {
        NetworkManager.PacketContext context = contextSupplier.get();
        ServerPlayer player = (ServerPlayer) context.getPlayer();
        context.queue(() -> {
            if (backpackItem.getItem() instanceof BackpackItem) {
                BackpackContainer backpackContainer = BackpackItem.getContainer(backpackItem);

                MenuProvider provider = new MenuProvider() {
                    @Override
                    public @NotNull Component getDisplayName() {
                        return Component.translatable("container.camping.backpack");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
                        return new BackpackScreenHandler(syncId, playerInventory, backpackContainer);
                    }
                };

                player.openMenu(provider);
            }
        });
    }

    public static void register() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, ID, (buf, contextSupplier) -> {
            OpenBackpackPacket packet = new OpenBackpackPacket(buf);
            packet.handle(() -> contextSupplier);
        });
    }
}
