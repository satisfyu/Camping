package net.satisfy.camping.fabric.core.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.satisfy.camping.Camping;
import net.satisfy.camping.fabric.core.crafting.BackpackUpgradeRecipe;

import java.util.function.Supplier;

public class CampingRecipesFabric {

    public static final RecipeSerializer<BackpackUpgradeRecipe> BACKPACK_UPGRADE = registerRecipeSerializer("backpack_upgrade", BackpackUpgradeRecipe.Serializer::new);

    public static <T extends RecipeSerializer<?>> T registerRecipeSerializer(String recipeName, Supplier<T> supplier) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Camping.identifier(recipeName), supplier.get());
    }

    public static void register() {}
}
