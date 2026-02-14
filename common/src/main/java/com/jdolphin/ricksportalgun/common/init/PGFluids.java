package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.fluid.BootlegPortalFluid;
import com.jdolphin.ricksportalgun.common.fluid.PortalFluid;
import com.jdolphin.ricksportalgun.common.fluid.QuantumLeapElixirFluid;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FlowingFluid;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class PGFluids {
    public static final Map<ResourceLocation, FlowingFluid> ALL = new HashMap<>();

    public static Pair<FlowingFluid, FlowingFluid> PORTAL_FLUID = registerBoth("portal_fluid", new PortalFluid.Source(), new PortalFluid.Flowing());
    public static Pair<FlowingFluid, FlowingFluid> BOOTLEG_PORTAL_FLUID = registerBoth("bootleg_portal_fluid", new BootlegPortalFluid.Source(), new BootlegPortalFluid.Flowing());
    public static Pair<FlowingFluid, FlowingFluid> QUANTUM_LEAP_ELIXIR= registerBoth("quantum_leap_elixir", new QuantumLeapElixirFluid.Source(), new QuantumLeapElixirFluid.Flowing());

    public static void init(BiConsumer<FlowingFluid, ResourceLocation> r) {
        for (var e : ALL.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    private static Pair<FlowingFluid, FlowingFluid> registerBoth(String name, FlowingFluid source, FlowingFluid flowing) {
        ResourceLocation sourceRl = PGHelper.id(name);
        ALL.put(sourceRl, source);
        ResourceLocation flowingRl = PGHelper.id(name + "_flowing");
        ALL.put(flowingRl, flowing);
        return new Pair<>(source, flowing);
    }
}
