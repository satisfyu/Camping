package net.satisfy.camping.core.world.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.satisfy.camping.CampingNeoForge;
import net.satisfy.camping.core.registry.NeoForgeCampingRecipes;
import net.satisfy.camping.core.world.item.BackpackBlockItem;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class ForgeBackpackUpgradeRecipe implements CraftingRecipe, IWrapperRecipe<ShapedRecipe> {
    private final ShapedRecipe compose;

    public ForgeBackpackUpgradeRecipe(ShapedRecipe compose) {
        this.compose = compose;
    }

    @Override
    public ShapedRecipe getCompose() {
        return compose;
    }

    @Override
    public @NotNull String getGroup() {
        return compose.getGroup();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return NeoForgeCampingRecipes.BACKPACK_UPGRADE;
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
    public boolean matches(CraftingInput input, Level level) {
        return compose.matches(input, level);
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return compose.getResultItem(provider);
    }

    @Override
    public CraftingBookCategory category() {
        return compose.category();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return compose.getIngredients();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    private Optional<ItemStack> getBackpack(CraftingInput input) {
        for (int i = 0; i < input.size(); i++) {
            ItemStack s = input.getItem(i);
            if (s.getItem() instanceof BackpackBlockItem) return Optional.of(s);
        }
        return Optional.empty();
    }

    @Override
    public @NotNull ItemStack assemble(CraftingInput input, HolderLookup.Provider provider) {
        ItemStack out = compose.assemble(input, provider);
        getBackpack(input).ifPresent(src -> {
            CustomData cd = src.get(DataComponents.CUSTOM_DATA);
            if (cd != null) out.set(DataComponents.CUSTOM_DATA, CustomData.of(cd.copyTag()));
        });
        if (out.getItem() instanceof BackpackBlockItem b) {
            CustomData cd = out.get(DataComponents.CUSTOM_DATA);
            CompoundTag tag = cd != null ? cd.copyTag() : new CompoundTag();
            tag.putInt("slots", b.getNumberOfSlots());
            tag.putInt("upgradeSlots", b.getNumberOfUpgradeSlots());
            out.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
        return out;
    }

    public static class Serializer extends RecipeWrapperSerializer<ShapedRecipe, ForgeBackpackUpgradeRecipe> {
        public Serializer() {
            super(ForgeBackpackUpgradeRecipe::new, RecipeSerializer.SHAPED_RECIPE);
        }
    }
}
