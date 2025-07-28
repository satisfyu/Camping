package net.satisfy.camping.client.renderer.player.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.satisfy.camping.Camping;
import net.satisfy.camping.client.CampingClientForge;
import net.satisfy.camping.client.model.EnderpackModel;
import org.antlr.v4.runtime.misc.NotNull;

public class EnderpackLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    private final EnderpackModel<T> model;

    public EnderpackLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
        this.model = new EnderpackModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(CampingClientForge.ENDERPACK_LAYER));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof Player) {
            boolean hasEnderpack = ((Player) entity).getInventory().armor.stream()
                    .anyMatch(stack -> stack.getItem().getDescriptionId().toLowerCase().contains("enderpack"));

            if (hasEnderpack) {
                this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                poseStack.pushPose();
                // poseStack.translate(0F, -1F, 0.025F);

                poseStack.translate(-0.0625f * 5f, 0, 0.0625f * 2.0f);
                if (entity.isCrouching()) poseStack.translate(0, 0.0625f * 2.0f, -0.0625f);

                renderColoredCutoutModel(this.model, getTextureLocation(entity), poseStack, multiBufferSource, i, entity, 1.0f, 1.0f, 1.0f);
                poseStack.popPose();
            }
        }
    }

    @Override
    protected ResourceLocation getTextureLocation(@NotNull T entity) {
        return Camping.identifier("textures/model/enderpack.png");
    }
}
