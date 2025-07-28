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
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.Camping;
import net.satisfy.camping.client.CampingClientForge;
import net.satisfy.camping.client.model.WandererBagModel;
import org.antlr.v4.runtime.misc.NotNull;

public class WandererBagLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    private final WandererBagModel<T> model;

    public WandererBagLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
        this.model = new WandererBagModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(CampingClientForge.WANDERER_BAG_LAYER));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean shouldRender = false;

        for (ItemStack stack : ((Player) entity).getInventory().armor) {
            if (stack.getItem().getDescriptionId().toLowerCase().contains("wanderer_bag")) {
                shouldRender = true;
            }
        }

        if (shouldRender) {
            this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            poseStack.pushPose();
            // poseStack.translate(0.3F, -1.5F, 0.4F);

            poseStack.translate(0.0625f * -5.0f, 0.0625f * 10.0f, 0.0625f * 4.0f);
            if (entity.isCrouching()) poseStack.translate(0, 0.0625f * -1.0f, 0.0625f * 4.5f);

            renderColoredCutoutModel(this.model, getTextureLocation(entity), poseStack, multiBufferSource, i, entity, 1.0f, 1.0f, 1.0f);
            poseStack.popPose();
        }
    }

    @Override
    protected ResourceLocation getTextureLocation(@NotNull T entity) {
        return Camping.identifier("textures/model/wanderer_bag.png");
    }
}
