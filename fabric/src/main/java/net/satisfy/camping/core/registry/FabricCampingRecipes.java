package net.satisfy.camping.core.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.satisfy.camping.Camping;
import net.satisfy.camping.core.world.recipe.FabricBackpackUpgradeRecipe;

public class FabricCampingRecipes {
    public static final RecipeSerializer<FabricBackpackUpgradeRecipe> BACKPACK_UPGRADE = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Camping.identifier("backpack_upgrade"), new FabricBackpackUpgradeRecipe.Serializer());

    public static void register() {}
}
