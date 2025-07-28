package net.satisfy.camping.core.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.satisfy.camping.core.world.block.entity.BackpackBlockEntity;
import net.satisfy.camping.platform.PlatformHelper;

import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("all")
public class BackpackContainer implements Container, StackedContentsCompatible {
    private NonNullList<ItemStack> stacks = NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY);

    
    private List<ContainerListener> listeners;

    private  Player player;

    public BackpackContainer(NonNullList<ItemStack> itemStacks) {
        this.stacks = itemStacks;
    }

    public BackpackContainer(NonNullList<ItemStack> itemStacks,  Player player) {
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
    public ItemStack getItem(int i) {
        return stacks.get(i);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        return ContainerHelper.removeItem(stacks, i, j);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
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

        CustomData blockEntityTag = PlatformHelper.getEquippedBackpack(this.player).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);

        if (blockEntityTag.copyTag() == null) {

            CompoundTag compoundTag = new CompoundTag();
            ContainerHelper.saveAllItems(compoundTag, NonNullList.withSize(24, ItemStack.EMPTY), player.level().registryAccess());
            ItemStack itemStack1 = PlatformHelper.getEquippedBackpack(this.player);
            itemStack1.set(DataComponents.CUSTOM_DATA, blockEntityTag);
            blockEntityTag = itemStack1.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        }

        ContainerHelper.loadAllItems(blockEntityTag.copyTag(), itemStacks, player.level().registryAccess());

        List<ItemStack> itemStacks1 = this.stacks;
        List<ItemStack> itemStacks2 = itemStacks;

        if (itemStacks1.equals(itemStacks2)) {
            return;
        } else {
            CompoundTag compoundTag = new CompoundTag();

            ContainerHelper.saveAllItems(compoundTag, this.stacks, player.level().registryAccess());

            PlatformHelper.getEquippedBackpack(this.player).set(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
        }

    }

    public List<ItemStack> getStacks() {
        return stacks;
    }
}
