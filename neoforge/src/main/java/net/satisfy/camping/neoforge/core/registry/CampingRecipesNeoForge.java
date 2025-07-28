package net.satisfy.camping.neoforge.core.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.satisfy.camping.Constants;
import net.satisfy.camping.neoforge.core.crafting.BackpackUpgradeRecipe;

public class CampingRecipesNeoForge {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Constants.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BackpackUpgradeRecipe>> BACKPACK_UPGRADE = RECIPES.register("backpack_upgrade", BackpackUpgradeRecipe.Serializer::new);

    public static void register(IEventBus modEventBus) {
        RECIPES.register(modEventBus);
    }
}
