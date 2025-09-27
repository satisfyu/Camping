package net.satisfy.camping.client.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.NeoForge;
import net.satisfy.camping.core.network.NeoForgeCampingNetwork;
import net.satisfy.camping.core.network.packet.NeoForgeOpenBackpackC2SPacket;
import org.lwjgl.glfw.GLFW;

public class NeoForgeOpenBackpackKey {
    public static final String KEY_NAME = "Open Backpack";
    public static final String KEY_CATEGORY = "[Let's Do] Camping";
    public static KeyMapping OPEN_BACKPACK;

    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        OPEN_BACKPACK = new KeyMapping(KEY_NAME, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, KEY_CATEGORY);
        event.register(OPEN_BACKPACK);
    }

    public static void registerInputHandler() {
        NeoForge.EVENT_BUS.addListener((InputEvent.Key e) -> {
            if (e.getAction() == GLFW.GLFW_PRESS && OPEN_BACKPACK != null && OPEN_BACKPACK.isActiveAndMatches(InputConstants.getKey(e.getKey(), e.getScanCode()))) {
                NeoForgeCampingNetwork.sendToServer(NeoForgeOpenBackpackC2SPacket.INSTANCE);
            }
        });
    }
}
