package com.jdolphin.ricksportalgun.common.customization.type;

import net.minecraft.resources.ResourceLocation;

public class DefaultPortalType extends PortalType {
    public DefaultPortalType(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean supportsShape() {
        return false;
    }
}
