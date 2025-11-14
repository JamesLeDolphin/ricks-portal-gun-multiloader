package com.jdolphin.ricksportalgun.common.customization;

import net.minecraft.resources.ResourceLocation;

public class VortexPortalType extends PortalType{

    public VortexPortalType(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean canChangeShape() {
        return false;
    }
}
