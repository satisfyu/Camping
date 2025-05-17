package net.satisfy.camping.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.camping.client.model.MosquitoModel;
import net.satisfy.camping.core.world.entity.Mosquito;

public class MosquitoRenderer extends MobRenderer<Mosquito, MosquitoModel<Mosquito>> {

    private static final ResourceLocation MOSQUITO_LOCATION = new ResourceLocation("minecraft", "textures/entity/phantom.png");

    public MosquitoRenderer(EntityRendererProvider.Context context) {
        super(context, new MosquitoModel<Mosquito>(context.bakeLayer(MosquitoModel.MOSQUITO_LAYER)), 0.15F); // float for shadow size
    }

    @Override
    public ResourceLocation getTextureLocation(Mosquito mosquito) {
        return MOSQUITO_LOCATION;
    }

    protected void setupRotations(Mosquito mosquito, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(mosquito, stack, ageInTicks, rotationYaw, partialTicks);
        stack.mulPose(Axis.XP.rotationDegrees(mosquito.getXRot()));
    }
}

