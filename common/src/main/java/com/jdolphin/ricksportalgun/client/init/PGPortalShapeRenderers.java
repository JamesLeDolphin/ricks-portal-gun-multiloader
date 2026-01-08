package com.jdolphin.ricksportalgun.client.init;

import com.jdolphin.ricksportalgun.client.render.portal.shape.*;
import com.jdolphin.ricksportalgun.common.customization.shape.PortalShape;
import com.jdolphin.ricksportalgun.common.init.PGPortalShapes;

import java.util.HashMap;
import java.util.Map;

public class PGPortalShapeRenderers {
    public static final Map<PortalShape, AbstractPortalShapeRenderer> RENDERER_MAP = new HashMap<>();

    public static void init() {
        registerRenderer(PGPortalShapes.SQUARE, new SquareShapeRenderer());
        registerRenderer(PGPortalShapes.DIAMOND, new DiamondShapeRenderer());
        registerRenderer(PGPortalShapes.TRIANGLE, new TriangleShapeRenderer());
        registerRenderer(PGPortalShapes.OCTAGON, new OctagonShapeRenderer());
    }

    public static AbstractPortalShapeRenderer getRenderer(PortalShape shape) {
        return RENDERER_MAP.get(shape);
    }

    private static void registerRenderer(PortalShape shape, AbstractPortalShapeRenderer renderer) {
        RENDERER_MAP.put(shape, renderer);
    }
}
