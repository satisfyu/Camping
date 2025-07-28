package net.satisfy.camping.core.crafting;

import net.minecraft.world.item.crafting.Recipe;

public interface IWrapperRecipe<T extends Recipe<?>> {
    T getCompose();
}
