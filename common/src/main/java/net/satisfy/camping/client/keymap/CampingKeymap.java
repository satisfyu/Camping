package net.satisfy.camping.client.keymap;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.satisfy.camping.Constants;
import net.satisfy.camping.core.registry.CampingNetworks;
import org.lwjgl.glfw.GLFW;

@SuppressWarnings("removal")
public class CampingKeymap {

    public static KeyMapping OPEN_KEY = new KeyMapping(
            Constants.KEY_NAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            Constants.KEY_CATEGORY
    );

    public static void register() {
        KeyMappingRegistry.register(OPEN_KEY);
        ClientTickEvent.CLIENT_POST.register(minecraft -> {
            while (OPEN_KEY.consumeClick()) {
                var player = minecraft.player;
                if (player != null) {
                    RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), player.registryAccess());
                    NetworkManager.sendToServer(CampingNetworks.ENDER_PACK_ID, buf);
                }
            }
        });
    }
}
