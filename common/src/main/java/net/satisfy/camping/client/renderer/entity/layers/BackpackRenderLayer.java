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
import net.satisfy.camping.core.util.BackpackRegistry;
import net.satisfy.camping.core.util.BackpackVariant;
import net.satisfy.camping.core.world.item.BackpackBlockItem;

public class BackpackRenderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public BackpackRenderLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> $$0) {
        super($$0);
    }

    public static void performTranslations(PoseStack poseStack, BackpackVariant type, boolean isCrouching) {

        final float PIXEL = 0.0625f;

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
                case SHEEPBAG, GOODYBAG -> poseStack.translate(0, PIXEL * -3.0f, PIXEL * 5.0f);
            }
        }
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, AbstractClientPlayer abstractClientPlayer, float v, float v1, float v2, float v3, float v4, float v5) {

        ItemStack stack = abstractClientPlayer.getItemBySlot(EquipmentSlot.CHEST);
        if (!(stack.getItem() instanceof BackpackBlockItem backpack)) return;

        Model model = BackpackRegistry.getBodyModel(backpack, this.getParentModel().body);
        poseStack.pushPose();

        performTranslations(poseStack, backpack.variant, abstractClientPlayer.isCrouching());
        model.renderToBuffer(poseStack, multiBufferSource.getBuffer(model.renderType(backpack.getTexture())), i, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        poseStack.popPose();
    }
}
