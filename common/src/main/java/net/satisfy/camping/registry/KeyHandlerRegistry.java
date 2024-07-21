package net.satisfy.camping.registry;

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
import net.satisfy.camping.item.BackpackItem;
import net.satisfy.camping.network.OpenBackpackPacket;
import net.satisfy.camping.platform.PlatformHelper;

@Environment(EnvType.CLIENT)
public class KeyHandlerRegistry {
    private static final String CATEGORY = "key.camping.category";
    private static final String OPEN_BACKPACK_KEY = "key.camping.open_backpack";
    private static final KeyMapping open_backpack = new KeyMapping(OPEN_BACKPACK_KEY, InputConstants.Type.KEYSYM, InputConstants.KEY_B, CATEGORY);

    static {
        KeyMappingRegistry.register(open_backpack);
        registerKeyHandler();
    }

    private static void registerKeyHandler() {
        ClientTickEvent.CLIENT_POST.register(client -> {
            if (open_backpack.consumeClick()) {
                Player player = client.player;
                if (player != null) {
                    if (PlatformHelper.isBackpackEquipped(player)) {
                        ItemStack backpackItem = PlatformHelper.getEquippedBackpack(player);
                        if (!backpackItem.isEmpty() && backpackItem.getItem() instanceof BackpackItem) {
                            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                            new OpenBackpackPacket(backpackItem).toBytes(buf);
                            NetworkManager.sendToServer(OpenBackpackPacket.ID, buf);
                        }
                    }
                }
            }
        });
    }
}
