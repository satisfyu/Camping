package net.satisfy.camping.fabric.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.core.block.BackpackType;
import net.satisfy.camping.core.item.BackpackItem;
import net.satisfy.camping.core.registry.BackpackRegistry;

public class BackpackArmorRenderer implements ArmorRenderer {

    public static void performTranslations(PoseStack poseStack, BackpackType type, boolean isCrouching) {

        final float PIXEL = 0.0625f; // equal to 1.0f divided by 16.0f, or 1/16th of a block

        switch (type) {
            case SMALL_BACKPACK -> poseStack.translate(PIXEL * 3.0f, PIXEL * 10.0f, PIXEL * 3.0f);
            case LARGE_BACKPACK -> poseStack.translate(0, PIXEL * 12.0f, PIXEL * 10.0f);
            case WANDERER_BACKPACK -> poseStack.translate(0, PIXEL * 8.0f, PIXEL * 10.5f);
            case WANDERER_BAG -> poseStack.translate(PIXEL * -5.0f, PIXEL * 10.0f, PIXEL * 4.0f);
            case SHEEPBAG -> poseStack.translate(PIXEL * -3.0f, PIXEL * 12.0f, PIXEL * 3.0f);
            case GOODYBAG -> poseStack.translate(PIXEL * -6.0f, PIXEL * 10.0f, PIXEL * 4.0f);
        }

        if (isCrouching) {
            switch (type) {
                case SMALL_BACKPACK -> poseStack.translate(0, PIXEL * -2.5f, PIXEL * 5.0f);
                case LARGE_BACKPACK -> poseStack.translate(0, PIXEL * -6.5f, PIXEL * 5.5f);
                case WANDERER_BACKPACK -> poseStack.translate(0, PIXEL * -5.5f, PIXEL * 1.5f);
                case WANDERER_BAG -> poseStack.translate(0, PIXEL * -3.0f, PIXEL * 4.5f);
                case SHEEPBAG -> poseStack.translate(0, PIXEL * -3.0f, PIXEL * 5.0f);
                case GOODYBAG -> poseStack.translate(0, PIXEL * -3.0f, PIXEL * 5.0f);
            }
        }
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
        BackpackItem backpack = (BackpackItem) stack.getItem();

        Model model = BackpackRegistry.getBodyModel(backpack, contextModel.body);
        poseStack.pushPose();

//        switch (backpack.backpackType) {
//            case SMALL_BACKPACK: poseStack.translate(0.0625f * 3.0f, 0.0625f * 10.0f, 0.0625f * 3.0f);
//            case WANDERER_BACKPACK: poseStack.translate(0, 0, 0);
//            case LARGE_BACKPACK: poseStack.translate(0, 0, 0);
//            case WANDERER_BAG: poseStack.translate(0, 0, 0);
//            case SHEEPBAG: poseStack.translate(0, 0, 0);
//            case GOODYBAG: poseStack.translate(0, 0, 0);
//        }
//
//        if (entity.isCrouching()) {
//            switch (backpack.backpackType) {
//                case SMALL_BACKPACK: poseStack.translate(0, 0.0625f * -2.5f, 0.0625f * 5.0f);
//                case WANDERER_BACKPACK: poseStack.translate(0, 0, 0);
//                case LARGE_BACKPACK: poseStack.translate(0, 0, 0);
//                case WANDERER_BAG: poseStack.translate(0, 0, 0);
//                case SHEEPBAG: poseStack.translate(0, 0, 0);
//                case GOODYBAG: poseStack.translate(0, 0, 0);
//            }
//        }

        BackpackArmorRenderer.performTranslations(poseStack, backpack.backpackType, entity.isCrouching());
        model.renderToBuffer(poseStack, vertexConsumers.getBuffer(model.renderType(backpack.getBackpackTexture())), light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);

        poseStack.popPose();
    }
}
