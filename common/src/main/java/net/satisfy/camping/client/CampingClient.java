package net.satisfy.camping.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.satisfy.camping.client.screen.BackpackScreen;
import net.satisfy.camping.client.screen.BackpackScreenHandler;
import net.satisfy.camping.core.registry.CampingScreenHandlers;

public class CampingClient {

    // todo register the keybind

    public static void init() {
        MenuScreens.<BackpackScreenHandler, BackpackScreen>register(CampingScreenHandlers.BACKPACK, BackpackScreen::new);
    }
}
