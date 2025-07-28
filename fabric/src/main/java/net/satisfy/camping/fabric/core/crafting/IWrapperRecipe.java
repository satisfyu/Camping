package net.satisfy.camping.fabric.core.crafting;

import net.minecraft.world.item.crafting.Recipe;

public interface IWrapperRecipe<T extends Recipe<?>> {
    T getCompose();
}
