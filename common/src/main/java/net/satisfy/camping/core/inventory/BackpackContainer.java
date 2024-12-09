package net.satisfy.camping.core.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.core.block.entity.BackpackBlockEntity;
import net.satisfy.camping.core.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("all")
public class BackpackContainer implements Container, StackedContentsCompatible {
    private NonNullList<ItemStack> stacks = NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY);

    @Nullable
    private List<ContainerListener> listeners;

    private @Nullable Player player;

    public BackpackContainer(NonNullList<ItemStack> itemStacks) {
        this.stacks = itemStacks;
    }

    public BackpackContainer(NonNullList<ItemStack> itemStacks, @Nullable Player player) {
        this(itemStacks);
        this.player = player;
    }

    @Override
    public int getContainerSize() {
        return BackpackBlockEntity.CONTAINER_SIZE;
    }

    @Override
    public boolean isEmpty() {
        return this.stacks.stream().allMatch(Predicate.isEqual(ItemStack.EMPTY));
    }

    @Override
    public @NotNull ItemStack getItem(int i) {
        return stacks.get(i);
    }

    @Override
    public @NotNull ItemStack removeItem(int i, int j) {
        return ContainerHelper.removeItem(stacks, i, j);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(this.stacks, i);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        stacks.set(i, itemStack);
        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return !player.isDeadOrDying();
    }

    @Override
    public void clearContent() {
        this.stacks.clear();
        this.setChanged();
    }

    @Override
    public void fillStackedContents(StackedContents stackedContents) {
        for (ItemStack itemStack : this.stacks) {
            stackedContents.accountStack(itemStack);
        }
        this.setChanged();
    }

    @Override
    public void setChanged() {

        if (this.player == null) {

            return;
        }

        NonNullList<ItemStack> itemStacks = NonNullList.withSize(24, ItemStack.EMPTY);

        CompoundTag blockEntityTag = BlockItem.getBlockEntityData(PlatformHelper.getEquippedBackpack(this.player));

        if (blockEntityTag == null) {

            CompoundTag compoundTag = new CompoundTag();
            ContainerHelper.saveAllItems(compoundTag, NonNullList.withSize(24, ItemStack.EMPTY));
            ItemStack itemStack1 = PlatformHelper.getEquippedBackpack(this.player);
            itemStack1.addTagElement("BlockEntityTag", compoundTag);
            blockEntityTag = BlockItem.getBlockEntityData(itemStack1);
        }

        ContainerHelper.loadAllItems(blockEntityTag, itemStacks);

        List<ItemStack> itemStacks1 = this.stacks;
        List<ItemStack> itemStacks2 = itemStacks;

        if (itemStacks1.equals(itemStacks2)) {
            return;
        } else {
            CompoundTag compoundTag = new CompoundTag();

            ContainerHelper.saveAllItems(compoundTag, this.stacks);

            PlatformHelper.getEquippedBackpack(this.player).addTagElement("BlockEntityTag", compoundTag);
        }

    }

    public List<ItemStack> getStacks() {
        return stacks;
    }
}
