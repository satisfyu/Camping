package net.satisfy.camping.core.world.item;

import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public class ModSpawnEggItem extends Item {
    private static final Map<Supplier<EntityType<? extends Mob>>, ModSpawnEggItem> BY_ID = Maps.newIdentityHashMap();
    private final Supplier<EntityType<? extends Mob>> defaultType;

    public ModSpawnEggItem(Supplier<EntityType<? extends Mob>> $$0, Item.Properties $$3) {
        super($$3);
        this.defaultType = $$0;
        BY_ID.put($$0, this);
    }

    public Supplier<EntityType<? extends Mob>> getDefaultType() {
        return defaultType;
    }

    public InteractionResult useOn(UseOnContext $$0) {
        Level $$1 = $$0.getLevel();
        if (!($$1 instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack $$2 = $$0.getItemInHand();
            BlockPos $$3 = $$0.getClickedPos();
            Direction $$4 = $$0.getClickedFace();
            BlockState $$5 = $$1.getBlockState($$3);
            if ($$5.is(Blocks.SPAWNER)) {
                BlockEntity $$6 = $$1.getBlockEntity($$3);
                if ($$6 instanceof SpawnerBlockEntity) {
                    SpawnerBlockEntity $$7 = (SpawnerBlockEntity)$$6;
                    EntityType<?> $$8 = this.getType($$2.getTag());
                    $$7.setEntityId($$8, $$1.getRandom());
                    $$6.setChanged();
                    $$1.sendBlockUpdated($$3, $$5, $$5, 3);
                    $$1.gameEvent($$0.getPlayer(), GameEvent.BLOCK_CHANGE, $$3);
                    $$2.shrink(1);
                    return InteractionResult.CONSUME;
                }
            }

            BlockPos $$9;
            if ($$5.getCollisionShape($$1, $$3).isEmpty()) {
                $$9 = $$3;
            } else {
                $$9 = $$3.relative($$4);
            }

            EntityType<?> $$11 = this.getType($$2.getTag());
            if ($$11.spawn((ServerLevel)$$1, $$2, $$0.getPlayer(), $$9, MobSpawnType.SPAWN_EGG, true, !Objects.equals($$3, $$9) && $$4 == Direction.UP) != null) {
                $$2.shrink(1);
                $$1.gameEvent($$0.getPlayer(), GameEvent.ENTITY_PLACE, $$3);
            }

            return InteractionResult.CONSUME;
        }
    }

    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        BlockHitResult $$4 = getPlayerPOVHitResult($$0, $$1, Fluid.SOURCE_ONLY);
        if ($$4.getType() != Type.BLOCK) {
            return InteractionResultHolder.pass($$3);
        } else if (!($$0 instanceof ServerLevel)) {
            return InteractionResultHolder.success($$3);
        } else {
            BlockPos $$6 = $$4.getBlockPos();
            if (!($$0.getBlockState($$6).getBlock() instanceof LiquidBlock)) {
                return InteractionResultHolder.pass($$3);
            } else if ($$0.mayInteract($$1, $$6) && $$1.mayUseItemAt($$6, $$4.getDirection(), $$3)) {
                EntityType<?> $$7 = this.getType($$3.getTag());
                Entity $$8 = $$7.spawn((ServerLevel)$$0, $$3, $$1, $$6, MobSpawnType.SPAWN_EGG, false, false);
                if ($$8 == null) {
                    return InteractionResultHolder.pass($$3);
                } else {
                    if (!$$1.getAbilities().instabuild) {
                        $$3.shrink(1);
                    }

                    $$1.awardStat(Stats.ITEM_USED.get(this));
                    $$0.gameEvent($$1, GameEvent.ENTITY_PLACE, $$8.position());
                    return InteractionResultHolder.consume($$3);
                }
            } else {
                return InteractionResultHolder.fail($$3);
            }
        }
    }

    public boolean spawnsEntity(@Nullable CompoundTag $$0, EntityType<?> $$1) {
        return Objects.equals(this.getType($$0), $$1);
    }

//    public int getColor(int $$0) {
//        return $$0 == 0 ? this.backgroundColor : this.highlightColor;
//    }

    @Nullable
    public static ModSpawnEggItem byId(@Nullable EntityType<?> $$0) {
        return (ModSpawnEggItem)BY_ID.get($$0);
    }

    public static Iterable<ModSpawnEggItem> eggs() {
        return Iterables.unmodifiableIterable(BY_ID.values());
    }

    public EntityType<?> getType(@Nullable CompoundTag $$0) {
        if ($$0 != null && $$0.contains("EntityTag", 10)) {
            CompoundTag $$1 = $$0.getCompound("EntityTag");
            if ($$1.contains("id", 8)) {
                return (EntityType)EntityType.byString($$1.getString("id")).orElse(this.defaultType.get());
            }
        }

        return this.defaultType.get();
    }

    public FeatureFlagSet requiredFeatures() {
        return this.defaultType.get().requiredFeatures();
    }

    public Optional<Mob> spawnOffspringFromSpawnEgg(Player $$0, Mob $$1, EntityType<? extends Mob> $$2, ServerLevel $$3, Vec3 $$4, ItemStack $$5) {
        if (!this.spawnsEntity($$5.getTag(), $$2)) {
            return Optional.empty();
        } else {
            Mob $$6;
            if ($$1 instanceof AgeableMob) {
                $$6 = ((AgeableMob)$$1).getBreedOffspring($$3, (AgeableMob)$$1);
            } else {
                $$6 = (Mob)$$2.create($$3);
            }

            if ($$6 == null) {
                return Optional.empty();
            } else {
                $$6.setBaby(true);
                if (!$$6.isBaby()) {
                    return Optional.empty();
                } else {
                    $$6.moveTo($$4.x(), $$4.y(), $$4.z(), 0.0F, 0.0F);
                    $$3.addFreshEntityWithPassengers($$6);
                    if ($$5.hasCustomHoverName()) {
                        $$6.setCustomName($$5.getHoverName());
                    }

                    if (!$$0.getAbilities().instabuild) {
                        $$5.shrink(1);
                    }

                    return Optional.of($$6);
                }
            }
        }
    }
}

