package net.satisfy.camping.neoforge.client.keymap;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.satisfy.camping.Constants;
import org.lwjgl.glfw.GLFW;

public class OpenKeyNeoForge {

    public static final KeyMapping OPEN_KEY = new KeyMapping(
            Constants.KEY_NAME,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            Constants.KEY_CATEGORY
    );
}
