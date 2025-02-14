package net.satisfy.camping.client.screen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.camping.core.registry.CampingScreenHandlers;
import net.satisfy.camping.core.registry.CampingTags;
import net.satisfy.camping.core.world.block.BackpackBlock;
import net.satisfy.camping.core.world.block.entity.BackpackBlockEntity;
import net.satisfy.camping.core.inventory.BackpackContainer;

public class BackpackScreenHandler extends AbstractContainerMenu {
    private final BackpackContainer container;
    private final Player player;
    private final BlockPos blockPos;

    public BackpackScreenHandler(int syncId, Inventory playerInventory, BackpackContainer container, BlockPos blockPos) {
        super(CampingScreenHandlers.BACKPACK, syncId);
        checkContainerSize(container, BackpackBlockEntity.CONTAINER_SIZE);
        this.container = container;
        this.player = playerInventory.player;
        this.blockPos = blockPos;
        container.startOpen(playerInventory.player);

        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 8; ++k) {
                this.addSlot(new Slot(container, k + j * 8, 17 + k * 18, 12 + j * 18) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return !stack.is(CampingTags.BACKPACK_BLACKLIST);
                    }
                });
            }
        }

        int playerInventoryYOffset = 1;

        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 85 + j * 18 + playerInventoryYOffset));
            }
        }

        for (int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 143 + playerInventoryYOffset));
        }
    }

    public BackpackScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new BackpackContainer(NonNullList.withSize(BackpackBlockEntity.CONTAINER_SIZE, ItemStack.EMPTY)), BlockPos.ZERO);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        if (i >= 0 && i < this.slots.size()) {
            ItemStack itemStack = ItemStack.EMPTY;
            Slot slot = this.slots.get(i);
            if (slot.hasItem()) {
                ItemStack itemStack2 = slot.getItem();
                itemStack = itemStack2.copy();
                if (i < BackpackBlockEntity.CONTAINER_SIZE) {
                    if (!this.moveItemStackTo(itemStack2, BackpackBlockEntity.CONTAINER_SIZE, this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemStack2, 0, BackpackBlockEntity.CONTAINER_SIZE, false)) {
                    return ItemStack.EMPTY;
                }

                if (itemStack2.isEmpty()) {
                    slot.setByPlayer(ItemStack.EMPTY);
                } else {
                    slot.setChanged();
                }

                if (itemStack2.getCount() == itemStack.getCount()) {
                    return ItemStack.EMPTY;
                }

                slot.onTake(player, itemStack2);
            }

            return itemStack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        if (this.container instanceof BackpackContainer) {
            this.container.setChanged();
        }
        if (!this.player.isAlive() || this.player.distanceToSqr(this.blockPos.getX() + 0.5, this.blockPos.getY() + 0.5, this.blockPos.getZ() + 0.5) > 64.0) {
            this.player.closeContainer();
        }
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
        if (this.container instanceof BackpackContainer) {
            this.container.setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        BlockState checkedState = player.level().getBlockState(this.blockPos);
        Block checkedBlock = checkedState.getBlock();

        boolean validBlock = checkedBlock instanceof BackpackBlock && (player.level().getBlockEntity(this.blockPos) != null && !player.level().getBlockEntity(this.blockPos).isRemoved());

        return this.container.stillValid(player) && (validBlock || (checkedBlock == Blocks.AIR && player.distanceToSqr(blockPos.getX(), blockPos.getY(), blockPos.getZ()) < 2.0D));
    }
}
