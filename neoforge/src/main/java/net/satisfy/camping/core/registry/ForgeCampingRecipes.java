package net.satisfy.camping.core.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.satisfy.camping.Camping;
import net.satisfy.camping.core.world.recipe.ForgeBackpackUpgradeRecipe;

import java.util.function.BiConsumer;

public class ForgeCampingRecipes {

    public static final RecipeSerializer<ForgeBackpackUpgradeRecipe> BACKPACK_UPGRADE = new ForgeBackpackUpgradeRecipe.Serializer();

    public static void register(BiConsumer<RecipeSerializer<?>, ResourceLocation> consumer) {
        consumer.accept(BACKPACK_UPGRADE, Camping.identifier("backpack_upgrade"));
    }
}
