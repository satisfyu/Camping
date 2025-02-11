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
import net.satisfy.camping.core.util.CampingIdentifier;

public class WandererBackpackModel<T extends Entity> extends EntityModel<T> implements BackpackModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new CampingIdentifier("wanderer_backpack"), "main");
    private final ModelPart wanderer_backpack;

    public WandererBackpackModel(ModelPart root) {
        this.wanderer_backpack = root.getChild("wanderer_backpack");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition wanderer_backpack = partdefinition.addOrReplaceChild("wanderer_backpack", CubeListBuilder.create()
                .texOffs(0, 14).addBox(-6.0F, -11.0F, -7.0F, 12.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-5.0F, -8.0F, -8.0F, 10.0F, 4.0F, 6.0F, new CubeDeformation(0.2F))
                .texOffs(0, 0).addBox(-5.0F, -8.0F, -8.0F, 10.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0, 0, 0));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        wanderer_backpack.render(poseStack, buffer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    @Override
    public void setupAnim(T entity, float f, float g, float h, float i, float j) {
        wanderer_backpack.xRot = entity.isCrouching() ? 30.0f * (Mth.PI/180.0f) : 0;
    }

    @Override
    public void copyBody(ModelPart model) {
        wanderer_backpack.copyFrom(model);
    }
}