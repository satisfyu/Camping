package net.satisfy.camping.core.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.satisfy.camping.Camping;

import java.util.function.BiConsumer;

public class CampingItems {

    public static final Item MULTITOOL = new Item(new Item.Properties());
    public static final Item SMALL_BACKPACK = new Item(new Item.Properties());
    public static final Item LARGE_BACKPACK = new Item(new Item.Properties());
    public static final Item WANDERER_BACKPACK = new Item(new Item.Properties());
    public static final Item WANDERER_BAG = new Item(new Item.Properties());
    public static final Item GOODYBAG = new Item(new Item.Properties());
    public static final Item SHEEPBAG = new Item(new Item.Properties());
    public static final Item ENDERPACK = new Item(new Item.Properties());
    public static final Item ENDERBAG = new Item(new Item.Properties());

    public static void register(BiConsumer<Item, ResourceLocation> consumer) {
        consumer.accept(MULTITOOL, Camping.identifier("multitool"));
        consumer.accept(SMALL_BACKPACK, Camping.identifier("small_backpack"));
        consumer.accept(LARGE_BACKPACK, Camping.identifier("large_backpack"));
        consumer.accept(WANDERER_BACKPACK, Camping.identifier("wanderer_backpack"));
        consumer.accept(WANDERER_BAG, Camping.identifier("wanderer_bag"));
        consumer.accept(GOODYBAG, Camping.identifier("goodybag"));
        consumer.accept(SHEEPBAG, Camping.identifier("sheepbag"));
        consumer.accept(ENDERPACK, Camping.identifier("enderpack"));
        consumer.accept(ENDERBAG, Camping.identifier("enderbag"));
    }
}
