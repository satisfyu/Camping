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
import net.satisfy.camping.core.world.item.BackpackItem;
import net.satisfy.camping.core.registry.BackpackRegistry;

public class BackpackTrinketRenderer implements TrinketRenderer {
    @Override
    public void render(ItemStack itemStack, SlotReference slotReference, EntityModel<? extends LivingEntity> entityModel, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, LivingEntity entity, float v, float v1, float v2, float v3, float v4, float v5) {
        BackpackItem backpack = (BackpackItem) itemStack.getItem();
        Model model = BackpackRegistry.getBodyModel(backpack, ((HumanoidModel<?>) entityModel).body);

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
//                case SMALL_BACKPACK: poseStack.translate(0, 0, 0.0625f * 5.0f);
//                case WANDERER_BACKPACK: poseStack.translate(0, 0, 0);
//                case LARGE_BACKPACK: poseStack.translate(0, 0, 0);
//                case WANDERER_BAG: poseStack.translate(0, 0, 0);
//                case SHEEPBAG: poseStack.translate(0, 0, 0);
//                case GOODYBAG: poseStack.translate(0, 0, 0);
//            }
//        }

        BackpackArmorRenderer.performTranslations(poseStack, backpack.backpackType, entity.isCrouching());
        model.renderToBuffer(poseStack, multiBufferSource.getBuffer(model.renderType(backpack.getBackpackTexture())), i, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
    }
}
