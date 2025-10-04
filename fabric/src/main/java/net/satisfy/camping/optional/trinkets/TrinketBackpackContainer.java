package net.satisfy.camping.optional.trinkets;

import dev.emi.trinkets.api.SlotReference;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.satisfy.camping.core.world.BackpackContainer;

public class TrinketBackpackContainer extends BackpackContainer {

    private final Player player;
    private final SlotReference slotRef;
    private final int index;
    private final ItemStack trinket;

    public static TrinketBackpackContainer of(Player player, SlotReference ref, ItemStack stack) {
        NonNullList<ItemStack> items = BackpackContainer.readFromItem(stack);
        return new TrinketBackpackContainer(items, player, ref, stack, ref.index());
    }

    public TrinketBackpackContainer(NonNullList<ItemStack> stacks, Player player, SlotReference ref, ItemStack trinket, int index) {
        super(stacks);
        this.player = player;
        this.slotRef = ref;
        this.trinket = trinket;
        this.index = index;
    }

    @Override
    public void setChanged() {
        if (player == null || slotRef == null) return;
        if (isEmpty()) {
            trinket.remove(DataComponents.CONTAINER);
        } else {
            trinket.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(getStacks()));
        }
        slotRef.inventory().setItem(index, trinket);
    }
}
