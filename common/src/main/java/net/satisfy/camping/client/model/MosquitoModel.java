package net.satisfy.camping.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.satisfy.camping.Camping;
import net.satisfy.camping.core.world.entity.Mosquito;

public class MosquitoModel<T extends Mosquito> extends HierarchicalModel<T> {

    public static final ModelLayerLocation MOSQUITO_LAYER = new ModelLayerLocation(Camping.identifier("mosquito"), "main");

    private final ModelPart root;

    public MosquitoModel(ModelPart root) {
        super();
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(T mosquito, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
    }
}

