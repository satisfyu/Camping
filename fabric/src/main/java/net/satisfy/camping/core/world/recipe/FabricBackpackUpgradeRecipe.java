package net.satisfy.camping.core.world.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.satisfy.camping.core.registry.FabricCampingRecipes;
import net.satisfy.camping.core.world.item.BackpackBlockItem;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class FabricBackpackUpgradeRecipe extends ShapedRecipe implements IWrapperRecipe<ShapedRecipe> {

    public static final Set<ResourceLocation> REGISTERED_RECIPES = new LinkedHashSet<>();
    private final ShapedRecipe compose;

    public FabricBackpackUpgradeRecipe(ShapedRecipe compose) {
        super(compose.getId(), compose.getGroup(), compose.category(), compose.getWidth(), compose.getHeight(), compose.getIngredients(), compose.result);
        this.compose = compose;
        REGISTERED_RECIPES.add(compose.getId());
    }

    @Override
    public ShapedRecipe getCompose() {
        return this.compose;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return FabricCampingRecipes.BACKPACK_UPGRADE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    private Optional<ItemStack> getBackpack(CraftingContainer inv) {
        for (int slot = 0; slot < inv.getContainerSize(); slot++) {
            ItemStack slotStack = inv.getItem(slot);
            if (slotStack.getItem() instanceof BackpackBlockItem) {
                return Optional.of(slotStack);
            }
        }

        return Optional.empty();
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        ItemStack upgradedBackpack = super.assemble(inv, registryAccess);
        getBackpack(inv).flatMap(backpack -> Optional.ofNullable(backpack.getTag())).ifPresent(tag -> upgradedBackpack.setTag(tag.copy()));

//        upgradedBackpack.getCapability(CampingForge.BACKPACK_WRAPPER_CAPABILITY).ifPresent(wrapper -> {
//            BackpackBlockItem backpackItem = ((BackpackBlockItem) upgradedBackpack.getItem());
//            wrapper.setSlotNumbers(backpackItem.getNumberOfSlots(), backpackItem.getNumberOfUpgradeSlots());
//        });

        BackpackWrapperLookup.get(upgradedBackpack).ifPresent(wrapper -> {
            BackpackBlockItem backpackItem = ((BackpackBlockItem) upgradedBackpack.getItem());
            wrapper.setSlotNumbers(backpackItem.getNumberOfSlots(), backpackItem.getNumberOfUpgradeSlots());
        });

        return upgradedBackpack;
    }

    public static class Serializer extends RecipeWrapperSerializer<ShapedRecipe, FabricBackpackUpgradeRecipe> {
        public Serializer() {
            super(FabricBackpackUpgradeRecipe::new, RecipeSerializer.SHAPED_RECIPE);
        }
    }
}
