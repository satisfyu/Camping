package net.satisfy.camping.forge.client.renderer.player.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.client.model.SmallBackpackModel;
import net.satisfy.camping.forge.client.CampingClientForge;
import net.satisfy.camping.forge.integration.CuriosBackpack;
import net.satisfy.camping.util.CampingIdentifier;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosSmallBackpackLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    private final SmallBackpackModel<T> model;
    private static final SmallBackpackModel<LivingEntity> model1 = new SmallBackpackModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(CampingClientForge.SMALL_BACKPACK_LAYER));

    public CuriosSmallBackpackLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
        this.model = new SmallBackpackModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(CampingClientForge.SMALL_BACKPACK_LAYER));
    }

    public void renderMethod(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, @NotNull T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean shouldRender = false;

        for (ItemStack stack : ((Player) entity).getInventory().armor) {
            if (stack.getItem().getDescriptionId().toLowerCase().contains("small_backpack")) {
                shouldRender = true;
            }
        }

        if (shouldRender) {
            this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            poseStack.pushPose();
            poseStack.translate(-0.2F, -1.5F, 0.425F);
            renderColoredCutoutModel(this.model, getTextureLocation(entity), poseStack, multiBufferSource, i, entity, 1.0f, 1.0f, 1.0f);
            poseStack.popPose();
        }
    }

    public static void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, float partialTicks, LivingEntity entity, CuriosBackpack shulkerBoxAccessory, ItemStack stack) {
        boolean shouldRender = false;

//        for (ItemStack stack1 : ((Player) entity).getInventory().armor) {
//            if (stack1.getItem().getDescriptionId().toLowerCase().contains("small_backpack")) {
//                shouldRender = true;
//            }
//        }

        boolean[] booleans = new boolean[]{false};
        CuriosApi.getCuriosHelper().getCurio(stack).ifPresent(curio -> {
            if (curio instanceof CuriosBackpack backpack) {
                booleans[0] = true;
            }
        });

        if (booleans[0]) {
//            this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            poseStack.pushPose();
            poseStack.translate(-0.2F, -1.5F, 0.425F);
            renderColoredCutoutModel(model1, new CampingIdentifier("textures/model/small_backpack.png"), poseStack, multiBufferSource, i, entity, 1.0f, 1.0f, 1.0f);
            poseStack.popPose();
        }
    }

    @Override
    protected @NotNull CampingIdentifier getTextureLocation(@NotNull T entity) {
        return new CampingIdentifier("textures/model/small_backpack.png");
    }

    protected static CampingIdentifier getTexture(@NotNull LivingEntity entity) {
        return new CampingIdentifier("textures/model/small_backpack.png");
    }

    @Override
    public void render(PoseStack arg, MultiBufferSource arg2, int i, T arg3, float f, float g, float h, float j, float k, float l) {

    }
}
