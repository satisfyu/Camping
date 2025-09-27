package net.satisfy.camping.core.world.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Function;

public class RecipeWrapperSerializer<T extends Recipe<?>, R extends Recipe<?> & IWrapperRecipe<T>> implements RecipeSerializer<R> {

    private final Function<T, R> initialize;
    private final RecipeSerializer<T> base;

    public RecipeWrapperSerializer(Function<T, R> initialize, RecipeSerializer<T> base) {
        this.initialize = initialize;
        this.base = base;
    }

    @Override
    public MapCodec<R> codec() {
        return base.codec().xmap(initialize, R::getCompose);
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, R> streamCodec() {
        StreamCodec<RegistryFriendlyByteBuf, T> inner = base.streamCodec();
        return new StreamCodec<>() {
            @Override
            public R decode(RegistryFriendlyByteBuf buf) {
                return initialize.apply(inner.decode(buf));
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, R value) {
                inner.encode(buf, value.getCompose());
            }
        };
    }
}
