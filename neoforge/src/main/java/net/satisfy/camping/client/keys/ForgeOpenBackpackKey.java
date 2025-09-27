package net.satisfy.camping.client.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.satisfy.camping.core.network.ForgeCampingNetwork;
import net.satisfy.camping.core.network.packet.ForgeOpenBackpackC2SPacket;
import org.lwjgl.glfw.GLFW;

public class ForgeOpenBackpackKey {

    public static final String KEY_NAME = "Open Backpack";
    public static final String KEY_CATEGORY = "[Let's Do] Camping";
    public static KeyMapping OPEN_BACKPACK;

    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        OPEN_BACKPACK = new KeyMapping(
                KEY_NAME,
                KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                KEY_CATEGORY
        );
        event.register(OPEN_BACKPACK);
    }

    public static void registerInputHandler() {
        MinecraftForge.EVENT_BUS.addListener((InputEvent.Key e) -> {
            if (e.getAction() == GLFW.GLFW_PRESS &&
                    OPEN_BACKPACK != null &&
                    OPEN_BACKPACK.isActiveAndMatches(InputConstants.getKey(e.getKey(), e.getScanCode()))) {
                ForgeCampingNetwork.sendToServer(new ForgeOpenBackpackC2SPacket());
            }
        });
    }
}
