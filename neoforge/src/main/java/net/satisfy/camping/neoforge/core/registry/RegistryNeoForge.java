package net.satisfy.camping.neoforge.core.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.satisfy.camping.core.registry.CampingBlockEntities;
import net.satisfy.camping.core.registry.CampingBlocks;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.registry.CampingTab;
import net.satisfy.camping.neoforge.CampingNeoForge;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RegistryNeoForge {

    public static void register(IEventBus modEventBus) {
        bind(Registries.BLOCK, CampingBlocks::register);
        bind(Registries.BLOCK_ENTITY_TYPE, CampingBlockEntities::register);
        bind(Registries.ITEM, CampingItems::register);
        bind(Registries.CREATIVE_MODE_TAB, CampingTab::register);

        CampingLootModifiersNeoForge.register(modEventBus);
        //CampingRecipesNeoForge.register(modEventBus); // TODO fixme recipes
    }

    private static <T> void bind(ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
        CampingNeoForge.EVENT_BUS.addListener((RegisterEvent event) -> {
            if (registry.equals(event.getRegistryKey())) {
                source.accept((t, rl) -> event.register(registry, rl, () -> t));
            }
        });
    }
}
