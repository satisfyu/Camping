package net.satisfy.camping.optional;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.camping.Camping;
import net.satisfy.camping.core.registry.CampingBlocks;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class NeoForgeJEICampingPlugin implements IModPlugin {

    private static final ResourceLocation ID = Camping.identifier("jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(CampingBlocks.GRILL, RecipeTypes.CAMPFIRE_COOKING);
    }
}
