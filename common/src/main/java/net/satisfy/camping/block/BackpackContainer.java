package net.satisfy.camping.block;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.block.entity.BackpackBlockEntity;
import net.satisfy.camping.item.BackpackItem;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class BackpackContainer implements Container, StackedContentsCompatible {

    private ItemStack packStack;
    private NonNullList<ItemStack> stacks = NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY);

    public BackpackContainer(ItemStack packStack) {
        this.packStack = packStack;
        AtomicInteger index = new AtomicInteger();
        BackpackItem.getContents(packStack).forEach(
                stack -> stacks.set(index.getAndIncrement(), stack)
        );
    }

    @Override
    public int getContainerSize() {
        return BackpackBlockEntity.CONTAINER_SIZE;
    }

    @Override
    public boolean isEmpty() { return false; }

    @Override
    public ItemStack getItem(int i) {
        return stacks.get(i);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        ItemStack itemStack = ContainerHelper.removeItem(this.stacks, i, j);
        if (!itemStack.isEmpty()) {
            this.setChanged();
        }

        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        ItemStack itemStack = this.stacks.get(i);
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.stacks.set(i, ItemStack.EMPTY);
            setStackNbt(i, ItemStack.EMPTY);
            return itemStack;
        }
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        this.stacks.set(i, itemStack);
        if (!itemStack.isEmpty() && itemStack.getCount() > this.getMaxStackSize()) {
            itemStack.setCount(this.getMaxStackSize());
        }

        this.setChanged();
    }

    @Override
    public void setChanged() {
        AtomicInteger counter = new AtomicInteger();
        this.stacks.forEach(stack -> setStackNbt(counter.getAndIncrement(), stack));
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
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

    private void setStackNbt(int i, ItemStack stack) {
        CompoundTag tag = packStack.getOrCreateTag();
        CompoundTag compoundTag2 = tag.getCompound("BlockEntityTag");
        ListTag listTag = compoundTag2.getList("Items", 10);
        if (listTag.isEmpty()) {
            listTag = new ListTag();
            listTag.addAll(
                    NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, new CompoundTag())
            );
        }
        listTag.set(i, stack.save(new CompoundTag()));
        compoundTag2.put("Items", listTag);
        tag.put("BlockEntityTag", compoundTag2);
        packStack.setTag(tag);
    }

    public List<ItemStack> getStacks() {
        return stacks;
    }
}
