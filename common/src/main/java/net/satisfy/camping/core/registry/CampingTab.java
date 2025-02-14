package net.satisfy.camping.core.registry;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.Camping;

import java.util.function.BiConsumer;

public class CampingTab {

    public static final CreativeModeTab CAMPING = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(CampingBlocks.LARGE_BACKPACK))
            .title(Component.translatable("itemGroup.camping.camping_tab"))
            .displayItems((itemDisplayParameters, output) -> {
                CampingItems.CREATIVE_TAB_ITEMS.forEach(output::accept);
            })
            .build();

    public static void register(BiConsumer<CreativeModeTab, ResourceLocation> consumer) {
        consumer.accept(CAMPING, Camping.identifier("camping"));
    }
}
