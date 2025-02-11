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
import net.satisfy.camping.core.util.CampingIdentifier;
import net.satisfy.camping.client.model.SmallBackpackModel;
import net.satisfy.camping.forge.client.CampingClientForge;
import org.jetbrains.annotations.NotNull;

public class SmallBackpackLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    private final SmallBackpackModel<T> model;

    public SmallBackpackLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
        this.model = new SmallBackpackModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(CampingClientForge.SMALL_BACKPACK_LAYER));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, @NotNull T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean shouldRender = false;

        for (ItemStack stack : ((Player) entity).getInventory().armor) {
            if (stack.getItem().getDescriptionId().toLowerCase().contains("small_backpack")) {
                shouldRender = true;
            }
        }

        if (shouldRender) {
            this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            poseStack.pushPose();
            poseStack.translate(0.0625f * 3.0f, 0.0625f * 10.0f, 0.0625f * 3.0f);
            if (entity.isCrouching()) poseStack.translate(0, 0.0625f * 0.5f, 0.0625f * 5.0f);
            renderColoredCutoutModel(this.model, getTextureLocation(entity), poseStack, multiBufferSource, i, entity, 1.0f, 1.0f, 1.0f);
            poseStack.popPose();
        }
    }

    @Override
    protected @NotNull CampingIdentifier getTextureLocation(@NotNull T entity) {
        return new CampingIdentifier("textures/model/small_backpack.png");
    }
}
