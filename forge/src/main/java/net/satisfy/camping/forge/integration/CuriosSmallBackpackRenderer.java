package net.satisfy.camping.forge.integration;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.forge.client.renderer.player.layers.CuriosSmallBackpackLayer;
import net.satisfy.camping.forge.client.renderer.player.layers.SmallBackpackLayer;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CuriosSmallBackpackRenderer implements ICurioRenderer {


    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack,
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

        CuriosApi.getCuriosHelper().getCurio(stack).ifPresent(curio -> {

            // this works as expected, and detects when the player is wearing a registered curio item

            System.out.println("found a curio registered to item stack " + stack.getDisplayName().getString());

            if (curio instanceof CuriosBackpack shulkerBoxAccessory) {

                // @satisfy this is currently failing ^^
                System.out.println("attempted to render CuriosBackpack");

                LivingEntity livingEntity = slotContext.entity();
//                ICurioRenderer.translateIfSneaking(matrixStack, livingEntity);
//                ICurioRenderer.rotateIfSneaking(matrixStack, livingEntity);
                CuriosSmallBackpackLayer.render(matrixStack, renderTypeBuffer, light, partialTicks, livingEntity, shulkerBoxAccessory, stack);
            }
        });



//        renderLayerParent.getModel().renderToBuffer(poseStack, );

//        boolean shouldRender = false;
//
//        for (ItemStack stack : ((Player) entity).getInventory().armor) {
//            if (stack.getItem().getDescriptionId().toLowerCase().contains("small_backpack")) {
//                shouldRender = true;
//            }
//        }
//
//        if (shouldRender) {
//            this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
//            poseStack.pushPose();
//            poseStack.translate(-0.2F, -1.5F, 0.425F);
//            renderColoredCutoutModel(this.model, getTextureLocation(entity), poseStack, multiBufferSource, i, entity, 1.0f, 1.0f, 1.0f);
//            poseStack.popPose();
//        }
    }
}
