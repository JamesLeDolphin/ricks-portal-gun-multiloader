package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.customization.shape.PortalShape;
import com.jdolphin.ricksportalgun.common.customization.shape.SimpleShape;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PGPortalShapes {
    public static final Map<ResourceLocation, PortalShape> SHAPES = new HashMap<>();

    public static final PortalShape SQUARE = register("square", SimpleShape::new);
    public static final PortalShape DIAMOND = register("diamond", SimpleShape::new);
    public static final PortalShape TRIANGLE = register("triangle", SimpleShape::new);
    public static final PortalShape OCTAGON = register("octagon", SimpleShape::new);

    public static PortalShape get(ResourceLocation rl) {
        return SHAPES.get(rl);
    }

    private static PortalShape register(String name, Function<ResourceLocation, PortalShape> function) {
        ResourceLocation rl = PGHelper.id(name);
        PortalShape shape = function.apply(rl);
        SHAPES.put(rl, shape);
        return shape;
    }
}