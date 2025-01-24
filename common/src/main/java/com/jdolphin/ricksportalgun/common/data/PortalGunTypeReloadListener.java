package com.jdolphin.ricksportalgun.common.data;

import com.jdolphin.ricksportalgun.common.init.PortalGunTypeRegistry;
import com.jdolphin.ricksportalgun.common.util.PortalGunType;
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

    @Override
    protected void apply(Map<ResourceLocation, PortalGunType> gunTypeMap, ResourceManager manager, ProfilerFiller profiler) {
        PortalGunTypeRegistry.PORTAL_GUN_TYPES.clear();
        System.out.println("WABADA");
        for (Map.Entry<ResourceLocation, PortalGunType> entry : gunTypeMap.entrySet()) {
            PortalGunType type = entry.getValue();
            if (type != null) {
                System.out.println(type.name());
                System.out.println(type.id());
                System.out.println(type.color().getRGB());
                System.out.println(type.model());
                PortalGunTypeRegistry.add(type);
            }
        }
    }
}
