package net.satisfy.camping.core.crafting;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.satisfy.camping.core.registry.CampingRecipesFabric;
import net.satisfy.camping.core.world.item.BackpackItem;
import net.satisfy.camping.mixin.ShapedRecipeAccessor;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class BackpackUpgradeRecipe extends ShapedRecipe implements IWrapperRecipe<ShapedRecipe> {
    public static final Set<ResourceLocation> REGISTERED_RECIPES = new LinkedHashSet<>();
    private final ShapedRecipe compose;

    public BackpackUpgradeRecipe(ShapedRecipe compose) {
        super(compose.getId(), compose.getGroup(), compose.category(), compose.getWidth(), compose.getHeight(), compose.getIngredients(), ((ShapedRecipeAccessor) compose).getResult());
        this.compose = compose;
        REGISTERED_RECIPES.add(compose.getId());
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
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        ItemStack upgradedBackpack = super.assemble(inv, registryAccess);
        getBackpack(inv).flatMap(backpack -> Optional.ofNullable(backpack.getTag())).ifPresent(tag -> upgradedBackpack.setTag(tag.copy()));

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

    private Optional<ItemStack> getBackpack(CraftingContainer inv) {
        for (int slot = 0; slot < inv.getContainerSize(); slot++) {
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
