package net.satisfy.camping.client.keymap;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.KeyMapping;
import net.satisfy.camping.Constants;
import net.satisfy.camping.core.network.CampingMessagesFabric;
import org.lwjgl.glfw.GLFW;

public class OpenKeyFabric {

    public static KeyMapping OPEN_KEY;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(OPEN_KEY.consumeClick()) {
                ClientPlayNetworking.send(CampingMessagesFabric.ENDER_PACK, PacketByteBufs.create());
            }
        });
    }

    public static void register() {

        OPEN_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                Constants.KEY_NAME,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                Constants.KEY_CATEGORY
        ));

        registerKeyInputs();
    }
}
