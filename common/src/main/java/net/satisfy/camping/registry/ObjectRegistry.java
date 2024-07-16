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
import net.satisfy.camping.Camping;
import net.satisfy.camping.Util.CampingIdentifier;
import net.satisfy.camping.Util.CampingUtil;
import net.satisfy.camping.block.GrillBlock;
import net.satisfy.camping.block.SleepingBagBlock;
import net.satisfy.camping.block.TentMainBlock;
import net.satisfy.camping.block.TentMainHeadBlock;
import net.satisfy.camping.block.TentRightBlock;
import net.satisfy.camping.block.TentRightHeadBlock;
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
    public static final RegistrySupplier<Block> ENDER_BACKPACK_BLOCK = registerWithoutItem("ender_backpack", () -> new Block(BlockBehaviour.Properties.copy(Blocks.RED_WOOL)));;
    public static final RegistrySupplier<Item> MULTITOOL = registerItem("multitool", () -> new MultitoolItem(new Item.Properties().rarity(Rarity.COMMON).stacksTo(1).durability(92).fireResistant()));


    public static final RegistrySupplier<Block> TENT_MAIN = registerWithItem("tent_main", () -> new TentMainBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE)));
    public static final RegistrySupplier<Block> TENT_MAIN_HEAD = registerWithoutItem("tent_main_head", () -> new TentMainHeadBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE)));
    public static final RegistrySupplier<Block> TENT_RIGHT = registerWithoutItem("tent_right", () -> new TentRightBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE)));
    public static final RegistrySupplier<Block> TENT_HEAD_RIGHT = registerWithoutItem("tent_head_right", () -> new TentRightHeadBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE)));



    public static final String[] colors = {
            "white", "light_gray", "gray", "black", "red", "orange", "yellow", "lime", "green", "cyan", "light_blue", "blue", "purple", "magenta", "pink", "brown"
    };


    static {

        // sleeping bags
        for (String color : colors) {
            SLEEPING_BAGS.put(color, registerWithItem("sleeping_bag_" + color, () -> new SleepingBagBlock(DyeColor.BLUE, Block.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.IGNORE))));
        }


        registerItem("ender_backpack", () -> new EnderBackpackItem(ArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

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
