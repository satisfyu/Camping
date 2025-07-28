package net.satisfy.camping.neoforge.core.crafting;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Function;

public class RecipeWrapperSerializer<T extends Recipe<?>, R extends Recipe<?> & IWrapperRecipe<T>> implements RecipeSerializer<R> {
    private final Function<T, R> initialize;
    private final RecipeSerializer<T> recipeSerializer;

    public RecipeWrapperSerializer(Function<T, R> initialize, RecipeSerializer<T> recipeSerializer) {
        this.initialize = initialize;
        this.recipeSerializer = recipeSerializer;
    }

    public MapCodec<R> fromJson() {
        return (MapCodec<R>) initialize.apply((T) recipeSerializer.codec());
    }

    public StreamCodec<RegistryFriendlyByteBuf, R> fromAndToNetwork() {
        return (StreamCodec<RegistryFriendlyByteBuf, R>) initialize.apply((T) recipeSerializer.streamCodec());
    }

    @Override
    public MapCodec<R> codec() {
        return fromJson();
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, R> streamCodec() {
        return fromAndToNetwork();
    }
}
