package net.satisfy.camping.fabric.core.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.camping.core.registry.CampingBlockEntities;
import net.satisfy.camping.core.registry.CampingBlocks;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.registry.CampingTab;
import net.satisfy.camping.fabric.core.crafting.BackpackWrapperLookup;

import java.util.function.BiConsumer;

public class RegistryFabric {

    public static void register() {
        CampingBlocks.register(bind(BuiltInRegistries.BLOCK));
        CampingBlockEntities.register(bind(BuiltInRegistries.BLOCK_ENTITY_TYPE));
        CampingItems.register(bind(BuiltInRegistries.ITEM));
        CampingTab.register(bind(BuiltInRegistries.CREATIVE_MODE_TAB));

        CampingLootModifiersFabric.register();
        //CampingRecipesFabric.register();//TODO fixme recipes

        BackpackWrapperLookup.init();
    }

    private static <T> BiConsumer<T, ResourceLocation> bind(Registry<? super T> registry) {
        return (t, id) -> Registry.register(registry, id, t);
    }
}
