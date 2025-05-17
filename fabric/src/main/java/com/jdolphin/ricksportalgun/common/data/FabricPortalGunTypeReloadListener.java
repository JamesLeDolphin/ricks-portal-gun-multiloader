package com.jdolphin.ricksportalgun.common.data;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class FabricPortalGunTypeReloadListener extends PortalGunTypeReloadListener implements IdentifiableResourceReloadListener {

    @Override
    public ResourceLocation getFabricId() {
        return this.getID();
    }
}
