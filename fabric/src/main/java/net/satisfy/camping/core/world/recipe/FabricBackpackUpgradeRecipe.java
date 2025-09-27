package net.satisfy.camping.core.world.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.satisfy.camping.core.registry.FabricCampingRecipes;
import net.satisfy.camping.core.world.item.BackpackBlockItem;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class FabricBackpackUpgradeRecipe implements CraftingRecipe, IWrapperRecipe<ShapedRecipe> {

    private final ShapedRecipe compose;

    public FabricBackpackUpgradeRecipe(ShapedRecipe compose) {
        this.compose = compose;
    }

    @Override
    public ShapedRecipe getCompose() {
        return this.compose;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return FabricCampingRecipes.BACKPACK_UPGRADE;
    }

    @Override
    public @NotNull String getGroup() {
        return compose.getGroup();
    }

    @Override
    public @NotNull CraftingBookCategory category() {
        return compose.category();
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider provider) {
        return compose.getResultItem(provider);
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return compose.getIngredients();
    }

    @Override
    public boolean showNotification() {
        return compose.showNotification();
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return compose.canCraftInDimensions(w, h);
    }

    @Override
    public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
        return compose.matches(input, level);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput input, HolderLookup.@NotNull Provider provider) {
        ItemStack upgraded = compose.assemble(input, provider);
        getBackpack(input).ifPresent(backpack -> {
            CustomData data = backpack.get(DataComponents.CUSTOM_DATA);
            if (data != null) {
                CompoundTag tagCopy = data.copyTag();
                upgraded.set(DataComponents.CUSTOM_DATA, CustomData.of(tagCopy));
            }
        });
        BackpackWrapperLookup.get(upgraded).ifPresent(wrapper -> {
            BackpackBlockItem item = (BackpackBlockItem) upgraded.getItem();
            wrapper.setSlotNumbers(item.getNumberOfSlots(), item.getNumberOfUpgradeSlots());
        });
        return upgraded;
    }

    @Override
    public boolean isIncomplete() {
        return compose.isIncomplete();
    }

    private Optional<ItemStack> getBackpack(CraftingInput input) {
        int w = input.width();
        int h = input.height();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                ItemStack s = input.getItem(x + y * w);
                if (s.getItem() instanceof BackpackBlockItem) return Optional.of(s);
            }
        }
        return Optional.empty();
    }

    public static class Serializer extends RecipeWrapperSerializer<ShapedRecipe, FabricBackpackUpgradeRecipe> {
        public Serializer() {
            super(FabricBackpackUpgradeRecipe::new, RecipeSerializer.SHAPED_RECIPE);
        }
    }
}
