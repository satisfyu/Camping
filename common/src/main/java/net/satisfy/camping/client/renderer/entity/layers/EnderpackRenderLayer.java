package net.satisfy.camping.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.core.registry.CampingItems;
import net.satisfy.camping.core.util.BackpackRegistry;
import net.satisfy.camping.core.world.item.EnderpackBlockItem;

public class EnderpackRenderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public EnderpackRenderLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> $$0) {
        super($$0);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, AbstractClientPlayer abstractClientPlayer, float v, float v1, float v2, float v3, float v4, float v5) {

        ItemStack stack = abstractClientPlayer.getItemBySlot(EquipmentSlot.CHEST);
        if (!(stack.getItem() instanceof EnderpackBlockItem enderpack)) return;

        // Model model = BackpackRegistry.getBodyModel(enderpack, contextModel.body);
        Model model = BackpackRegistry.getBodyModel(enderpack, this.getParentModel().body);

        final boolean isEnderBag = enderpack == CampingItems.ENDERBAG;
        final boolean isEnderPack = enderpack == CampingItems.ENDERPACK;

        poseStack.pushPose();

        if (isEnderBag) poseStack.translate(-0.0625f * 5f, 0, 0.0625f * 2f);
        if (isEnderPack) poseStack.translate(-0.0625f * 5f, 0, 0.0625f * 2f);

        if (abstractClientPlayer.isCrouching()) poseStack.translate(0, -0.0625f - (0.0625f / 8f), (0.0625f) / 10.0f);

        model.renderToBuffer(poseStack, multiBufferSource.getBuffer(model.renderType(enderpack.getTexture())), i, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        poseStack.popPose();
    }
}
