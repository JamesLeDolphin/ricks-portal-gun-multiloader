package com.jdolphin.ricksportalgun.client.entity;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

public class PortalEntityRenderState extends EntityRenderState {
    public int rgb;
    public boolean opening;
    public boolean closing;
    public float yRot;
    public boolean isNew;
    public Direction direction;
    public Direction facing;
    public float width;
    public Component name;

    public PortalEntityRenderState() {}
}