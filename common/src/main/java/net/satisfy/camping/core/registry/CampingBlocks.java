package net.satisfy.camping.core.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.satisfy.camping.Camping;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

public class CampingBlocks {

    public static final String[] COLORS = {"white", "light_gray", "gray", "black", "red", "orange", "yellow", "lime", "green", "cyan", "light_blue", "blue", "purple", "magenta", "pink", "brown"};

    public static final Map<String, Block> SLEEPING_BAGS = new HashMap<>();
    public static final Map<String, Block> TENT_MAIN = new HashMap<>();
    public static final Map<String, Block> TENT_MAIN_HEAD = new HashMap<>();
    public static final Map<String, Block> TENT_RIGHT = new HashMap<>();
    public static final Map<String, Block> TENT_HEAD_RIGHT = new HashMap<>();

    public static final Block GRILL = new Block(BlockBehaviour.Properties.of());
    public static final Block SMALL_BACKPACK = new Block(BlockBehaviour.Properties.of());
    public static final Block LARGE_BACKPACK = new Block(BlockBehaviour.Properties.of());
    public static final Block WANDERER_BACKPACK = new Block(BlockBehaviour.Properties.of());
    public static final Block WANDERER_BAG = new Block(BlockBehaviour.Properties.of());
    public static final Block GOODYBAG = new Block(BlockBehaviour.Properties.of());
    public static final Block SHEEPBAG = new Block(BlockBehaviour.Properties.of());
    public static final Block ENDERPACK = new Block(BlockBehaviour.Properties.of());
    public static final Block ENDERBAG = new Block(BlockBehaviour.Properties.of());

    public static final Block[] BACKPACKS = new Block[]{SMALL_BACKPACK, LARGE_BACKPACK, WANDERER_BACKPACK, WANDERER_BAG, GOODYBAG, SHEEPBAG};
    public static final Block[] ENDERPACKS = new Block[]{ENDERPACK, ENDERBAG};

    public static void register(BiConsumer<Block, ResourceLocation> consumer) {

        consumer.accept(GRILL, Camping.identifier("grill"));
        consumer.accept(SMALL_BACKPACK, Camping.identifier("small_backpack"));
        consumer.accept(LARGE_BACKPACK, Camping.identifier("large_backpack"));
        consumer.accept(WANDERER_BACKPACK, Camping.identifier("wanderer_backpack"));
        consumer.accept(WANDERER_BAG, Camping.identifier("wanderer_bag"));
        consumer.accept(GOODYBAG, Camping.identifier("goodybag"));
        consumer.accept(SHEEPBAG, Camping.identifier("sheepbag"));
        consumer.accept(ENDERPACK, Camping.identifier("enderpack"));
        consumer.accept(ENDERBAG, Camping.identifier("enderbag"));

        for (String color : COLORS) {

            // DyeColor dyeColor = DyeColor.valueOf(color.toUpperCase(Locale.ENGLISH));

            Block coloredSleepingBag = new Block(BlockBehaviour.Properties.of());
            Block coloredTentMain = new Block(BlockBehaviour.Properties.of());
            Block coloredTentMainHead = new Block(BlockBehaviour.Properties.of());
            Block coloredTentRight = new Block(BlockBehaviour.Properties.of());
            Block coloredTentHeadRight = new Block(BlockBehaviour.Properties.of());

            SLEEPING_BAGS.put(color, coloredSleepingBag);
            TENT_MAIN.put(color, coloredTentMain);
            TENT_MAIN_HEAD.put(color, coloredTentMainHead);
            TENT_RIGHT.put(color, coloredTentRight);
            TENT_HEAD_RIGHT.put(color, coloredTentHeadRight);

            consumer.accept(coloredSleepingBag, Camping.identifier("sleeping_bag_" + color));
            consumer.accept(coloredTentMain, Camping.identifier("tent_" + color));
            consumer.accept(coloredTentMainHead, Camping.identifier("tent_head_" + color));
            consumer.accept(coloredTentRight, Camping.identifier("tent_right_" + color));
            consumer.accept(coloredTentHeadRight, Camping.identifier("tent_head_right_" + color));
        }
    }
}
