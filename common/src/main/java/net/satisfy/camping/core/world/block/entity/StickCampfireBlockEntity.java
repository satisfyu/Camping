package net.satisfy.camping.core.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.satisfy.camping.core.registry.CampingBlockEntities;
import net.satisfy.camping.core.world.block.StickCampfireBlock;

import java.util.Objects;
import java.util.Optional;

public class StickCampfireBlockEntity extends BlockEntity implements Clearable {

    private static final int INVENTORY_SIZE = 4;

    private final int[] cookingTime;
    private final int[] cookingProgress;
    private final NonNullList<ItemStack> items;
    private final RecipeManager.CachedCheck<Container, CampfireCookingRecipe> quickCheck;

    public StickCampfireBlockEntity(BlockPos pos, BlockState state) {
        super(CampingBlockEntities.STICK_CAMPFIRE, pos, state);
        this.cookingTime = new int[INVENTORY_SIZE];
        this.cookingProgress = new int[INVENTORY_SIZE];
        this.items = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
        this.quickCheck = RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);
    }

    public void dowse() {
        if (this.level != null) {
            this.markUpdated();
        }

    }

    public static void particleTick(Level pLevel, BlockPos pPos, BlockState pState, StickCampfireBlockEntity campfire) {
        RandomSource randomsource = pLevel.random;
        if (randomsource.nextFloat() < 0.11F) {
            for(int i = 0; i < randomsource.nextInt(2) + 2; ++i) {
                StickCampfireBlock.makeParticles(pLevel, pPos, pState.getValue(StickCampfireBlock.SIGNAL_FIRE), false);
            }
        }

        int l = pState.getValue(StickCampfireBlock.FACING).get2DDataValue();

        for(int j = 0; j < campfire.items.size(); ++j) {
            if (!campfire.items.get(j).isEmpty() && randomsource.nextFloat() < 0.2F) {
                Direction direction = Direction.from2DDataValue(Math.floorMod(j + l, 4));
                float f = 0.3125F;
                double d0 = (double)pPos.getX() + 0.5D - (double)((float)direction.getStepX() * 0.3125F) + (double)((float)direction.getClockWise().getStepX() * 0.3125F);
                double d1 = (double)pPos.getY() + 0.5D;
                double d2 = (double)pPos.getZ() + 0.5D - (double)((float)direction.getStepZ() * 0.3125F) + (double)((float)direction.getClockWise().getStepZ() * 0.3125F);

                for(int k = 0; k < 4; ++k) {
                    pLevel.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 5.0E-4D, 0.0D);
                }
            }
        }

    }

    public static void cookTick(Level level, BlockPos pos, BlockState state, StickCampfireBlockEntity grill) {
        boolean hasItems = false;

        for (int i = 0; i < grill.items.size(); ++i) {
            ItemStack itemStack = grill.items.get(i);
            if (!itemStack.isEmpty()) {
                hasItems = true;
                grill.cookingProgress[i]++;
                if (grill.cookingProgress[i] >= grill.cookingTime[i]) {
                    Container container = new SimpleContainer(itemStack);
                    ItemStack result = grill.quickCheck.getRecipeFor(container, level).map((recipe) -> recipe.assemble(container, level.registryAccess())).orElse(itemStack);
                    if (result.isItemEnabled(level.enabledFeatures())) {
                        // GrillingUtil.setGrilled(result);
                        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), result);
                        grill.items.set(i, ItemStack.EMPTY);
                        level.sendBlockUpdated(pos, state, state, 3);
                        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
                    }
                }
            }
        }

        if (hasItems) {
            setChanged(level, pos, state);
            level.playSound(null, pos, SoundEvents.SMOKER_SMOKE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        this.items.clear();
        ContainerHelper.loadAllItems(tag, this.items);
        if (tag.contains("CookingTimes", 11)) {
            int[] times = tag.getIntArray("CookingTimes");
            System.arraycopy(times, 0, this.cookingProgress, 0, Math.min(this.cookingTime.length, times.length));
        }

        if (tag.contains("CookingTotalTimes", 11)) {
            int[] totalTimes = tag.getIntArray("CookingTotalTimes");
            System.arraycopy(totalTimes, 0, this.cookingTime, 0, Math.min(this.cookingTime.length, totalTimes.length));
        }
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items, true);
        tag.putIntArray("CookingTimes", this.cookingProgress);
        tag.putIntArray("CookingTotalTimes", this.cookingTime);
    }

    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag = new CompoundTag();
        ContainerHelper.saveAllItems(compoundTag, this.items, true);
        return compoundTag;
    }

    private void markUpdated() {
        this.setChanged();
        Objects.requireNonNull(this.getLevel()).sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public Optional<CampfireCookingRecipe> getCookableRecipe(ItemStack stack) {
        return this.items.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.quickCheck.getRecipeFor(new SimpleContainer(stack), this.level);
    }

    public boolean placeFood(Entity entity, ItemStack stack, int cookTime) {
        for (int i = 0; i < this.items.size(); ++i) {
            ItemStack itemStack = this.items.get(i);
            if (itemStack.isEmpty()) {
                this.cookingTime[i] = cookTime;
                this.cookingProgress[i] = 0;
                this.items.set(i, stack.split(1));
                assert this.level != null;
                this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.getBlockPos(), GameEvent.Context.of(entity, this.getBlockState()));
                this.markUpdated();
                return true;
            }
        }
        return false;
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }
}
