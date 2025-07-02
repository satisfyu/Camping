package net.satisfy.camping.core.world.item;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class MosquitoRepellentItem extends Item {

    public static final String PLAYER_TAG = "mosquito.repellent";

    public MosquitoRepellentItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack $$0) {
        return 40;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.BOW;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        super.onUseTick(level, entity, stack, remainingUseDuration);

        float yaw = entity.getYRot();
        float radians = (float) Math.toRadians(yaw);

        double dx = -Mth.sin(radians);
        double dz = Mth.cos(radians);

        double offset = 1.0;
        double px = entity.getX() + dx * offset;
        double py = entity.getY() + entity.getEyeHeight() - 0.1; // Near eye level
        double pz = entity.getZ() + dz * offset;

        double speedX = dx * 0.05;
        double speedY = 0.0;
        double speedZ = dz * 0.05;

        level.addAlwaysVisibleParticle(
                ParticleTypes.BUBBLE_POP,
                px, py, pz,
                speedX, speedY, speedZ
        );

        if (remainingUseDuration >= 40 && !entity.getTags().contains(PLAYER_TAG)) entity.addTag(PLAYER_TAG);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (!player.isUsingItem()) player.startUsingItem(hand);

        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack $$0, Level $$1, LivingEntity $$2) {
        $$0.shrink(1);
        return $$0;
    }
}
