package net.satisfy.camping.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.item.DyeColor;
import net.satisfy.camping.Camping;
import net.satisfy.camping.util.CampingIdentifier;
import net.satisfy.camping.util.CampingUtil;
import net.satisfy.camping.block.*;
import net.satisfy.camping.block.BackpackBlock;
import net.satisfy.camping.item.BackpackItem;
import net.satisfy.camping.item.EnderpackItem;
import net.satisfy.camping.item.MultitoolItem;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ObjectRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Camping.MODID, Registries.ITEM);
    public static final Registrar<Item> ITEM_REGISTRAR = ITEMS.getRegistrar();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Camping.MODID, Registries.BLOCK);
    public static final Registrar<Block> BLOCK_REGISTRAR = BLOCKS.getRegistrar();

    public static final Map<String, RegistrySupplier<Block>> SLEEPING_BAGS = new HashMap<>();
    public static final RegistrySupplier<Block> GRILL = registerWithItem("grill", () -> new GrillBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON), 1));
    public static final RegistrySupplier<Item> MULTITOOL = registerItem("multitool", () -> new MultitoolItem(new Item.Properties().rarity(Rarity.COMMON).stacksTo(1).durability(92).fireResistant()));
    
    public static final BlockBehaviour.Properties BACKPACK_BEHAVIOUR = BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BIT).strength(1.5F).sound(SoundType.CANDLE).ignitedByLava().noOcclusion().noParticlesOnBreak().instabreak();
    
    public static final RegistrySupplier<Block> SMALL_BACKPACK = registerWithoutItem("small_backpack", () -> new BackpackBlock(BACKPACK_BEHAVIOUR, BackpackType.SMALL_BACKPACK));
    public static final RegistrySupplier<Item> SMALL_BACKPACK_ITEM = registerItem("small_backpack", () -> new BackpackItem(SMALL_BACKPACK.get(), new CampingIdentifier("textures/model/small_backpack.png")));
    public static final RegistrySupplier<Block> LARGE_BACKPACK = registerWithoutItem("large_backpack", () -> new BackpackBlock(BACKPACK_BEHAVIOUR, BackpackType.LARGE_BACKPACK));
    public static final RegistrySupplier<Item> LARGE_BACKPACK_ITEM = registerItem("large_backpack", () -> new BackpackItem(LARGE_BACKPACK.get(), new CampingIdentifier("textures/model/large_backpack.png")));
    public static final RegistrySupplier<Block> WANDERER_BACKPACK = registerWithoutItem("wanderer_backpack", () -> new BackpackBlock(BACKPACK_BEHAVIOUR, BackpackType.WANDERER_BACKPACK));
    public static final RegistrySupplier<Item> WANDERER_BACKPACK_ITEM = registerItem("wanderer_backpack", () -> new BackpackItem(WANDERER_BACKPACK.get(), new CampingIdentifier("textures/model/wanderer_pack.png")));
    public static final RegistrySupplier<Block> WANDERER_BAG = registerWithoutItem("wanderer_bag", () -> new BackpackBlock(BACKPACK_BEHAVIOUR, BackpackType.WANDERER_BAG));
    public static final RegistrySupplier<Item> WANDERER_BAG_ITEM = registerItem("wanderer_bag", () -> new BackpackItem(WANDERER_BAG.get(), new CampingIdentifier("textures/model/wanderer_bag.png")));
    public static final RegistrySupplier<Block> GOODYBAG = registerWithoutItem("goodybag", () -> new BackpackBlock(BACKPACK_BEHAVIOUR, BackpackType.GOODYBAG));
    public static final RegistrySupplier<Item> GOODYBAG_ITEM = registerItem("goodybag", () -> new BackpackItem(GOODYBAG.get(), new CampingIdentifier("textures/model/goodybag.png")));
    public static final RegistrySupplier<Block> SHEEPBAG = registerWithoutItem("sheepbag", () -> new BackpackBlock(BACKPACK_BEHAVIOUR, BackpackType.SHEEPBAG));
    public static final RegistrySupplier<Item> SHEEPBAG_ITEM = registerItem("sheepbag", () -> new BackpackItem(SHEEPBAG.get(), new CampingIdentifier("textures/model/sheepbag.png")));

    public static final RegistrySupplier<Block> ENDERPACK = registerWithoutItem("enderpack", () -> new EnderpackBlock(BACKPACK_BEHAVIOUR, EnderpackBlock.BackpackType.ENDERPACK));
    public static final RegistrySupplier<Item> ENDERPACK_ITEM = registerItem("enderpack", () -> new EnderpackItem(ENDERPACK.get(), ArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, new CampingIdentifier("textures/model/enderpack.png"), new Item.Properties()));
    public static final RegistrySupplier<Block> ENDERBAG = registerWithoutItem("enderbag", () -> new EnderpackBlock(BACKPACK_BEHAVIOUR, EnderpackBlock.BackpackType.ENDERBAG));
    public static final RegistrySupplier<Item> ENDERBAG_ITEM = registerItem("enderbag", () -> new EnderpackItem(ENDERBAG.get(), ArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, new CampingIdentifier("textures/model/enderbag.png"), new Item.Properties()));

    public static final String[] COLORS = {
            "white", "light_gray", "gray", "black", "red", "orange", "yellow", "lime", "green", "cyan", "light_blue", "blue", "purple", "magenta", "pink", "brown"
    };

    public static final Map<String, RegistrySupplier<Block>> TENT_MAIN = new HashMap<>();
    public static final Map<String, RegistrySupplier<Block>> TENT_MAIN_HEAD = new HashMap<>();
    public static final Map<String, RegistrySupplier<Block>> TENT_RIGHT = new HashMap<>();
    public static final Map<String, RegistrySupplier<Block>> TENT_HEAD_RIGHT = new HashMap<>();

    static {
        for (String color : COLORS) {
            DyeColor dyeColor = DyeColor.valueOf(color.toUpperCase());
            SLEEPING_BAGS.put(color, registerWithItem("sleeping_bag_" + color, () -> new SleepingBagBlock(dyeColor, BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE).instabreak().mapColor(DyeColor.WHITE))));
            TENT_MAIN.put(color, registerWithItem("tent_" + color, () -> new TentMainBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE).instabreak(), dyeColor)));
            TENT_MAIN_HEAD.put(color, registerWithoutItem("tent_head_" + color, () -> new TentMainHeadBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE).instabreak())));
            TENT_RIGHT.put(color, registerWithoutItem("tent_right_" + color, () -> new TentRightBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE).instabreak())));
            TENT_HEAD_RIGHT.put(color, registerWithoutItem("tent_head_right_" + color, () -> new TentRightHeadBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE).instabreak())));
        }
        BLOCKS.register();
        ITEMS.register();
    }

    public static <T extends Block> RegistrySupplier<T> registerWithItem(String name, Supplier<T> block) {
        return CampingUtil.registerWithItem(BLOCKS, BLOCK_REGISTRAR, ITEMS, ITEM_REGISTRAR, new CampingIdentifier(name), block);
    }

    public static <T extends Block> RegistrySupplier<T> registerWithoutItem(String path, Supplier<T> block) {
        return CampingUtil.registerWithoutItem(BLOCKS, BLOCK_REGISTRAR, new CampingIdentifier(path), block);
    }

    public static <T extends Item> RegistrySupplier<T> registerItem(String path, Supplier<T> itemSupplier) {
        return CampingUtil.registerItem(ITEMS, ITEM_REGISTRAR, new CampingIdentifier(path), itemSupplier);
    }
}
