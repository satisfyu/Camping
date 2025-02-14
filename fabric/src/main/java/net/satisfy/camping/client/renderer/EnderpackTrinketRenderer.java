package net.satisfy.camping.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.world.item.EnderpackItem;
import net.satisfy.camping.core.registry.BackpackRegistry;

public class EnderpackTrinketRenderer implements TrinketRenderer {
    @Override
    public void render(ItemStack itemStack, SlotReference slotReference, EntityModel<? extends LivingEntity> entityModel, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, LivingEntity entity, float v, float v1, float v2, float v3, float v4, float v5) {
        EnderpackItem enderpack = (EnderpackItem) itemStack.getItem();
        Model model = BackpackRegistry.getBodyModel(enderpack, ((HumanoidModel<?>) entityModel).body);

        final boolean isEnderBag = enderpack == CampingItems.ENDERBAG;
        final boolean isEnderPack = enderpack == CampingItems.ENDERPACK;

        poseStack.pushPose();

        if (isEnderBag) poseStack.translate(-0.0625f * 5f, 0, 0.0625f * 2f);
        if (isEnderPack) poseStack.translate(-0.0625f * 5f, 0, 0.0625f * 2f);

        if (entity.isCrouching()) poseStack.translate(0, -0.0625f - (0.0625f / 8f), (0.0625f) / 10.0f);
        model.renderToBuffer(poseStack, multiBufferSource.getBuffer(model.renderType(enderpack.getEnderpackTexture())), i, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        poseStack.popPose();
    }
}
