package net.satisfy.camping.integration;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.client.CampingClientForge;
import net.satisfy.camping.client.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CuriosBackpackRenderer implements ICurioRenderer {

    private static final Logger LOGGER = LogManager.getLogger();
    private final EntityModel<LivingEntity> model;
    private final ResourceLocation texture;

    private final BackpackType type;

    public CuriosBackpackRenderer(BackpackType type) {

        this.type = type;

        switch (type) {
            case ENDERPACK -> {
                this.model = new EnderpackModel<>(
                        Minecraft.getInstance().getEntityModels().bakeLayer(CampingClientForge.ENDERPACK_LAYER)
                );
                this.texture = new ResourceLocation("camping", "textures/model/enderpack.png");
            }
            case ENDERBAG -> {
                this.model = new EnderbagModel<>(
                        Minecraft.getInstance().getEntityModels().bakeLayer(CampingClientForge.ENDERBAG_LAYER)
                );
                this.texture = new ResourceLocation("camping", "textures/model/enderbag.png");
            }
            case GOODYBAG -> {
                this.model = new GoodybagModel<>(
                        Minecraft.getInstance().getEntityModels().bakeLayer(CampingClientForge.GOODYBAG_LAYER)
                );
                this.texture = new ResourceLocation("camping", "textures/model/goodybag.png");
            }
            case LARGE_BACKPACK -> {
                this.model = new LargeBackpackModel<>(
                        Minecraft.getInstance().getEntityModels().bakeLayer(CampingClientForge.LARGE_BACKPACK_LAYER)
                );
                this.texture = new ResourceLocation("camping", "textures/model/large_backpack.png");
            }
            case SHEEPBAG -> {
                this.model = new SheepbagModel<>(
                        Minecraft.getInstance().getEntityModels().bakeLayer(CampingClientForge.SHEEPBAG_LAYER)
                );
                this.texture = new ResourceLocation("camping", "textures/model/sheepbag.png");
            }
            case WANDERER_BACKPACK -> {
                this.model = new WandererBackpackModel<>(
                        Minecraft.getInstance().getEntityModels().bakeLayer(CampingClientForge.WANDERER_BACKPACK_LAYER)
                );
                this.texture = new ResourceLocation("camping", "textures/model/wanderer_pack.png");
            }
            case WANDERER_BAG -> {
                this.model = new WandererBagModel<>(
                        Minecraft.getInstance().getEntityModels().bakeLayer(CampingClientForge.WANDERER_BAG_LAYER)
                );
                this.texture = new ResourceLocation("camping", "textures/model/wanderer_bag.png");
            }
            default -> {
                this.model = new SmallBackpackModel<>(
                        Minecraft.getInstance().getEntityModels().bakeLayer(CampingClientForge.SMALL_BACKPACK_LAYER)
                );
                this.texture = new ResourceLocation("camping", "textures/model/small_backpack.png");
            }
        }
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render( ItemStack stack,
                                                                           SlotContext slotContext,
                                                                           PoseStack matrixStack,
                                                                           RenderLayerParent<T, M> renderLayerParent,
                                                                           MultiBufferSource renderTypeBuffer,
                                                                          int light, float limbSwing,
                                                                          float limbSwingAmount,
                                                                          float partialTicks,
                                                                          float ageInTicks,
                                                                          float netHeadYaw,
                                                                          float headPitch) {

        LivingEntity livingEntity = slotContext.entity();

        if (livingEntity == null) {
            LOGGER.warn("LivingEntity is null in CuriosBackpackRenderer");
            return;
        }

        model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        matrixStack.pushPose();
//        if (model instanceof EnderpackModel) {
//            matrixStack.translate(0F, -1F, 0.025F);
//        }
//        else if (model instanceof EnderbagModel) {
//            matrixStack.translate(0F, -1.5F, 0.025F);
//        }
//        else if (model instanceof GoodybagModel) {
//            matrixStack.translate(0.38F, -1.45F, 0.36F);
//        }
//        else if (model instanceof LargeBackpackModel) {
//            matrixStack.translate(0F, -1.4F, 0.025F);
//        }
//        else if (model instanceof SheepbagModel) {
//            matrixStack.translate(0.175F, -1.5F, 0.43F);
//        }
//        else if (model instanceof WandererBackpackModel) {
//            matrixStack.translate(0F, -1.5F, 0F);
//        }
//        else if (model instanceof WandererBagModel) {
//            matrixStack.translate(0.3F, -1.5F, 0.4F);
//        }
//        else {
//            matrixStack.translate(-0.2F, -1.5F, 0.425F);
//        }

        renderColoredCutoutModel(model, texture, matrixStack, renderTypeBuffer, light, livingEntity, 1.0f, 1.0f, 1.0f);

        matrixStack.popPose();
    }

    public static void performTranslations(PoseStack poseStack,  BackpackType type, boolean isCrouching) {

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
                case SMALL_BACKPACK -> poseStack.translate(0, PIXEL * 1.0f, PIXEL * 5.0f);
                case LARGE_BACKPACK -> poseStack.translate(0, PIXEL * -3.5f, PIXEL * 3.5f);
                case WANDERER_BACKPACK -> poseStack.translate(0, PIXEL * -2.5f, PIXEL * 1.0f);
                case WANDERER_BAG -> poseStack.translate(0, 0, PIXEL * 4.5f);
                case SHEEPBAG -> poseStack.translate(0, PIXEL * 1.0f, PIXEL * 5.0f);
                case GOODYBAG -> poseStack.translate(0, PIXEL * 1, PIXEL * 4.0f);
            }
        }
    }

    private <T extends LivingEntity, M extends EntityModel<T>> void renderColoredCutoutModel( M model,
                                                                                              ResourceLocation texture,
                                                                                              PoseStack poseStack,
                                                                                              MultiBufferSource buffer,
                                                                                             int light,
                                                                                              T entity,
                                                                                             float red, float green, float blue) {

        poseStack.pushPose();

//        poseStack.translate(0, 1.0f, 0);
//
//        poseStack.translate(-0.0625f * 5f, 0, 0.0625f * 2f);
//        if (entity.isCrouching()) {
//            poseStack.mulPoseMatrix(new Matrix4f().setRotationXYZ(30.0f * (Mth.PI/180.0f), 0, 0));
//            poseStack.translate(0, 0.0625f * 2, (-0.0625f));
//        }

         BackpackType FIXED_TYPE = switch (this.type) {
            case SMALL_BACKPACK ->  BackpackType.SMALL_BACKPACK;
            case GOODYBAG ->  BackpackType.GOODYBAG;
            case LARGE_BACKPACK ->  BackpackType.LARGE_BACKPACK;
            case SHEEPBAG ->  BackpackType.SHEEPBAG;
            case WANDERER_BACKPACK ->  BackpackType.WANDERER_BACKPACK;
            case WANDERER_BAG ->  BackpackType.WANDERER_BAG;
            default -> null;
        };

        if (FIXED_TYPE != null) performTranslations(poseStack, FIXED_TYPE, entity.isCrouching());
        else {
            if (this.model instanceof EnderbagModel<LivingEntity>) poseStack.translate(-0.0625f * 5f, 0, 0.0625f * 2f);
            if (this.model instanceof EnderpackModel<LivingEntity>) poseStack.translate(-0.0625f * 5f, 0, 0.0625f * 2f);

            if (entity.isCrouching()) poseStack.translate(0, 0.0625f * 3.0f, 0.0625f * -1.0f);
        }

        model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(texture)), light, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0f);
        poseStack.popPose();
    }

    public enum BackpackType {
        SMALL_BACKPACK, ENDERPACK, ENDERBAG, GOODYBAG, LARGE_BACKPACK, SHEEPBAG, WANDERER_BACKPACK, WANDERER_BAG
    }
}
