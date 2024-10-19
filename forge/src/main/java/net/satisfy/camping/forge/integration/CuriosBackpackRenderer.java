package net.satisfy.camping.forge.integration;

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
import net.satisfy.camping.client.model.SmallBackpackModel;
import net.satisfy.camping.client.model.EnderpackModel;
import net.satisfy.camping.client.model.EnderbagModel;
import net.satisfy.camping.client.model.GoodybagModel;
import net.satisfy.camping.client.model.LargeBackpackModel;
import net.satisfy.camping.client.model.SheepbagModel;
import net.satisfy.camping.client.model.WandererBackpackModel;
import net.satisfy.camping.client.model.WandererBagModel;
import net.satisfy.camping.forge.client.CampingClientForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CuriosBackpackRenderer implements ICurioRenderer {

    private static final Logger LOGGER = LogManager.getLogger();
    private final EntityModel<LivingEntity> model;
    private final ResourceLocation texture;

    public CuriosBackpackRenderer(BackpackType type) {
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
    public <T extends LivingEntity, M extends EntityModel<T>> void render(@NotNull ItemStack stack,
                                                                          @NotNull SlotContext slotContext,
                                                                          @NotNull PoseStack matrixStack,
                                                                          @NotNull RenderLayerParent<T, M> renderLayerParent,
                                                                          @NotNull MultiBufferSource renderTypeBuffer,
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
        if (model instanceof EnderpackModel) {
            matrixStack.translate(0F, -1F, 0.025F);
        } else if (model instanceof EnderbagModel) {
            matrixStack.translate(0F, -1.5F, 0.025F);
        } else if (model instanceof GoodybagModel) {
            matrixStack.translate(0.38F, -1.45F, 0.36F);
        } else if (model instanceof LargeBackpackModel) {
            matrixStack.translate(0F, -1.4F, 0.025F);
        } else if (model instanceof SheepbagModel) {
            matrixStack.translate(0.175F, -1.5F, 0.43F);
        } else if (model instanceof WandererBackpackModel) {
            matrixStack.translate(0F, -1.5F, 0F);
        } else if (model instanceof WandererBagModel) {
            matrixStack.translate(0.3F, -1.5F, 0.4F);
        } else {
            matrixStack.translate(-0.2F, -1.5F, 0.425F);
        }

        renderColoredCutoutModel(model, texture, matrixStack, renderTypeBuffer, light, livingEntity, 1.0f, 1.0f, 1.0f);

        matrixStack.popPose();
    }

    private <T extends LivingEntity, M extends EntityModel<T>> void renderColoredCutoutModel(@NotNull M model,
                                                                                             @NotNull ResourceLocation texture,
                                                                                             @NotNull PoseStack poseStack,
                                                                                             @NotNull MultiBufferSource buffer,
                                                                                             int light,
                                                                                             @NotNull T entity,
                                                                                             float red, float green, float blue) {
        model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(texture)), light, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0f);
    }

    public enum BackpackType {
        SMALL_BACKPACK, ENDERPACK, ENDERBAG, GOODYBAG, LARGE_BACKPACK, SHEEPBAG, WANDERER_BACKPACK, WANDERER_BAG
    }
}
