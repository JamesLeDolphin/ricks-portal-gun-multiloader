package com.jdolphin.ricksportalgun.client.model;

import com.jdolphin.ricksportalgun.client.render.state.PortalEntityRenderState;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class PortalEntityModel extends EntityModel<PortalEntityRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(PGHelper.createLocation("portal"), "main");
    private final ModelPart bb_main;

    public PortalEntityModel(ModelPart root) {
        super(root);
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

}