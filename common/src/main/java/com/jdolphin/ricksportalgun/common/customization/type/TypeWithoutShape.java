package com.jdolphin.ricksportalgun.common.customization.type;

import net.minecraft.resources.ResourceLocation;

public class TypeWithoutShape extends PortalType {
    public TypeWithoutShape(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean supportsShape() {
        return false;
    }
}
