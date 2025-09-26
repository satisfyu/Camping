package net.satisfy.camping.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.satisfy.camping.core.world.item.MarshmallowOnAStickItem;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    private void injectMarshmallowUseAnim(AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equipProgress, PoseStack matrices, MultiBufferSource bufferSource, int light, CallbackInfo ci) {
        if (!player.isUsingItem()) return;
        if (!(player.getUseItem().getItem() instanceof MarshmallowOnAStickItem)) return;

        ItemInHandRenderer self = (ItemInHandRenderer) (Object) this;

        ItemStack used = player.getUseItem();
        float duration = used.getUseDuration();
        float remaining = player.getUseItemRemainingTicks();
        float progress = (duration - remaining + partialTicks) / duration;

        float mainAngle = progress * (float) Math.PI * 8f;
        float wiggleMainHand = Mth.sin(mainAngle) * 0.05f;
        float bounceMainHand = Mth.sin(mainAngle * 0.5f) * 0.025f;

        if (hand == InteractionHand.MAIN_HAND) {
            matrices.pushPose();
            matrices.translate(0.05f + wiggleMainHand, -0.05f + bounceMainHand, -0.5f);
            matrices.mulPose(new Quaternionf(new AxisAngle4f((float) Math.toRadians(180), 1.0f, 0.0f, 0.0f)));
            self.renderItem(player, stack, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, false, matrices, bufferSource, light);
            matrices.popPose();
        }

        // Add particles
        Minecraft mc = Minecraft.getInstance();
        if (hand == InteractionHand.MAIN_HAND && mc.level != null && !mc.gameRenderer.getMainCamera().isDetached()) {
            Vec3 look = player.getLookAngle();
            Vec3 right = look.cross(new Vec3(0, 1, 0)).normalize();
            Vec3 up = right.cross(look).normalize();

            double baseX = player.getX() + look.x * 0.4 + right.x * 0.15;
            double baseY = player.getY() + 1.3 + up.y * 0.1;
            double baseZ = player.getZ() + look.z * 0.4 + right.z * 0.15;

            mc.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, used), baseX, baseY, baseZ, 0, 0, 0);
        }

        ci.cancel();
    }
}
