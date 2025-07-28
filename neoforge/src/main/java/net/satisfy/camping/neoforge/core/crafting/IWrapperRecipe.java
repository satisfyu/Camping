package net.satisfy.camping.neoforge.core.crafting;

import net.minecraft.world.item.crafting.Recipe;

public interface IWrapperRecipe<T extends Recipe<?>> {
    T getCompose();
}
