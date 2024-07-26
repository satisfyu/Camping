package net.satisfy.camping.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.satisfy.camping.Camping;
import net.satisfy.camping.util.CampingIdentifier;
import net.satisfy.camping.client.screen.BackpackScreenHandler;

import java.util.function.Supplier;

public class ScreenhandlerTypeRegistry {

    private static final Registrar<MenuType<?>> MENU_TYPES = DeferredRegister.create(Camping.MODID, Registries.MENU).getRegistrar();

    public static final RegistrySupplier<MenuType<BackpackScreenHandler>> BACKPACK_SCREENHANDLER = register("backpack_screenhandler", () -> new MenuType<>(BackpackScreenHandler::new, FeatureFlags.VANILLA_SET));


    public static <T extends AbstractContainerMenu> RegistrySupplier<MenuType<T>> register(String name, Supplier<MenuType<T>> menuType){
        return MENU_TYPES.register(new CampingIdentifier(name), menuType);
    }
}
