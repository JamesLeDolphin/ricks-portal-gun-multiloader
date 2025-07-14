package com.jdolphin.ricksportalgun.common.data;

import com.google.gson.JsonElement;
import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.common.init.PortalGunTypeRegistry;
import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class PortalGunTypeReloadListener extends SimpleJsonResourceReloadListener {

    public PortalGunTypeReloadListener() {
        super(PGConstants.GSON, "portal_guns");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        PortalGunTypeRegistry.PORTAL_GUN_TYPES.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
            JsonElement element = entry.getValue();
            PortalGunType type = PGConstants.GSON.fromJson(element, PortalGunType.class);
            if (type != null) {
                PortalGunTypeRegistry.add(type);
            }
        }
    }


    public ResourceLocation getID() {
        return PGHelper.createLocation("portal_guns");
    }

}
