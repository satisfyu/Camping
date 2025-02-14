package net.satisfy.camping.core.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.satisfy.camping.Camping;

public class CampingTags {
    public static final TagKey<Item> BACKPACK_BLACKLIST = TagKey.create(Registries.ITEM, Camping.identifier("backpack_blacklist"));
}
