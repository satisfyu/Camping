package net.satisfy.camping.client.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.satisfy.camping.core.network.FabricCampingNetwork;
import org.lwjgl.glfw.GLFW;

public class FabricOpenBackpackKey {
    public static final String KEY_NAME = "Open Backpack";
    public static final String KEY_CATEGORY = "[Let's Do] Camping";
    public static KeyMapping OPEN_BACKPACK;
    private static long lastSendNs;

    public static void register() {
        OPEN_BACKPACK = KeyBindingHelper.registerKeyBinding(new KeyMapping(KEY_NAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, KEY_CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_BACKPACK.consumeClick()) {
                if (client.player != null && client.screen == null) {
                    long now = System.nanoTime();
                    if (now - lastSendNs >= 150_000_000L && ClientPlayNetworking.canSend(FabricCampingNetwork.Packets.OPEN_BACKPACK)) {
                        ClientPlayNetworking.send(FabricCampingNetwork.Packets.OPEN_BACKPACK, net.fabricmc.fabric.api.networking.v1.PacketByteBufs.create());
                        lastSendNs = now;
                    }
                }
            }
        });
    }
}
