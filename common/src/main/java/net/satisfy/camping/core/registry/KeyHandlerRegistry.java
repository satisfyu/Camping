package net.satisfy.camping.core.registry;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.core.item.BackpackItem;
import net.satisfy.camping.core.item.EnderpackItem;
import net.satisfy.camping.core.network.OpenBackpackPacket;
import net.satisfy.camping.core.network.PacketHandler;
import net.satisfy.camping.core.platform.PlatformHelper;

@Environment(EnvType.CLIENT)
public class KeyHandlerRegistry {
    private static final String CATEGORY = "key.camping.category";
    private static final String OPEN_KEY = "key.camping.open";

    private static final KeyMapping open_key = new KeyMapping(OPEN_KEY, InputConstants.Type.KEYSYM, InputConstants.KEY_B, CATEGORY);

    public static void init() {
        KeyMappingRegistry.register(open_key);
        registerKeyHandler();
    }

    private static void registerKeyHandler() {
        ClientTickEvent.CLIENT_POST.register(client -> {
            if (open_key.consumeClick()) {
                Player player = client.player;
                if (player != null) {
                    if (PlatformHelper.isBackpackEquipped(player)) {
                        ItemStack backpackItem = PlatformHelper.getEquippedBackpack(player);
                        if (!backpackItem.isEmpty()) {
                            if (backpackItem.getItem() instanceof BackpackItem) {
                                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                                new OpenBackpackPacket(backpackItem).toBytes(buf);
                                NetworkManager.sendToServer(OpenBackpackPacket.ID, buf);
                            } else if (backpackItem.getItem() instanceof EnderpackItem) {
                                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                                NetworkManager.sendToServer(PacketHandler.OPEN_ENDER_CHEST_PACKET_ID, buf);
                            }
                        }
                    } else {
                        NetworkManager.sendToServer(PacketHandler.OPEN_ENDER_CHEST_PACKET_ID, new FriendlyByteBuf(Unpooled.buffer()));
                    }
                }
            }
        });
    }
}
