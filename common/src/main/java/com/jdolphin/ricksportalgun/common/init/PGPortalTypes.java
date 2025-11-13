package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.client.render.portal.*;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class PGPortalTypes {
    public static final Map<ResourceLocation, PortalTypeRenderer> TYPES = new HashMap<>();

    public static final PortalTypeRenderer DEFAULT = register("default", new DefaultPortalTypeRenderer());
    public static final PortalTypeRenderer END_PORTAL = register("end_portal", new EndPortalTypeRenderer());
    public static final PortalTypeRenderer VORTEX = register("vortex", new VortexTypeRenderer());
    public static final PortalTypeRenderer WATER = register("water", new WaterPortalTypeRenderer());

    private static PortalTypeRenderer register(String name, PortalTypeRenderer type) {
        TYPES.put(PGHelper.id(name), type);
        return type;
    }

}
