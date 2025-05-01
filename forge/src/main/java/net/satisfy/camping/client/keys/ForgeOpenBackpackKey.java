package net.satisfy.camping.client.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.satisfy.camping.CampingForge;
import net.satisfy.camping.core.network.ForgeCampingNetwork;
import net.satisfy.camping.core.network.packet.ForgeOpenBackpackC2SPacket;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class ForgeOpenBackpackKey {

    public static final String KEY_NAME = "Open Backpack";
    public static final String KEY_CATEGORY = "[Let's Do] Camping";

    public static KeyMapping OPEN_BACKPACK;

    public static void register() {

        OPEN_BACKPACK = new KeyMapping(
                KEY_NAME,
                KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                KEY_CATEGORY
        );

        CampingForge.EVENT_BUS.addListener((Consumer<RegisterKeyMappingsEvent>) event -> {
            event.register(OPEN_BACKPACK);
        });

        MinecraftForge.EVENT_BUS.addListener((Consumer<InputEvent.Key>) event -> {
            if (OPEN_BACKPACK.consumeClick()) {
                ForgeCampingNetwork.sendToServer(new ForgeOpenBackpackC2SPacket());
            }
        });
    }
}
