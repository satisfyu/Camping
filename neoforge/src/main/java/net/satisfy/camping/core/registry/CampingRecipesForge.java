package net.satisfy.camping.core.registry;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.satisfy.camping.Constants;
import net.satisfy.camping.core.crafting.BackpackUpgradeRecipe;

public class CampingRecipesForge {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Constants.MOD_ID);

    public static final RegistryObject<RecipeSerializer<BackpackUpgradeRecipe>> BACKPACK_UPGRADE = RECIPES.register("backpack_upgrade", BackpackUpgradeRecipe.Serializer::new);

    public static void register(IEventBus modEventBus) {
        RECIPES.register(modEventBus);
    }
}
