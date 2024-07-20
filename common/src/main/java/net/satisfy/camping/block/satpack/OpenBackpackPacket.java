package net.satisfy.camping.block.satpack;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class OpenBackpackPacket {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ResourceLocation ID = new ResourceLocation(Camping.MODID, "open_backpack");
    private final BlockPos pos;

    public OpenBackpackPacket(BlockPos pos) {
        this.pos = pos;
    }

    public OpenBackpackPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkManager.PacketContext> contextSupplier) {
        NetworkManager.PacketContext context = contextSupplier.get();
        ServerPlayer player = (ServerPlayer) context.getPlayer();
        context.queue(() -> {
            LOGGER.info("Handling OpenBackpackPacket on server for player: {}", player.getName().getString());
            LOGGER.info("Player position: {}", pos);

            ItemStack backpackItem = player.getInventory().items.stream()
                    .filter(itemStack -> itemStack.getItem() instanceof BackpackItem)
                    .findFirst()
                    .orElse(ItemStack.EMPTY);

            if (!backpackItem.isEmpty()) {
                SimpleContainer backpackContainer = BackpackItem.getContainer(backpackItem);

                MenuProvider provider = new MenuProvider() {
                    @Override
                    public @NotNull Component getDisplayName() {
                        return Component.translatable("container.backpack");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
                        return new BackpackScreenHandler(syncId, playerInventory, backpackContainer);
                    }
                };

                player.openMenu(provider);
                LOGGER.info("Opened BackpackScreenHandler for player: {}", player.getName().getString());
            } else {
                LOGGER.warn("No BackpackItem found in player's inventory.");
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
