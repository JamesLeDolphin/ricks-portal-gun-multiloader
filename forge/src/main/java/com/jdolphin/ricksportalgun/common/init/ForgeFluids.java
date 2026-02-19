package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.PGConstants;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForgeFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, PGConstants.MODID);

    public static final RegistryObject<FlowingFluid> PORTAL_FLUID = FLUIDS.register("portal_fluid",
            () -> new ForgeFlowingFluid.Source(ForgeFluids.PORTAL_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> PORTAL_FLUID_FLOWING = FLUIDS.register("portal_fluid_flowing",
            () -> new ForgeFlowingFluid.Flowing(ForgeFluids.PORTAL_FLUID_PROPERTIES));


    public static final RegistryObject<FlowingFluid> BOOTLEG_PORTAL_FLUID = FLUIDS.register("bootleg_portal_fluid",
            () -> new ForgeFlowingFluid.Source(ForgeFluids.BOOTLEG_PORTAL_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> BOOTLEG_PORTAL_FLUID_FLOWING = FLUIDS.register("bootleg_portal_fluid_flowing",
            () -> new ForgeFlowingFluid.Flowing(ForgeFluids.BOOTLEG_PORTAL_FLUID_PROPERTIES));


    public static final RegistryObject<FlowingFluid> QUANTUM_LEAP_ELIXIR = FLUIDS.register("quantum_leap_elixir",
            () -> new ForgeFlowingFluid.Source(ForgeFluids.QUANTUM_LEAP_ELIXIR_PROPERTIES));
    public static final RegistryObject<FlowingFluid> QUANTUM_LEAP_ELIXIR_FLOWING = FLUIDS.register("quantum_leap_elixir_flowing",
            () -> new ForgeFlowingFluid.Flowing(ForgeFluids.QUANTUM_LEAP_ELIXIR_PROPERTIES));


    public static final ForgeFlowingFluid.Properties PORTAL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            PGFluidTypes.PORTAL_FLUID_TYPE, PORTAL_FLUID, PORTAL_FLUID_FLOWING);
    public static final ForgeFlowingFluid.Properties BOOTLEG_PORTAL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            PGFluidTypes.BOOTLEG_PORTAL_FLUID_TYPE, BOOTLEG_PORTAL_FLUID, BOOTLEG_PORTAL_FLUID_FLOWING);
    public static final ForgeFlowingFluid.Properties QUANTUM_LEAP_ELIXIR_PROPERTIES = new ForgeFlowingFluid.Properties(
            PGFluidTypes.QUANTUM_LEAP_ELIXIR_TYPE, QUANTUM_LEAP_ELIXIR, QUANTUM_LEAP_ELIXIR_FLOWING);
}
