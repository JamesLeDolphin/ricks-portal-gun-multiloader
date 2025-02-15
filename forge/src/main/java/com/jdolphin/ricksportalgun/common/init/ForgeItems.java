package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.Constants;
import com.jdolphin.ricksportalgun.common.item.PortalFluidItem;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;
import java.util.List;

public class ForgeItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);

    public static final RegistryObject<Item> PORTAL_GUN = registerGun("portal_gun");
    public static final RegistryObject<Item> GOLDEN_PORTAL_GUN = registerGun("golden_portal_gun", Color.YELLOW);
    public static final RegistryObject<Item> PRIME_PORTAL_GUN = registerGun("prime_portal_gun");

    public static final RegistryObject<Item> PORTAL_FLUID_BOTTLE = registerFluid("portal_fluid_bottle");
    public static final RegistryObject<Item> BOOTLEG_PORTAL_FLUID_BOTTLE = registerFluid("bootleg_portal_fluid_bottle");
    public static final RegistryObject<Item> QUANTUM_LEAP_ELIXIR = registerFluid("quantum_leap_elixir");


    private static RegistryObject<Item> registerFluid(String name) {
        return ITEMS.register(name,
                () -> new PortalFluidItem(new Item.Properties()
                        .setId(ITEMS.key(name))
                        .craftRemainder(Items.GLASS_BOTTLE)
                        .food(PGFoods.PORTAL_FLUID, PGFoods.PORTAL_FLUID_CONSUMABLE)));
    }

    private static RegistryObject<Item> registerGun(String name, Color colour) {
        return ITEMS.register(name, () -> new PortalGunItem(new Item.Properties()
                .setId(ITEMS.key(name))
                .stacksTo(1)
                .component(PGDataComponents.BOOTLEG, false)
                .component(PGDataComponents.WAYPOINTS, List.of())
                .component(PGDataComponents.FUEL, 64)
                .component(PGDataComponents.LOCK, false)
                .component(PGDataComponents.MAX_FUEL, 64)
                .component(PGDataComponents.DEFAULT_COLOUR, colour.getRGB())
                .component(PGDataComponents.PORTAL_COLOUR, colour.getRGB())
                .component(PGDataComponents.PORTAL_POS, BlockPos.ZERO)
                .component(PGDataComponents.PORTAL_DIM, Level.OVERWORLD.location())
        ));
    }

    private static RegistryObject<Item> registerGun(String name) {
        return registerGun(name, Color.GREEN);
    }
}
