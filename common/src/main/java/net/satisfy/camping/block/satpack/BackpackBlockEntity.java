package net.satisfy.camping.block.satpack;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.satisfy.camping.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class BackpackBlockEntity extends RandomizableContainerBlockEntity implements MenuProvider {
    public static final int CONTAINER_SIZE = 27;
    private NonNullList<ItemStack> items;
    private final ContainerOpenersCounter openersCounter;
    private final ChestLidController chestLidController = new ChestLidController();
    public static final DirectionProperty FACING = DirectionProperty.create("facing");

    public BackpackBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityTypeRegistry.BACKPACK_BLOCK_ENTITY.get(), blockPos, blockState);
        this.items = NonNullList.withSize(27, ItemStack.EMPTY);
        this.openersCounter = new ContainerOpenersCounter() {
            protected void onOpen(Level level, BlockPos blockPos, BlockState blockState) {
                if (level != null) {
                    BackpackBlockEntity.this.playSound(blockState);
                }
            }

            protected void onClose(Level level, BlockPos blockPos, BlockState blockState) {
                if (level != null) {
                    BackpackBlockEntity.this.playSound(blockState);
                }
            }

            protected void openerCountChanged(Level level, BlockPos blockPos, BlockState blockState, int i, int j) {
                if (level != null) {
                    BackpackBlockEntity.this.signalOpenCount(level, blockPos, blockState, i, j);
                }
            }

            protected boolean isOwnContainer(Player player) {
                return player.containerMenu instanceof AbstractContainerMenu && ((AbstractContainerMenu) player.containerMenu).stillValid(player);
            }
        };
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (!this.trySaveLootTable(compoundTag)) {
            ContainerHelper.saveAllItems(compoundTag, this.items);
        }
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(compoundTag)) {
            ContainerHelper.loadAllItems(compoundTag, this.items);
        }
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.backpack");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int syncId, @NotNull Inventory playerInventory) {
        return new BackpackScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.backpack");
    }


    @Override
    protected @NotNull NonNullList<ItemStack> getItems() {
        if (this.items.size() > 46) {
            this.items = NonNullList.withSize(46, ItemStack.EMPTY);
        }
        return this.items;
    }

    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> nonNullList) {
        if (nonNullList.size() <= 46) {
            this.items = nonNullList;
        } else {
            this.items = NonNullList.withSize(46, ItemStack.EMPTY);
            for (int i = 0; i < 46; i++) {
                this.items.set(i, nonNullList.get(i));
            }
        }
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator() && this.getLevel() != null) {
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator() && this.getLevel() != null) {
            this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    void playSound(BlockState blockState) {
        if (blockState.hasProperty(FACING)) {
            Vec3i vec3i = blockState.getValue(FACING).getNormal();
            double d = (double) this.worldPosition.getX() + 0.5D + (double) vec3i.getX() / 2.0D;
            double e = (double) this.worldPosition.getY() + 0.5D + (double) vec3i.getY() / 2.0D;
            double f = (double) this.worldPosition.getZ() + 0.5D + (double) vec3i.getZ() / 2.0D;
            assert this.level != null;
            this.level.playSound(null, d, e, f, SoundEvents.BRUSH_GENERIC, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
        }
    }

    public static void lidAnimateTick(Level level, BlockPos blockPos, BlockState blockState, BackpackBlockEntity basketBlockEntity) {
        basketBlockEntity.chestLidController.tickLid();
    }

    @Override
    public boolean triggerEvent(int i, int j) {
        if (i == 1) {
            this.chestLidController.shouldBeOpen(j > 0);
            return true;
        } else {
            return super.triggerEvent(i, j);
        }
    }

    protected void signalOpenCount(Level level, BlockPos blockPos, BlockState blockState, int i, int j) {
        Block block = blockState.getBlock();
        level.blockEvent(blockPos, block, 1, j);
    }
}
