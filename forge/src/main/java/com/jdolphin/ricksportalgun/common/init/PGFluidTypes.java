package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.common.fluids.PortalGunFluidType;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;

import java.awt.*;

public class PGFluidTypes {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, PGConstants.MODID);

    public static final RegistryObject<FluidType> PORTAL_FLUID_TYPE = register("portal_fluid", Color.GREEN.getRGB(),
            FluidType.Properties.create().lightLevel(2).density(15).viscosity(5).sound(SoundAction.get("drink"), SoundEvents.HONEY_DRINK));

    public static final RegistryObject<FluidType> BOOTLEG_PORTAL_FLUID_TYPE = register("bootleg_portal_fluid", Color.GREEN.darker().getRGB(),
            FluidType.Properties.create().lightLevel(2).density(15).viscosity(5).sound(SoundAction.get("drink"), SoundEvents.HONEY_DRINK));

    public static final RegistryObject<FluidType> QUANTUM_LEAP_ELIXIR_TYPE = register("quantum_leap_elixir", Color.PINK.darker().getRGB(),
            FluidType.Properties.create().lightLevel(2).density(15).viscosity(5).sound(SoundAction.get("drink"), SoundEvents.HONEY_DRINK));


    private static RegistryObject<FluidType> register(String name, int color, FluidType.Properties properties) {
        return FLUID_TYPES.register(name,
                () -> new PortalGunFluidType(PGHelper.id("block/" + name + "_still"), PGHelper.id("block/" + name + "_flow"), PGHelper.id(""), color, new Vector3f(color), properties));
    }
}
