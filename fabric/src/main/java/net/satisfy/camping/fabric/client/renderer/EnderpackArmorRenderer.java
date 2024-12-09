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
import net.satisfy.camping.core.item.EnderpackItem;
import net.satisfy.camping.core.registry.BackpackRegistry;

public class EnderpackArmorRenderer implements ArmorRenderer {
    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
        EnderpackItem enderpack = (EnderpackItem) stack.getItem();

        Model model = BackpackRegistry.getBodyModel(enderpack, contextModel.body);

        model.renderToBuffer(matrices, vertexConsumers.getBuffer(model.renderType(enderpack.getEnderpackTexture())), light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
    }
}
