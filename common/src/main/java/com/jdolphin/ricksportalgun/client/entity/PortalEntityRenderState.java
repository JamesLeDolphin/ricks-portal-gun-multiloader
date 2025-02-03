package com.jdolphin.ricksportalgun.client.entity;

import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class PortalEntityRenderState extends EntityRenderState {
    public int rgb;
    public boolean opening;
    public boolean closing;
    public float yRot;
    public boolean isNew;
    public boolean flat;

    public PortalEntityRenderState() {}
}