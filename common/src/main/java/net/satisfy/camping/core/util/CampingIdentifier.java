package net.satisfy.camping.core.util;

import net.minecraft.resources.ResourceLocation;
import net.satisfy.camping.Camping;

public class CampingIdentifier extends ResourceLocation {
    public CampingIdentifier(String path) {
        super(Camping.MODID, path);
    }
}
