package net.satisfy.camping.core.world.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MarshmallowOnAStickItem extends Item {
    private static final String NBT_STAGE = "marshmallowStage";
    private static final String[] STAGES = {"warmed", "melted", "charred", "totally_burnt"};

    public MarshmallowOnAStickItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE; // Animationslogik für den Gebrauch (wir könnten auch eine benutzerdefinierte Animation hinzufügen)
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 120; // Verwendungsdauer (kann nach Bedarf angepasst werden)
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND)
            return InteractionResultHolder.fail(player.getItemInHand(hand));

        ItemStack marshmallow = player.getMainHandItem();
        CompoundTag tag = marshmallow.getOrCreateTag();

        spawnParticles(player.position(), marshmallow, level);

        if (!canChangeStage(level, marshmallow)) {
            return InteractionResultHolder.fail(marshmallow);
        }

        player.startUsingItem(hand);

        return InteractionResultHolder.consume(marshmallow);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int timeLeft) {
        if (!(user instanceof ServerPlayer player)) return;

        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains(NBT_STAGE)) {
            String currentStage = tag.getString(NBT_STAGE);
            if (currentStage.equals("totally_burnt")) {
                player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.CHARCOAL));
            } else {
                updateStage(stack, level, player);
            }
            tag.remove(NBT_STAGE);
        }
    }

    private void updateStage(ItemStack stack, Level level, LivingEntity entity) {
        CompoundTag tag = stack.getOrCreateTag();
        String currentStage = tag.getString(NBT_STAGE);

        if (currentStage.isEmpty()) {
            tag.putString(NBT_STAGE, "warmed");
            return;
        }

        int currentIndex = getStageIndex(currentStage);
        if (currentIndex == 3) return; // Already "totally_burnt", no further updates

        int nextIndex = currentIndex + 1;
        tag.putString(NBT_STAGE, STAGES[nextIndex]);

        // Partikel hinzufügen
        spawnParticles(entity.position(), stack, level);
    }

    private static int getStageIndex(String stage) {
        for (int i = 0; i < STAGES.length; i++) {
            if (STAGES[i].equals(stage)) {
                return i;
            }
        }
        return 0;
    }

    private boolean canChangeStage(Level level, ItemStack stack) {
        String currentStage = stack.getOrCreateTag().getString(NBT_STAGE);
        return !currentStage.equals("totally_burnt");
    }

    private void spawnParticles(Vec3 pos, ItemStack stack, Level level) {
        for (int i = 0; i < 10; i++) {
            Vec3 motion = new Vec3(
                    (level.random.nextDouble() - 0.5) * 0.2,
                    (level.random.nextDouble() - 0.5) * 0.2,
                    (level.random.nextDouble() - 0.5) * 0.2
            );
            level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack),
                    pos.x, pos.y + 1.0, pos.z, motion.x, motion.y, motion.z);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        String stage = stack.getOrCreateTag().getString(NBT_STAGE);

        tooltip.add(Component.translatable("tooltip.camping.marshmallow_stage." + stage).withStyle(ChatFormatting.WHITE));

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
