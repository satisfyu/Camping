package net.satisfy.camping.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.camping.block.BackpackBlock;
import net.satisfy.camping.inventory.BackpackContainer;
import net.satisfy.camping.client.screen.BackpackScreenHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

public class BackpackBlockEntity extends BaseContainerBlockEntity {
    public static final int COLUMNS = 8;
    public static final int ROWS = 3;
    public static final int CONTAINER_SIZE = 24;
    public static final String ITEMS_TAG = "Items";
    private static final int[] SLOTS = IntStream.range(0, CONTAINER_SIZE).toArray();
    private NonNullList<ItemStack> itemStacks;

    public BackpackBlockEntity(@Nullable DyeColor dyeColor, BlockPos blockPos, BlockState blockState) {
        super(BlockEntityType.SHULKER_BOX, blockPos, blockState);
        this.itemStacks = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
    }

    public BackpackBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityType.SHULKER_BOX, blockPos, blockState);
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
        return (ItemStack)this.getItems().get(i);
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
        return false;
    }

    private static void doNeighborUpdates(Level level, BlockPos blockPos, BlockState blockState) {
        blockState.updateNeighbourShapes(level, blockPos, Block.UPDATE_ALL);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.camping.backpack");
    }

    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.loadFromTag(compoundTag);
    }

    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, this.itemStacks, false);
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

    protected void setItems(NonNullList<ItemStack> nonNullList) {
        this.itemStacks = nonNullList;
    }

    public int[] getSlotsForFace(Direction direction) {
        return SLOTS;
    }

    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return !(Block.byItem(itemStack.getItem()) instanceof BackpackBlock);
    }

    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return true;
    }


    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new BackpackScreenHandler(i, inventory, new BackpackContainer(this.itemStacks));
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
