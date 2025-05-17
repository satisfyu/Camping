package net.satisfy.camping;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.camping.client.model.EnderbagModel;
import net.satisfy.camping.client.model.EnderpackModel;
import net.satisfy.camping.core.registry.CampingSounds;

public class Camping {

    public static void init() {
        CampingSounds.init();
    }

    public static ResourceLocation identifier(String value) {
        return new ResourceLocation(Constants.MOD_ID, value);
    }
}