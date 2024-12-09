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

public class WandererBagModel<T extends Entity> extends EntityModel<T> implements BackpackModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new CampingIdentifier("wanderer_bag"), "main");
    private final ModelPart wanderer_bag;

    public WandererBagModel(ModelPart root) {
        this.wanderer_bag = root.getChild("wanderer_bag");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition wanderer_bag = partdefinition.addOrReplaceChild("wanderer_bag", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, -10.0F, -2.0F, 8.0F, 10.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(9.0F, -4.0F, -1.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).mirror().addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, 24.0F, -6.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }



    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.scale(1F, 1F, 1F);
        poseStack.translate(-0.3F, 0.6F, 0.225F);
        wanderer_bag.render(poseStack, buffer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    @Override
    public void setupAnim(T entity, float f, float g, float h, float i, float j) {

    }

    @Override
    public void copyBody(ModelPart model) {
        wanderer_bag.copyFrom(model);
    }
}