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
import net.satisfy.camping.client.model.EnderpackModel;
import net.satisfy.camping.forge.client.CampingClientForge;
import net.satisfy.camping.core.util.CampingIdentifier;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosEnderpackLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    private final EnderpackModel<T> model;

    public CuriosEnderpackLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
        this.model = new EnderpackModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(CampingClientForge.ENDERPACK_LAYER));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int light, @NotNull T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!(entity instanceof Player player)) {
            return;
        }

        boolean shouldRender = false;

        for (ItemStack stack : player.getInventory().armor) {
            if (stack.getItem().getDescriptionId().toLowerCase().contains("enderpack")) {
                shouldRender = true;
                break;
            }
        }

        if (!shouldRender) {
            shouldRender = CuriosApi.getCuriosHelper().getEquippedCurios(player).map(handler -> {
                for (int slot = 0; slot < handler.getSlots(); slot++) {
                    ItemStack stack = handler.getStackInSlot(slot);
                    if (stack.getItem().getDescriptionId().toLowerCase().contains("enderpack")) {
                        return true;
                    }
                }
                return false;
            }).orElse(false);
        }

        if (shouldRender) {
            this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            poseStack.pushPose();
            poseStack.translate(0F, -1F, 0.025F);
            renderColoredCutoutModel(this.model, getTextureLocation(entity), poseStack, multiBufferSource, light, entity, 1.0f, 1.0f, 1.0f);
            poseStack.popPose();
        }
    }

    @Override
    protected @NotNull CampingIdentifier getTextureLocation(@NotNull T entity) {
        return new CampingIdentifier("textures/model/enderpack.png");
    }
}
