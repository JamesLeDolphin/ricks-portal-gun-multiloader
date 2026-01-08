package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.customization.type.DefaultPortalType;
import com.jdolphin.ricksportalgun.common.customization.type.PortalType;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PGPortalTypes {
    public static final Map<ResourceLocation, PortalType> TYPES = new HashMap<>();

    public static final PortalType DEFAULT = register("default", DefaultPortalType::new);
    public static final PortalType END_PORTAL = register("end_portal", PortalType::new);
    public static final PortalType WATER = register("water", PortalType::new);

    public static PortalType get(ResourceLocation rl) {
        return TYPES.get(rl);
    }

    private static PortalType register(String name, Function<ResourceLocation, PortalType> typeFunction) {
        PortalType type = typeFunction.apply(PGHelper.id(name));
        TYPES.put(PGHelper.id(name), type);
        return type;
    }

}
