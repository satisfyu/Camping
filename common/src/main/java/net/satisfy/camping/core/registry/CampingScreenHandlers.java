package net.satisfy.camping.core.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.satisfy.camping.Camping;
import net.satisfy.camping.client.screen.BackpackScreenHandler;

import java.util.function.BiConsumer;

public class CampingScreenHandlers {

    public static final MenuType<BackpackScreenHandler> BACKPACK = new MenuType<>(BackpackScreenHandler::new, FeatureFlags.VANILLA_SET);

    public static void register(BiConsumer<MenuType<?>, ResourceLocation> consumer) {
        consumer.accept(BACKPACK, Camping.identifier("backpack"));
    }
}
