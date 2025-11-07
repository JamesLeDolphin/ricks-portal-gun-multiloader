package com.jdolphin.ricksportalgun.common.customization;

import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;

public abstract class PGPortalType {
    protected Model model;
    private final String id;
    private final boolean needsModel;

    public PGPortalType(String id, boolean needsModel) {
        this.id = id;
        this.needsModel = needsModel;
    }

    public boolean needsModel() {
        return needsModel;
    }

    public String getId() {
        return id;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Model getModel() {
        return model;
    }

    public abstract void renderPortal(PortalEntity entity, float yaw, float partialTick, PoseStack stack, MultiBufferSource source, int packedLight, float red, float green, float blue);
}
