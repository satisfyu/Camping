package net.satisfy.camping.forge.client.renderer.player.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.core.util.CampingIdentifier;
import net.satisfy.camping.client.model.WandererBackpackModel;
import net.satisfy.camping.forge.client.CampingClientForge;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class WandererBackpackLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    private final WandererBackpackModel<T> model;

    public WandererBackpackLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
        this.model = new WandererBackpackModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(CampingClientForge.WANDERER_BACKPACK_LAYER));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, @NotNull T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean shouldRender = false;

        for (ItemStack stack : ((Player) entity).getInventory().armor) {
            if (stack.getItem().getDescriptionId().toLowerCase().contains("wanderer_backpack")) {
                shouldRender = true;
            }
        }

        if (shouldRender) {
            this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            poseStack.pushPose();

            // really messes up position, I'm too lazy, will (eventually) be fixed
            // poseStack.mulPoseMatrix(new Matrix4f().setRotationXYZ(0, Mth.PI, 0));

            poseStack.translate(0, 0.0625f * 8.0f, 0.0625f * 10.5f);
            if (entity.isCrouching()) poseStack.translate(0, 0.0625f * -2.5f, 0.0625f * 1.5f);

            renderColoredCutoutModel(this.model, getTextureLocation(entity), poseStack, multiBufferSource, i, entity, 1.0f, 1.0f, 1.0f);
            poseStack.popPose();
        }
    }

    @Override
    protected @NotNull CampingIdentifier getTextureLocation(@NotNull T entity) {
        return new CampingIdentifier("textures/model/wanderer_pack.png");
    }
}
