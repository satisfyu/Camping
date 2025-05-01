package net.satisfy.camping;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.camping.client.model.EnderbagModel;
import net.satisfy.camping.client.model.EnderpackModel;

public class Camping {

    public static void init() {}

    public static ResourceLocation identifier(String value) {
        return new ResourceLocation(Constants.MOD_ID, value);
    }
}