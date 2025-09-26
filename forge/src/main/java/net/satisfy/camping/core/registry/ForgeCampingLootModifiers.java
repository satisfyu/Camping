package net.satisfy.camping.core.registry;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.satisfy.camping.Camping;
import net.satisfy.camping.core.world.loot.AddItemModifier;

import java.util.function.BiConsumer;

public class ForgeCampingLootModifiers {

    public static final Codec<? extends IGlobalLootModifier> ADD_ITEM = AddItemModifier.CODEC.get();

    public static void register(BiConsumer<Codec<? extends IGlobalLootModifier>, ResourceLocation> consumer) {
        consumer.accept(ADD_ITEM, Camping.identifier("add_item"));
    }
}
