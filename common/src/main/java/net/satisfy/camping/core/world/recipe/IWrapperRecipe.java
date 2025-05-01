package net.satisfy.camping.core.world.recipe;

import net.minecraft.world.item.crafting.Recipe;

public interface IWrapperRecipe<T extends Recipe<?>> {
    T getCompose();
}
