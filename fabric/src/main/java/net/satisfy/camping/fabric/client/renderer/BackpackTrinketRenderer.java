package net.satisfy.camping.fabric.client.renderer;

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
import net.satisfy.camping.item.BackpackItem;
import net.satisfy.camping.registry.BackpackRegistry;

public class BackpackTrinketRenderer implements TrinketRenderer {
    @Override
    public void render(ItemStack itemStack, SlotReference slotReference, EntityModel<? extends LivingEntity> entityModel, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, LivingEntity livingEntity, float v, float v1, float v2, float v3, float v4, float v5) {
        BackpackItem backpack = (BackpackItem) itemStack.getItem();
        Model model = BackpackRegistry.getBodyModel(backpack, ((HumanoidModel<?>) entityModel).body);
        model.renderToBuffer(poseStack, multiBufferSource.getBuffer(model.renderType(backpack.getBackpackTexture())), i, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
    }
}
