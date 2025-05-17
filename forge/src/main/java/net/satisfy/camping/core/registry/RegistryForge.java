package net.satisfy.camping.core.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.satisfy.camping.Camping;
import net.satisfy.camping.CampingForge;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RegistryForge {

    public static void register(IEventBus modEventBus) {
        bind(Registries.BLOCK, CampingBlocks::register);
        bind(Registries.BLOCK_ENTITY_TYPE, CampingBlockEntities::register);
        bind(Registries.ITEM, CampingItems::register);
        bind(Registries.CREATIVE_MODE_TAB, RegistryForge::registerTab);

        bind(Registries.MENU, CampingScreenHandlers::register);

        bind(Registries.ENTITY_TYPE, CampingEntities::register);

        bind(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ForgeCampingLootModifiers::register);
        bind(ForgeRegistries.Keys.RECIPE_SERIALIZERS, ForgeCampingRecipes::register);
    }

    private static <T> void bind(ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
        CampingForge.EVENT_BUS.addListener((RegisterEvent event) -> {
            if (registry.equals(event.getRegistryKey())) {
                source.accept((t, rl) -> event.register(registry, rl, () -> t));
            }
        });
    }

    public static final CreativeModeTab CREATIVE_TAB = CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(() -> new ItemStack(CampingBlocks.GRILL))
            .title(Component.translatable("itemGroup.camping"))
            .displayItems((itemDisplayParameters, output) -> CampingItems.CREATIVE_TAB_ITEMS.forEach(output::accept))
            .build();

    public static void registerTab(BiConsumer<CreativeModeTab, ResourceLocation> consumer) {
        consumer.accept(CREATIVE_TAB, Camping.identifier("creative_tab"));
    }
}
