package net.satisfy.camping.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.satisfy.camping.Camping;

public class EnderpackModel<T extends Entity> extends EntityModel<T> implements BackpackModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Camping.identifier("enderpack"), "main");
    private final ModelPart enderpack;

    public EnderpackModel(ModelPart root) {
        this.enderpack = root.getChild("enderpack");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition enderpack = partdefinition.addOrReplaceChild("enderpack", CubeListBuilder.create()
                .texOffs(0, 0).addBox(0, 0, 0, 10.0F, 10.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(2, 2, 6, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        enderpack.render(poseStack, buffer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    @Override
    public void setupAnim(T entity, float f, float g, float h, float i, float j) {
        enderpack.xRot = entity.isCrouching() ? 30.0f * (Mth.PI/180.0f) : 0;
    }

    @Override
    public void copyBody(ModelPart model) {
        enderpack.copyFrom(model);
    }
}