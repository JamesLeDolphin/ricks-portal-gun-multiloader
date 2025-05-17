package com.jdolphin.ricksportalgun.common.data;

import com.jdolphin.ricksportalgun.common.init.PortalGunTypeRegistry;
import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class PortalGunTypeReloadListener extends SimpleJsonResourceReloadListener<PortalGunType> {

    public PortalGunTypeReloadListener() {
        super(PortalGunType.CODEC, FileToIdConverter.json("portal_guns"));
    }

    public ResourceLocation getID() {
        return PGHelper.createLocation("portal_guns");
    }

    @Override
    protected void apply(Map<ResourceLocation, PortalGunType> gunTypeMap, ResourceManager manager, ProfilerFiller profiler) {
        PortalGunTypeRegistry.PORTAL_GUN_TYPES.clear();
        for (Map.Entry<ResourceLocation, PortalGunType> entry : gunTypeMap.entrySet()) {
            PortalGunType type = entry.getValue();
            if (type != null) {
                PortalGunTypeRegistry.add(type);
            }
        }
    }
}
