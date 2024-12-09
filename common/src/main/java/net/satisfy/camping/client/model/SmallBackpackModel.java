package net.satisfy.camping.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.satisfy.camping.core.util.CampingIdentifier;

public class SmallBackpackModel<T extends Entity> extends EntityModel<T> implements BackpackModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new CampingIdentifier("small_backpack"), "main");
    private final ModelPart small_backpack;

    public SmallBackpackModel(ModelPart root) {
        this.small_backpack = root.getChild("small_backpack");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition small_backpack = partdefinition.addOrReplaceChild("small_backpack", CubeListBuilder.create().texOffs(0, 15).addBox(-4.0F, -8.0F, 4.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, -10.0F, -1.0F, 8.0F, 10.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).addBox(-6.0F, -5.0F, 4.0F, 6.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 24.0F, -7.0F));




        return LayerDefinition.create(meshdefinition, 32, 32);
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.scale(1F, 1F, 1F);
        poseStack.translate(0.2F, 0.6F, 0.2F);
        small_backpack.render(poseStack, buffer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    @Override
    public void setupAnim(T entity, float f, float g, float h, float i, float j) {

    }

    @Override
    public void copyBody(ModelPart model) {
        small_backpack.copyFrom(model);
    }
}