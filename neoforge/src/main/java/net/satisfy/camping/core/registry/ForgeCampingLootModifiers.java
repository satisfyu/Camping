package net.satisfy.camping.core.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.satisfy.camping.Camping;
import net.satisfy.camping.core.world.loot.AddItemModifier;

import java.util.function.BiConsumer;

public class ForgeCampingLootModifiers {

    public static final MapCodec<? extends IGlobalLootModifier> ADD_ITEM = AddItemModifier.CODEC;

    public static void register(BiConsumer<MapCodec<? extends IGlobalLootModifier>, ResourceLocation> consumer) {
        consumer.accept(ADD_ITEM, Camping.identifier("add_item"));
    }
}
