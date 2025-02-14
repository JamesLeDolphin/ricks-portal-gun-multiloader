package com.jdolphin.ricksportalgun.client.entity;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.Direction;

public class PortalEntityRenderState extends EntityRenderState {
    public int rgb;
    public boolean opening;
    public boolean closing;
    public float yRot;
    public boolean isNew;
    public Direction direction;
    public Direction facing;
    public PortalEntityRenderState() {}
}