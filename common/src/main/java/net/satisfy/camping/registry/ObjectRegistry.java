package net.satisfy.camping.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.item.DyeColor;
import net.satisfy.camping.Camping;
import net.satisfy.camping.Util.CampingIdentifier;
import net.satisfy.camping.Util.CampingUtil;
import net.satisfy.camping.block.*;
import net.satisfy.camping.item.MultitoolItem;
import net.satisfy.camping.item.backpack.EnderBackpackItem;

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

    public static final RegistrySupplier<Item> SMALL_BACKPACK = registerItem("small_backpack", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> LARGE_BACKPACK = registerItem("large_backpack", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WANDERER_BACKPACK = registerItem("wanderer_backpack", () -> new Item(new Item.Properties()));


    public static final RegistrySupplier<Block> ENDER_BACKPACK_BLOCK = registerWithoutItem("ender_backpack", () -> new Block(BlockBehaviour.Properties.copy(Blocks.RED_WOOL)));
    public static final RegistrySupplier<Item> ENDER_BACKPACK = registerItem("ender_backpack", () -> new Item(new Item.Properties()));

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

            SLEEPING_BAGS.put(color, registerWithItem("sleeping_bag_" + color, () -> new SleepingBagBlock(dyeColor, BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE))));
            TENT_MAIN.put(color, registerWithItem("tent_" + color, () -> new TentMainBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE), dyeColor)));
            TENT_MAIN_HEAD.put(color, registerWithoutItem("tent_head_" + color, () -> new TentMainHeadBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE))));
            TENT_RIGHT.put(color, registerWithoutItem("tent_right_" + color, () -> new TentRightBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE))));
            TENT_HEAD_RIGHT.put(color, registerWithoutItem("tent_head_right_" + color, () -> new TentRightHeadBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE))));
        }

        //registerItem("ender_backpack", () -> new EnderBackpackItem(ArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

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
