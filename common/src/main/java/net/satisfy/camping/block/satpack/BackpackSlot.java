package net.satisfy.camping.block.satpack;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.registry.TagRegistry;

public class BackpackSlot extends Slot {
    public BackpackSlot(Container container, int index, int x, int y) {
        super(container, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return !stack.is(TagRegistry.BACKPACK_BLACKLIST);
    }

}
