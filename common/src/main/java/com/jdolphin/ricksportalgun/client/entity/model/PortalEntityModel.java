package com.jdolphin.ricksportalgun.client.entity.model;

import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class PortalEntityModel extends EntityModel<PortalEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(PGHelper.id("portal"), "main");
    private final ModelPart bb_main;

    public PortalEntityModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition
                .addOrReplaceChild("bb_main", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-8.0F, -24.0F, 0.0F, 16.0F, 32.0F, 0.0F,
                                new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(PortalEntity entity, float v, float v1, float v2, float v3, float v4) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, int i2) {
        bb_main.render(poseStack, vertexConsumer, i, i1, i2);
    }
}