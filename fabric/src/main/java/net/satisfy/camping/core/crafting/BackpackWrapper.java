package net.satisfy.camping.core.crafting;

import net.minecraft.world.item.ItemStack;

public class BackpackWrapper implements IBackpackWrapper {

    private final ItemStack backpack;

    public BackpackWrapper(ItemStack backpack) {
        this.backpack = backpack;
    }

    @Override
    public void setSlotNumbers(int numberOfInventorySlots, int numberOfUpgradeSlots) {

    }
}
