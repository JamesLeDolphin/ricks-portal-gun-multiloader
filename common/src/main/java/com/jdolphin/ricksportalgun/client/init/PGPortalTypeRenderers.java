package com.jdolphin.ricksportalgun.client.init;

import com.jdolphin.ricksportalgun.client.render.portal.type.AbstractPortalTypeRenderer;
import com.jdolphin.ricksportalgun.client.render.portal.type.DefaultPortalTypeRenderer;
import com.jdolphin.ricksportalgun.client.render.portal.type.EndPortalTypeRenderer;
import com.jdolphin.ricksportalgun.client.render.portal.type.WaterPortalTypeRenderer;
import com.jdolphin.ricksportalgun.common.customization.PortalType;
import com.jdolphin.ricksportalgun.common.init.PGPortalTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PGPortalTypeRenderers {
    public static final Map<PortalType, AbstractPortalTypeRenderer> RENDERER_MAP = new HashMap<>();

    public static void init() {
        registerPortalTypeRenderer(PGPortalTypes.DEFAULT, DefaultPortalTypeRenderer::new);
        registerPortalTypeRenderer(PGPortalTypes.END_PORTAL, EndPortalTypeRenderer::new);
        registerPortalTypeRenderer(PGPortalTypes.WATER, WaterPortalTypeRenderer::new);
    }

    private static void registerPortalTypeRenderer(PortalType type, Function<PortalType, AbstractPortalTypeRenderer> rendererFunction) {
        RENDERER_MAP.put(type, rendererFunction.apply(type));
    }

    public static AbstractPortalTypeRenderer getRenderer(PortalType type) {
        return RENDERER_MAP.get(type);
    }
}
