package net.satisfy.camping.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.satisfy.camping.Util.CampingIdentifier;

public class TagRegistry {
    public static final TagKey<Item> BACKPACK_BLACKLIST = TagKey.create(Registries.ITEM, new CampingIdentifier("backpack_blacklist"));
}
