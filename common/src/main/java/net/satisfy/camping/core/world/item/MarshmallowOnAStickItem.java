package net.satisfy.camping.core.world.item;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.satisfy.camping.core.registry.CampingItems;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MarshmallowOnAStickItem extends Item {
    private static final String NBT_STAGE = "marshmallowStage";
    private static final String NBT_ROASTING = "marshmallowRoasting";
    private static final String NBT_TICKS = "roastTicks";
    private static final String NBT_TARGET = "roastTarget";
    private static final String[] STAGES = {"default", "warmed", "melted", "charred", "totally_burnt"};

    public MarshmallowOnAStickItem(Properties properties) {
        super(properties);
    }

    private static CompoundTag getOrCreateData(ItemStack stack) {
        CustomData d = stack.get(DataComponents.CUSTOM_DATA);
        return d == null ? new CompoundTag() : d.copyTag();
    }

    private static void saveData(ItemStack stack, CompoundTag tag) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        CompoundTag tag = getOrCreateData(stack);
        return tag.getBoolean(NBT_ROASTING) ? UseAnim.NONE : UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        CompoundTag tag = getOrCreateData(stack);
        return tag.getBoolean(NBT_ROASTING) ? 72000 : 32;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = getOrCreateData(stack);
        if (!tag.contains(NBT_STAGE)) tag.putString(NBT_STAGE, "default");
        BlockHitResult bhr = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        boolean roasting = bhr.getType() == HitResult.Type.BLOCK && level.getBlockState(bhr.getBlockPos()).getBlock() instanceof CampfireBlock;
        tag.putBoolean(NBT_ROASTING, roasting);
        if (roasting) {
            tag.putInt(NBT_TICKS, 0);
            tag.putInt(NBT_TARGET, 40 + level.random.nextInt(60));
            saveData(stack, tag);
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }
        saveData(stack, tag);
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockState state = level.getBlockState(ctx.getClickedPos());
        if (!(state.getBlock() instanceof CampfireBlock)) return InteractionResult.PASS;
        if (state.hasProperty(CampfireBlock.LIT) && !state.getValue(CampfireBlock.LIT)) return InteractionResult.PASS;
        Player player = ctx.getPlayer();
        if (player == null) return InteractionResult.PASS;
        ItemStack stack = ctx.getItemInHand();
        CompoundTag tag = getOrCreateData(stack);
        if (!tag.contains(NBT_STAGE)) tag.putString(NBT_STAGE, "default");
        tag.putBoolean(NBT_ROASTING, true);
        tag.putInt(NBT_TICKS, 0);
        tag.putInt(NBT_TARGET, 40 + level.random.nextInt(60));
        saveData(stack, tag);
        player.startUsingItem(ctx.getHand());
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int timeLeft) {
        CompoundTag tag = getOrCreateData(stack);
        tag.remove(NBT_TICKS);
        tag.remove(NBT_TARGET);
        tag.remove(NBT_ROASTING);
        saveData(stack, tag);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, Level level, LivingEntity entity) {
        CompoundTag tag = getOrCreateData(stack);
        if (tag.getBoolean(NBT_ROASTING)) {
            tag.remove(NBT_ROASTING);
            saveData(stack, tag);
            return stack;
        }
        FoodProperties props = resolveBaseFood(stack);
        if (entity instanceof Player player) {
            player.getFoodData().eat(props.nutrition(), props.saturation());
            for (FoodProperties.PossibleEffect e : props.effects()) {
                if (level.random.nextFloat() < e.probability()) {
                    entity.addEffect(new MobEffectInstance(e.effect()));
                }
            }
            String s = tag.getString(NBT_STAGE);
            switch (s) {
                case "melted" -> entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 20, 0));
                case "charred" -> entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 20 * 20, 0));
                case "totally_burnt" -> entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 45 * 20, 1));
            }
            Abilities ab = player.getAbilities();
            if (!ab.instabuild) stack.shrink(1);
        } else {
            stack.shrink(1);
        }
        return stack;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotIndex, boolean isSelected) {
        CompoundTag tag = getOrCreateData(stack);
        if (!tag.contains(NBT_STAGE)) tag.putString(NBT_STAGE, "default");
        String stage = tag.getString(NBT_STAGE);
        int modelData = switch (stage) {
            case "warmed" -> 1;
            case "melted" -> 2;
            case "charred" -> 3;
            case "totally_burnt" -> 4;
            default -> 0;
        };
        tag.putInt("CustomModelData", modelData);
        saveData(stack, tag);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        CompoundTag tag = getOrCreateData(stack);
        if (level.isClientSide) {
            if (!tag.getBoolean(NBT_ROASTING)) return;
            if (!(entity instanceof Player player)) return;
            BlockHitResult bhr = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
            if (bhr.getType() != HitResult.Type.BLOCK) return;
            BlockState bs = level.getBlockState(bhr.getBlockPos());
            if (!(bs.getBlock() instanceof CampfireBlock)) return;

            Vec3 look = player.getLookAngle();
            Vec3 right = look.cross(new Vec3(0, 1, 0)).normalize();
            Vec3 up = right.cross(look).normalize();

            double baseX = player.getX() + look.x * 0.4 + right.x * 0.15;
            double baseY = player.getY() + 1.3 + up.y * 0.1;
            double baseZ = player.getZ() + look.z * 0.4 + right.z * 0.15;

            double itemX = 0.125, itemY = 0.0, itemZ = 0.0;
            float itemChance = 0.10f;

            double dripX = 0.145, dripY = -0.05, dripZ = 0.0;
            float dripChance = 0.075f;

            double smokeX = 0.12, smokeY = 0.22, smokeZ = 0.0;
            float smokeChance = 0.12f;

            if ((remainingUseDuration & 7) == 0 && level.random.nextFloat() < itemChance) {
                double ix = baseX + right.x * itemX + up.x * itemY + look.x * itemZ;
                double iy = baseY + right.y * itemX + up.y * itemY + look.y * itemZ;
                double iz = baseZ + right.z * itemX + up.z * itemY + look.z * itemZ;
                level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(CampingItems.MARSHMALLOW)), ix, iy, iz, 0, 0, 0);
            }

            if ((remainingUseDuration & 15) == 0 && level.random.nextFloat() < dripChance) {
                double dx = baseX + right.x * dripX + up.x * dripY + look.x * dripZ;
                double dy = baseY + right.y * dripX + up.y * dripY + look.y * dripZ;
                double dz = baseZ + right.z * dripX + up.z * dripY + look.z * dripZ;
                double dvx = (level.random.nextDouble() - 0.5) * 0.02;
                double dvy = -0.06 - level.random.nextDouble() * 0.04;
                double dvz = (level.random.nextDouble() - 0.5) * 0.02;
                level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(CampingItems.MARSHMALLOW)), dx, dy, dz, dvx, dvy, dvz);
            }

            if ((remainingUseDuration & 7) == 0 && level.random.nextFloat() < smokeChance) {
                double sx = baseX + right.x * smokeX + up.x * smokeY + look.x * smokeZ;
                double sy = baseY + right.y * smokeX + up.y * smokeY + look.y * smokeZ;
                double sz = baseZ + right.z * smokeX + up.z * smokeY + look.z * smokeZ;
                level.addParticle(ParticleTypes.SMOKE, sx, sy, sz, 0, 0.02, 0);
            }
        } else {
            if (!tag.getBoolean(NBT_ROASTING)) return;
            if (!(entity instanceof ServerPlayer)) return;
            if (!entity.isUsingItem()) return;
            if (entity.getUseItem() != stack) return;
            int ticks = tag.getInt(NBT_TICKS) + 1;
            tag.putInt(NBT_TICKS, ticks);
            int target = tag.getInt(NBT_TARGET);
            if (target > 0 && ticks >= target) {
                advanceStage(stack, level, entity);
                int nextTarget = 40 + level.random.nextInt(60);
                tag.putInt(NBT_TARGET, nextTarget);
                tag.putInt(NBT_TICKS, 0);
                saveData(stack, tag);
            } else {
                saveData(stack, tag);
            }
        }
    }

    private void advanceStage(ItemStack stack, Level level, LivingEntity entity) {
        CompoundTag tag = getOrCreateData(stack);
        String s = tag.getString(NBT_STAGE);
        if (s.isEmpty()) s = "default";
        int idx = getStageIndex(s);
        if (idx < STAGES.length - 1) {
            tag.putString(NBT_STAGE, STAGES[idx + 1]);
            int nextTarget = 40 + level.random.nextInt(60);
            tag.putInt(NBT_TARGET, nextTarget);
            tag.putInt(NBT_TICKS, 0);
            saveData(stack, tag);
            spawnParticles(entity.position(), stack, level, level.random);
        }
    }

    private FoodProperties resolveBaseFood(ItemStack stack) {
        String s = getOrCreateData(stack).getString(NBT_STAGE);
        if (s.isEmpty()) s = "default";
        if (s.equals("default")) {
            FoodProperties b = new ItemStack(Items.COOKIE).get(DataComponents.FOOD);
            assert b != null;
            return new FoodProperties.Builder().nutrition(b.nutrition()).saturationModifier(b.saturation()).alwaysEdible().build();
        }
        if (s.equals("warmed")) {
            FoodProperties b = new ItemStack(Items.APPLE).get(DataComponents.FOOD);
            assert b != null;
            return new FoodProperties.Builder().nutrition(b.nutrition()).saturationModifier(b.saturation()).alwaysEdible().build();
        }
        if (s.equals("melted")) {
            FoodProperties b = new ItemStack(Items.BREAD).get(DataComponents.FOOD);
            assert b != null;
            return new FoodProperties.Builder().nutrition(b.nutrition()).saturationModifier(b.saturation()).alwaysEdible().build();
        }
        FoodProperties b = new ItemStack(Items.ROTTEN_FLESH).get(DataComponents.FOOD);
        assert b != null;
        return new FoodProperties.Builder().nutrition(b.nutrition()).saturationModifier(b.saturation()).alwaysEdible().build();
    }

    private static int getStageIndex(String stage) {
        for (int i = 0; i < STAGES.length; i++) {
            if (STAGES[i].equals(stage)) return i;
        }
        return 0;
    }

    private void spawnParticles(Vec3 pos, ItemStack stack, Level level, RandomSource random) {
        for (int i = 0; i < 10; i++) {
            Vec3 m = new Vec3((random.nextDouble() - 0.5) * 0.2, (random.nextDouble() - 0.5) * 0.2, (random.nextDouble() - 0.5) * 0.2);
            level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), pos.x, pos.y + 1.0, pos.z, m.x, m.y, m.z);
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag tooltipFlag) {
        CompoundTag tag = getOrCreateData(itemStack);
        String s = tag.getString(NBT_STAGE);
        if (s.isEmpty()) s = "default";
        tooltip.add(Component.translatable("tooltip.camping.marshmallow_stage." + s).withStyle(ChatFormatting.WHITE));
    }
}
