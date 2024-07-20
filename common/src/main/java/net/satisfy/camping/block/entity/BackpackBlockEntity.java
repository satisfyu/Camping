package net.satisfy.camping.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.camping.client.screen.BackpackScreenHandler;
import net.satisfy.camping.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BackpackBlockEntity extends BlockEntity implements MenuProvider {
    public static final int CONTAINER_SIZE = 24;
    private NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
    private Component customName;

    public BackpackBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.BACKPACK_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items);
        if (tag.contains("CustomName", 8)) {
            this.customName = Component.Serializer.fromJson(tag.getString("CustomName"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items);
        if (this.customName != null) {
            tag.putString("CustomName", Component.Serializer.toJson(this.customName));
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return this.customName != null ? this.customName : Component.translatable("container.camping.backpack");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, @NotNull Inventory inv, @NotNull Player player) {
        return new BackpackScreenHandler(syncId, inv, getContainer());
    }

    public Container getContainer() {
        return new SimpleContainer(this.items.toArray(new ItemStack[0])) {
            @Override
            public void setChanged() {
                super.setChanged();
                BackpackBlockEntity.this.setChanged();
            }
        };
    }

    public int getContainerSize() {
        return CONTAINER_SIZE;
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    public void setCustomName(Component customName) {
        this.customName = customName;
    }
}
