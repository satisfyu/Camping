package net.satisfy.camping.core.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.satisfy.camping.Camping;
import net.satisfy.camping.core.world.block.*;

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

    public static final Block GRILL = new GrillBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON).lightLevel((state) -> state.getValue(GrillBlock.LIT) ? 10 : 0), 1);

    public static final BlockBehaviour.Properties BACKPACK_BEHAVIOUR = BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BIT).strength(1.5F).sound(SoundType.CANDLE).ignitedByLava().noOcclusion().noParticlesOnBreak().instabreak();
    public static final Block SMALL_BACKPACK = new BackpackBlock(BACKPACK_BEHAVIOUR, BackpackType.SMALL_BACKPACK);
    public static final Block LARGE_BACKPACK = new BackpackBlock(BACKPACK_BEHAVIOUR, BackpackType.LARGE_BACKPACK);
    public static final Block WANDERER_BACKPACK = new BackpackBlock(BACKPACK_BEHAVIOUR, BackpackType.WANDERER_BACKPACK);
    public static final Block WANDERER_BAG = new BackpackBlock(BACKPACK_BEHAVIOUR, BackpackType.WANDERER_BAG);
    public static final Block GOODYBAG = new BackpackBlock(BACKPACK_BEHAVIOUR, BackpackType.GOODYBAG);
    public static final Block SHEEPBAG = new BackpackBlock(BACKPACK_BEHAVIOUR, BackpackType.SHEEPBAG);
    public static final Block ENDERPACK = new EnderpackBlock(BACKPACK_BEHAVIOUR, EnderpackBlock.BackpackType.ENDERPACK);
    public static final Block ENDERBAG = new EnderpackBlock(BACKPACK_BEHAVIOUR, EnderpackBlock.BackpackType.ENDERBAG);

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

            DyeColor dyeColor = DyeColor.valueOf(color.toUpperCase(Locale.ENGLISH));

            Block coloredSleepingBag = new SleepingBagBlock(dyeColor, BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE).instabreak().mapColor(DyeColor.WHITE));
            Block coloredTentMain = new TentMainBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE).instabreak(), dyeColor);
            Block coloredTentMainHead = new TentMainHeadBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE).instabreak(), dyeColor);
            Block coloredTentRight = new TentRightBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE).instabreak(), dyeColor);
            Block coloredTentHeadRight = new TentRightHeadBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE).instabreak(), dyeColor);

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
