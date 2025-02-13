package net.satisfy.camping.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.world.item.EnderpackItem;
import net.satisfy.camping.core.registry.BackpackRegistry;

public class EnderpackArmorRenderer implements ArmorRenderer {
    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
        EnderpackItem enderpack = (EnderpackItem) stack.getItem();
        Model model = BackpackRegistry.getBodyModel(enderpack, contextModel.body);

        final boolean isEnderBag = enderpack == CampingItems.ENDERBAG;
        final boolean isEnderPack = enderpack == CampingItems.ENDERPACK;

        poseStack.pushPose();

        if (isEnderBag) poseStack.translate(-0.0625f * 5f, 0, 0.0625f * 2f);
        if (isEnderPack) poseStack.translate(-0.0625f * 5f, 0, 0.0625f * 2f);

        if (entity.isCrouching()) poseStack.translate(0, -0.0625f - (0.0625f / 8f), (0.0625f) / 10.0f);

        model.renderToBuffer(poseStack, multiBufferSource.getBuffer(model.renderType(enderpack.getEnderpackTexture())), light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        poseStack.popPose();
    }
}
