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

public class GoodybagModel<T extends Entity> extends EntityModel<T> implements BackpackModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Camping.identifier("goodybag"), "main");
    private final ModelPart goodybag;

    public GoodybagModel(ModelPart root) {
        this.goodybag = root.getChild("goodybag");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition goodybag = partdefinition.addOrReplaceChild("goodybag", CubeListBuilder.create().texOffs(0, 26).addBox(2.0F, -5.0F, 4.0F, 8.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(0.0F, 0.0F, -2.0F, 12.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(29, 2).addBox(5.0F, -8.0F, 4.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(1.0F, -10.0F, -2.0F, 10.0F, 10.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(32, 10).addBox(1.0F, -10.0F, -2.0F, 10.0F, 3.0F, 6.0F, new CubeDeformation(0.2F))
                .texOffs(16, 27).addBox(-2.0F, -5.0F, -2.0F, 3.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(34, 27).addBox(11.0F, -5.0F, -2.0F, 2.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0, 0, 0));

        PartDefinition goods = goodybag.addOrReplaceChild("goods", CubeListBuilder.create().texOffs(8, 33).addBox(11.0F, -12.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition small_map_r1 = goods.addOrReplaceChild("small_map_r1", CubeListBuilder.create().texOffs(8, 33).addBox(-0.2F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(11.0F, -2.0F, 2.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition spyglass_r1 = goods.addOrReplaceChild("spyglass_r1", CubeListBuilder.create().texOffs(0, 33).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -8.0F, 1.0F, 0.0F, -0.1309F, -0.6545F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        goodybag.render(poseStack, buffer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    @Override
    public void setupAnim(T entity, float f, float g, float h, float i, float j) {
        goodybag.xRot = entity.isCrouching() ? 30.0f * (Mth.PI/180.0f) : 0;
    }

    @Override
    public void copyBody(ModelPart model) {
        goodybag.copyFrom(model);
    }
}