package net.satisfy.camping.client;

//import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
//import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
//import dev.architectury.registry.client.rendering.RenderTypeRegistry;
//import dev.architectury.registry.menu.MenuRegistry;
//import dev.architectury.registry.registries.RegistrySupplier;
//import net.fabricmc.api.EnvType;
//import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;
import net.satisfy.camping.client.screen.BackpackScreen;
import net.satisfy.camping.client.screen.BackpackScreenHandler;
import net.satisfy.camping.core.registry.CampingScreenHandlers;
//import net.satisfy.camping.core.registry.EntityTypeRegistry;
//import net.satisfy.camping.core.registry.KeyHandlerRegistry;
//import net.satisfy.camping.core.registry.ScreenhandlerTypeRegistry;


public class CampingClient {

    // todo register the keybind

    public static void init() {
        MenuScreens.<BackpackScreenHandler, BackpackScreen>register(CampingScreenHandlers.BACKPACK, BackpackScreen::new);
    }
}
