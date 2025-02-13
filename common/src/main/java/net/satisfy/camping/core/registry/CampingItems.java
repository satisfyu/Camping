package net.satisfy.camping.core.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.satisfy.camping.Camping;
import net.satisfy.camping.core.world.item.BackpackItem;
import net.satisfy.camping.core.world.item.EnderpackItem;
import net.satisfy.camping.core.world.item.MultitoolItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class CampingItems {

    public static final Item MULTITOOL = new MultitoolItem(new Item.Properties().rarity(Rarity.COMMON).stacksTo(1).durability(92).fireResistant());
    public static final Item SMALL_BACKPACK = new BackpackItem(CampingBlocks.SMALL_BACKPACK, Camping.identifier("textures/model/small_backpack.png"));
    public static final Item LARGE_BACKPACK = new BackpackItem(CampingBlocks.LARGE_BACKPACK, Camping.identifier("textures/model/large_backpack.png"));
    public static final Item WANDERER_BACKPACK = new BackpackItem(CampingBlocks.WANDERER_BACKPACK, Camping.identifier("textures/model/wanderer_backpack.png"));
    public static final Item WANDERER_BAG = new BackpackItem(CampingBlocks.WANDERER_BAG, Camping.identifier("textures/model/wanderer_bag.png"));
    public static final Item GOODYBAG = new BackpackItem(CampingBlocks.GOODYBAG, Camping.identifier("textures/model/goodybag.png"));
    public static final Item SHEEPBAG = new BackpackItem(CampingBlocks.SHEEPBAG, Camping.identifier("textures/model/sheepbag.png"));
    public static final Item ENDERPACK = new EnderpackItem(CampingBlocks.ENDERPACK, ArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, Camping.identifier("textures/model/enderpack.png"), new Item.Properties());
    public static final Item ENDERBAG = new EnderpackItem(CampingBlocks.ENDERBAG, ArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, Camping.identifier("textures/model/enderbag.png"), new Item.Properties());

    public static final Item[] BACKPACKS = new Item[]{SMALL_BACKPACK, LARGE_BACKPACK, WANDERER_BACKPACK, WANDERER_BAG, GOODYBAG, SHEEPBAG};

    public static final List<ItemLike> CREATIVE_TAB_ITEMS = new ArrayList<>();

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
        consumer.accept(new BlockItem(CampingBlocks.GRILL, new Item.Properties()), BuiltInRegistries.BLOCK.getKey(CampingBlocks.GRILL));

        CREATIVE_TAB_ITEMS.add(Items.BUNDLE);
        CREATIVE_TAB_ITEMS.add(SMALL_BACKPACK);
        CREATIVE_TAB_ITEMS.add(LARGE_BACKPACK);
        CREATIVE_TAB_ITEMS.add(WANDERER_BACKPACK);
        CREATIVE_TAB_ITEMS.add(WANDERER_BAG);
        CREATIVE_TAB_ITEMS.add(GOODYBAG);
        CREATIVE_TAB_ITEMS.add(SHEEPBAG);
        CREATIVE_TAB_ITEMS.add(ENDERPACK);
        CREATIVE_TAB_ITEMS.add(ENDERBAG);
        CREATIVE_TAB_ITEMS.add(MULTITOOL);
        CREATIVE_TAB_ITEMS.add(CampingBlocks.GRILL);

        CampingBlocks.SLEEPING_BAGS.forEach((blockName, sleepingBagBlock) -> {
            consumer.accept(new BlockItem(sleepingBagBlock, new Item.Properties()), BuiltInRegistries.BLOCK.getKey(sleepingBagBlock));
            CREATIVE_TAB_ITEMS.add(sleepingBagBlock);
        });

        CampingBlocks.TENT_MAIN.forEach((blockName, tentMainBlock) -> {
            consumer.accept(new BlockItem(tentMainBlock, new Item.Properties()), BuiltInRegistries.BLOCK.getKey(tentMainBlock));
            CREATIVE_TAB_ITEMS.add(tentMainBlock);
        });
    }
}
