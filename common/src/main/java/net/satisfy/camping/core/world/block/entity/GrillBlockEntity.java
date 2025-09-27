package net.satisfy.camping.core.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.satisfy.camping.core.registry.CampingBlockEntities;
import net.satisfy.camping.core.util.GrillingUtil;
import net.satisfy.camping.core.world.block.GrillBlock;

import java.util.Objects;
import java.util.Optional;

public class GrillBlockEntity extends BlockEntity implements Clearable {

    private static final int INVENTORY_SIZE = 4;

    private final int[] cookingTime;
    private final int[] cookingProgress;
    private final NonNullList<ItemStack> items;
    private final RecipeManager.CachedCheck<SingleRecipeInput, CampfireCookingRecipe> quickCheck;

    public GrillBlockEntity(BlockPos pos, BlockState state) {
        super(CampingBlockEntities.GRILL, pos, state);
        this.cookingTime = new int[INVENTORY_SIZE];
        this.cookingProgress = new int[INVENTORY_SIZE];
        this.items = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
        this.quickCheck = RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);
    }

    private static void makeParticles(Level level, BlockPos pos) {
        RandomSource r = level.random;
        level.addAlwaysVisibleParticle(ParticleTypes.SMOKE, true,
                pos.getX() + 0.5 + r.nextDouble() / 3.0 * (r.nextBoolean() ? 1 : -1),
                pos.getY() + 1.3 + r.nextDouble() + r.nextDouble(),
                pos.getZ() + 0.5 + r.nextDouble() / 3.0 * (r.nextBoolean() ? 1 : -1),
                0.0, 0.07, 0.0);
    }

    public static void particleTick(Level level, BlockPos pos, BlockState state, GrillBlockEntity grill) {
        RandomSource r = level.random;
        if (r.nextFloat() < 0.11F) {
            for (int i = 0; i < r.nextInt(2) + 2; ++i) makeParticles(level, pos);
        }
        int base = state.getValue(GrillBlock.FACING).get2DDataValue();
        for (int j = 0; j < grill.items.size(); ++j) {
            if (!grill.items.get(j).isEmpty() && r.nextFloat() < 0.2F) {
                Direction dir = Direction.from2DDataValue(Math.floorMod(j + base, 4));
                float off = 0.15625F;
                double x = pos.getX() + 0.5 - dir.getStepX() * off + dir.getClockWise().getStepX() * off;
                double y = pos.getY() + 1.2;
                double z = pos.getZ() + 0.5 - dir.getStepZ() * off + dir.getClockWise().getStepZ() * off;
                for (int k = 0; k < 4; ++k) level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 5.0E-4, 0.0);
            }
        }
    }

    public static void cookTick(Level level, BlockPos pos, BlockState state, GrillBlockEntity grill) {
        boolean hasItems = false;
        for (int i = 0; i < grill.items.size(); ++i) {
            ItemStack stack = grill.items.get(i);
            if (!stack.isEmpty()) {
                hasItems = true;
                grill.cookingProgress[i]++;
                if (grill.cookingProgress[i] >= grill.cookingTime[i]) {
                    SingleRecipeInput input = new SingleRecipeInput(stack);
                    ItemStack result = grill.quickCheck
                            .getRecipeFor(input, level)
                            .map(RecipeHolder::value)
                            .map(r -> r.assemble(input, level.registryAccess()))
                            .orElse(stack);
                    if (result.isItemEnabled(level.enabledFeatures())) {
                        GrillingUtil.setGrilled(result);
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

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.items.clear();
        ContainerHelper.loadAllItems(tag, this.items, provider);
        if (tag.contains("CookingTimes", 11)) {
            int[] times = tag.getIntArray("CookingTimes");
            System.arraycopy(times, 0, this.cookingProgress, 0, Math.min(this.cookingTime.length, times.length));
        }
        if (tag.contains("CookingTotalTimes", 11)) {
            int[] totals = tag.getIntArray("CookingTotalTimes");
            System.arraycopy(totals, 0, this.cookingTime, 0, Math.min(this.cookingTime.length, totals.length));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        ContainerHelper.saveAllItems(tag, this.items, provider);
        tag.putIntArray("CookingTimes", this.cookingProgress);
        tag.putIntArray("CookingTotalTimes", this.cookingTime);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
        ContainerHelper.saveAllItems(tag, this.items, provider);
        tag.putIntArray("CookingTimes", this.cookingProgress);
        tag.putIntArray("CookingTotalTimes", this.cookingTime);
        return tag;
    }

    private void markUpdated() {
        this.setChanged();
        Objects.requireNonNull(this.getLevel()).sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }

    public Optional<CampfireCookingRecipe> getCookableRecipe(ItemStack stack) {
        if (this.items.stream().anyMatch(s -> s.isEmpty())) {
            return this.quickCheck.getRecipeFor(new SingleRecipeInput(stack), this.level).map(RecipeHolder::value);
        }
        return Optional.empty();
    }

    public boolean placeFood(Entity entity, ItemStack stack, int cookTime) {
        for (int i = 0; i < this.items.size(); ++i) {
            ItemStack s = this.items.get(i);
            if (s.isEmpty()) {
                this.cookingTime[i] = cookTime;
                this.cookingProgress[i] = 0;
                this.items.set(i, stack.split(1));
                if (this.level != null) {
                    this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.getBlockPos(), GameEvent.Context.of(entity, this.getBlockState()));
                }
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
