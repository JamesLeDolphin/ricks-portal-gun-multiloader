package com.jdolphin.ricksportalgun.client.init;

import com.jdolphin.ricksportalgun.client.render.portal.*;
import com.jdolphin.ricksportalgun.common.customization.PortalType;
import com.jdolphin.ricksportalgun.common.init.PGPortalTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PGPortalTypeRenderers {
    public static final Map<PortalType, PortalTypeRenderer> RENDERER_MAP = new HashMap<>();

    public static void init() {
        registerPortalTypeRenderer(PGPortalTypes.DEFAULT, DefaultPortalTypeRenderer::new);
        registerPortalTypeRenderer(PGPortalTypes.END_PORTAL, EndPortalTypeRenderer::new);
        registerPortalTypeRenderer(PGPortalTypes.VORTEX, VortexTypeRenderer::new);
        registerPortalTypeRenderer(PGPortalTypes.WATER, WaterPortalTypeRenderer::new);
    }

    private static void registerPortalTypeRenderer(PortalType type, Function<PortalType, PortalTypeRenderer> rendererFunction) {
        RENDERER_MAP.put(type, rendererFunction.apply(type));
    }

    public static PortalTypeRenderer getRenderer(PortalType type) {
        return RENDERER_MAP.get(type);
    }
}
