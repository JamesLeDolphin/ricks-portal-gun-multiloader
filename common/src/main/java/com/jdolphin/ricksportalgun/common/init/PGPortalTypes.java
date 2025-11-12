package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.customization.PGPortalType;
import com.jdolphin.ricksportalgun.common.customization.portaltypes.DefaultPortalType;
import com.jdolphin.ricksportalgun.common.customization.portaltypes.EndPortalType;
import com.jdolphin.ricksportalgun.common.customization.portaltypes.VortexType;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class PGPortalTypes {
    public static final Map<ResourceLocation, PGPortalType> TYPES = new HashMap<>();

    public static final PGPortalType DEFAULT = register("default", new DefaultPortalType());
    public static final PGPortalType END_PORTAL = register("end_portal", new EndPortalType());
    public static final PGPortalType VORTEX = register("vortex", new VortexType());

    private static PGPortalType register(String name, PGPortalType type) {
        TYPES.put(PGHelper.id(name), type);
        return type;
    }

}
