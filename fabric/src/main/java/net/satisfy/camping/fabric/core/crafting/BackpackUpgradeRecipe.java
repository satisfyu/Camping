package net.satisfy.camping.fabric.core.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.satisfy.camping.fabric.core.registry.CampingRecipesFabric;
import net.satisfy.camping.core.world.item.BackpackItem;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class BackpackUpgradeRecipe extends ShapedRecipe implements IWrapperRecipe<ShapedRecipe> {
    public static final Set<Integer> REGISTERED_RECIPES = new LinkedHashSet<>();
    private final ShapedRecipe compose;

    public BackpackUpgradeRecipe(ShapedRecipe compose) {
        super(compose.getGroup(), compose.category(), new ShapedRecipePattern(compose.getWidth(), compose.getHeight(), compose.getIngredients(), Optional.empty()), compose.getResultItem(null));
        this.compose = compose;
        REGISTERED_RECIPES.add(BuiltInRegistries.RECIPE_TYPE.getId(compose.getType()));
    }

    @Override
    public ShapedRecipe getCompose() {
        return compose;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
        ItemStack upgradedBackpack = super.assemble(craftingInput, provider);
        getBackpack(craftingInput).flatMap(backpack -> Optional.ofNullable(backpack.get(DataComponents.CUSTOM_DATA))).ifPresent(tag -> upgradedBackpack.set(DataComponents.CUSTOM_DATA, tag));

//        upgradedBackpack.getCapability(CapabilityBackpackWrapper.getCapabilityInstance()).ifPresent(wrapper -> {
//            BackpackItem backpackItem = ((BackpackItem) upgradedBackpack.getItem());
//            wrapper.setSlotNumbers(backpackItem.getNumberOfSlots(), backpackItem.getNumberOfUpgradeSlots());
//        });
        BackpackWrapperLookup.get(upgradedBackpack).ifPresent(wrapper -> {
            BackpackItem backpackItem = ((BackpackItem) upgradedBackpack.getItem());
            wrapper.setSlotNumbers(backpackItem.getNumberOfSlots(), backpackItem.getNumberOfUpgradeSlots());
        });

        return upgradedBackpack;
    }

    private Optional<ItemStack> getBackpack(RecipeInput inv) {
        for (int slot = 0; slot < inv.size(); slot++) {
            ItemStack slotStack = inv.getItem(slot);
            if (slotStack.getItem() instanceof BackpackItem) {
                return Optional.of(slotStack);
            }
        }

        return Optional.empty();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CampingRecipesFabric.BACKPACK_UPGRADE;
    }

    public static class Serializer extends RecipeWrapperSerializer<ShapedRecipe, BackpackUpgradeRecipe> {
        public Serializer() {
            super(BackpackUpgradeRecipe::new, RecipeSerializer.SHAPED_RECIPE);
        }
    }
}
