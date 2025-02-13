package net.satisfy.camping;

import net.minecraft.resources.ResourceLocation;

public class Camping {

    public static void init() {}

    public static ResourceLocation identifier(String path) {
        return new ResourceLocation(Constants.MOD_ID, path);
    }
}