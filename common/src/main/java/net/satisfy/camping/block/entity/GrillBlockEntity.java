package net.satisfy.camping.block.entity;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.satisfy.camping.Util.CampingUtil;
import net.satisfy.camping.block.GrillBlock;
import net.satisfy.camping.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GrillBlockEntity extends BlockEntity implements Clearable {
    private final NonNullList<ItemStack> items;
    private final int[] cookingProgress;
    private final int[] cookingTime;
    private final RecipeManager.CachedCheck<Container, CampfireCookingRecipe> quickCheck;

    public GrillBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.GRILL_BLOCK_ENTITY.get(), pos, state);
        this.items = NonNullList.withSize(4, ItemStack.EMPTY);
        this.cookingProgress = new int[4];
        this.cookingTime = new int[4];
        this.quickCheck = RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);
    }

    public static void cookTick(Level level, BlockPos pos, BlockState state, GrillBlockEntity grill) {
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
                        CampingUtil.Grilling.setGrilled(result);
                        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), result);
                        grill.items.set(i, ItemStack.EMPTY);
                        level.sendBlockUpdated(pos, state, state, 3);
                        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, Context.of(state));
                    }
                }
            }
        }

        if (hasItems) {
            setChanged(level, pos, state);
        }
    }

    public static void particleTick(Level level, BlockPos pos, BlockState state, GrillBlockEntity grill) {
        RandomSource randomSource = level.random;
        int i;
        if (randomSource.nextFloat() < 0.11F) {
            for (i = 0; i < randomSource.nextInt(2) + 2; ++i) {
                makeParticles(level, pos);
            }
        }

        i = state.getValue(GrillBlock.FACING).get2DDataValue();

        for (int j = 0; j < grill.items.size(); ++j) {
            if (!grill.items.get(j).isEmpty() && randomSource.nextFloat() < 0.2F) {
                Direction direction = Direction.from2DDataValue(Math.floorMod(j + i, 4));
                float offset = 0.15625F;
                double d = pos.getX() + 0.5 - direction.getStepX() * offset + direction.getClockWise().getStepX() * offset;
                double e = pos.getY() + 1.2;
                double g = pos.getZ() + 0.5 - direction.getStepZ() * offset + direction.getClockWise().getStepZ() * offset;

                for (int k = 0; k < 4; ++k) {
                    level.addParticle(ParticleTypes.SMOKE, d, e, g, 0.0, 5.0E-4, 0.0);
                }
            }
        }
    }


    private static void makeParticles(Level level, BlockPos pos) {
        RandomSource randomSource = level.random;
        level.addAlwaysVisibleParticle(ParticleTypes.SMOKE, true, pos.getX() + 0.5 + randomSource.nextDouble() / 3.0 * (randomSource.nextBoolean() ? 1 : -1), pos.getY() + 1.3 + randomSource.nextDouble() + randomSource.nextDouble(), pos.getZ() + 0.5 + randomSource.nextDouble() / 3.0 * (randomSource.nextBoolean() ? 1 : -1), 0.0, 0.07, 0.0);
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
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

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag compoundTag = new CompoundTag();
        ContainerHelper.saveAllItems(compoundTag, this.items, true);
        return compoundTag;
    }

    public Optional<CampfireCookingRecipe> getCookableRecipe(ItemStack stack) {
        return this.items.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.quickCheck.getRecipeFor(new SimpleContainer(stack), this.level);
    }

    public boolean placeFood(@Nullable Entity entity, ItemStack stack, int cookTime) {
        for (int i = 0; i < this.items.size(); ++i) {
            ItemStack itemStack = this.items.get(i);
            if (itemStack.isEmpty()) {
                this.cookingTime[i] = cookTime;
                this.cookingProgress[i] = 0;
                this.items.set(i, stack.split(1));
                assert this.level != null;
                this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.getBlockPos(), Context.of(entity, this.getBlockState()));
                this.markUpdated();
                return true;
            }
        }
        return false;
    }

    private void markUpdated() {
        this.setChanged();
        Objects.requireNonNull(this.getLevel()).sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    public void clearContent() {
        this.items.clear();
    }
}
