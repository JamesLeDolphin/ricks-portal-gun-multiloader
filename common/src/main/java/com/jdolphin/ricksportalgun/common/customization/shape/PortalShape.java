package com.jdolphin.ricksportalgun.common.customization.shape;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class PortalShape {
    protected ResourceLocation id;

    public PortalShape(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation getId() {
        return id;
    }

    public Component getTranslationName() {
        return Component.translatable(id.getNamespace() + ".shape." + id.getPath());
    }
}
