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

public class LargeBackpackModel<T extends Entity> extends EntityModel<T> implements BackpackModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new CampingIdentifier("large_backpack"), "main");
    private final ModelPart large_backpack;

    public LargeBackpackModel(ModelPart root) {
        this.large_backpack = root.getChild("large_backpack");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition large_backpack = partdefinition.addOrReplaceChild("large_backpack", CubeListBuilder.create().texOffs(0, 10).addBox(-5.0F, -12.0F, -8.0F, 10.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 26).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(29, 2).addBox(-1.0F, -10.0F, -4.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-6.0F, -17.0F, -8.0F, 12.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.scale(1F, 1F, 1F);
        poseStack.translate(0F, 0.75F, 0.6F);
        large_backpack.render(poseStack, buffer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    @Override
    public void setupAnim(T entity, float f, float g, float h, float i, float j) {

    }

    @Override
    public void copyBody(ModelPart model) {
        large_backpack.copyFrom(model);
    }
}