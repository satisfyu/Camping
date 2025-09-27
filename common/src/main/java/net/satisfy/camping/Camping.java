package net.satisfy.camping;

import net.minecraft.resources.ResourceLocation;

public class Camping {

    public static void init() {
    }

    public static ResourceLocation identifier(String name) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name);
    }
}