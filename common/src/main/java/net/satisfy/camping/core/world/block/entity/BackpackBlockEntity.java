package net.satisfy.camping.core.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.camping.client.screen.BackpackScreenHandler;
import net.satisfy.camping.core.inventory.BackpackContainer;
import net.satisfy.camping.core.registry.CampingBlockEntities;
import org.jetbrains.annotations.NotNull;

public class BackpackBlockEntity extends BaseContainerBlockEntity {
    public static final int CONTAINER_SIZE = 24;
    public static final String ITEMS_TAG = "Items";
    private NonNullList<ItemStack> itemStacks;

    public BackpackBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CampingBlockEntities.BACKPACK, blockPos, blockState);
        this.itemStacks = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
    }

    public int getContainerSize() {
        return this.itemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        return this.getItems().stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public @NotNull ItemStack getItem(int i) {
        return this.getItems().get(i);
    }

    @Override
    public @NotNull ItemStack removeItem(int i, int j) {
        ItemStack itemStack = ContainerHelper.removeItem(this.getItems(), i, j);
        if (!itemStack.isEmpty()) {
            this.setChanged();
        }

        return itemStack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(this.getItems(), i);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        this.getItems().set(i, itemStack);
        if (itemStack.getCount() > this.getMaxStackSize()) {
            itemStack.setCount(this.getMaxStackSize());
        }

        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        assert this.level != null;
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr(
                    (double) this.worldPosition.getX() + 0.5,
                    (double) this.worldPosition.getY() + 0.5,
                    (double) this.worldPosition.getZ() + 0.5
            ) <= 64.0;
        }
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.camping.backpack");
    }

    public void load(CompoundTag compoundTag) {
        this.loadFromTag(compoundTag);
        super.load(compoundTag);
    }

    protected void saveAdditional(CompoundTag compoundTag) {
        ContainerHelper.saveAllItems(compoundTag, this.itemStacks, false);
        super.saveAdditional(compoundTag);
    }

    public void loadFromTag(CompoundTag compoundTag) {
        this.itemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (compoundTag.contains(ITEMS_TAG, 9)) {
            ContainerHelper.loadAllItems(compoundTag, this.itemStacks);
        }
    }

    protected NonNullList<ItemStack> getItems() {
        return this.itemStacks;
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new BackpackScreenHandler(i, inventory, new BackpackContainer(this.itemStacks), this.worldPosition);
    }

    @Override
    public void clearContent() {
        this.getItems().clear();
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }
}
