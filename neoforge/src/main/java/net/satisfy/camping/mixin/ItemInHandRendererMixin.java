package net.satisfy.camping.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.camping.core.world.item.MarshmallowOnAStickItem;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

    @Shadow
    protected abstract void applyItemArmTransform(PoseStack poseStack, HumanoidArm arm, float equipProgress);

    @Shadow
    protected abstract void applyItemArmAttackTransform(PoseStack poseStack, HumanoidArm arm, float swingProgress);

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    private void injectMarshmallowUseAnim(AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equipProgress, PoseStack matrices, MultiBufferSource bufferSource, int light, CallbackInfo ci) {
        if (!player.isUsingItem()) return;
        ItemStack used = player.getUseItem();
        if (!(used.getItem() instanceof MarshmallowOnAStickItem)) return;
        if (!used.getOrCreateTag().getBoolean("marshmallowRoasting")) return;

        Minecraft mc = Minecraft.getInstance();
        if (!(mc.hitResult instanceof BlockHitResult bhr)) return;
        BlockState state = player.level().getBlockState(bhr.getBlockPos());
        if (!(state.getBlock() instanceof CampfireBlock)) return;
        if (state.hasProperty(CampfireBlock.LIT) && !state.getValue(CampfireBlock.LIT)) return;

        ItemInHandRenderer self = (ItemInHandRenderer) (Object) this;

        float usedTicks = used.getUseDuration() - player.getUseItemRemainingTicks() + partialTicks;
        float main = usedTicks * (float) (Math.PI / 12.0);
        float wiggle = Mth.sin(main) * 0.06f;
        float bounce = Mth.sin(main * 0.5f) * 0.03f;

        HumanoidArm arm = hand == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();

        matrices.pushPose();
        applyItemArmTransform(matrices, arm, 1.0f);
        applyItemArmAttackTransform(matrices, arm, swingProgress);
        matrices.translate(0.06f + wiggle, 0.3f + bounce, -0.32f);
        matrices.mulPose(new Quaternionf(new AxisAngle4f((float) Math.toRadians(-10), 1.0f, 0.0f, 0.0f)));
        matrices.mulPose(new Quaternionf(new AxisAngle4f((float) Math.toRadians(12), 0.0f, 1.0f, 0.0f)));
        matrices.scale(1.4f, 1.4f, 1.4f);
        self.renderItem(player, stack, hand == InteractionHand.MAIN_HAND ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, hand == InteractionHand.OFF_HAND, matrices, bufferSource, light);
        matrices.popPose();

        ci.cancel();
    }
}
