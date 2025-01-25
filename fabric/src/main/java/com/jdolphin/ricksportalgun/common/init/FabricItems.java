package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.item.DataCardItem;
import com.jdolphin.ricksportalgun.common.item.PortalFluidItem;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.awt.*;
import java.util.List;
import java.util.function.Function;

public class FabricItems {

    public static void register() {
        PGItems.PORTAL_GUN = registerGun("portal_gun");
        PGItems.GOLDEN_PORTAL_GUN = registerGun("golden_portal_gun");
        PGItems.PRIME_PORTAL_GUN = registerGun("prime_portal_gun");

        PGItems.PORTAL_FLUID = registerFluid("portal_fluid");
        PGItems.BOOTLEG_PORTAL_FLUID = registerFluid("bootleg_portal_fluid");
        PGItems.QUANTUM_LEAP_ELIXIR = registerFluid("quantum_leap_elixir");

        PGItems.DATA_CARD = registerItem("data_card", DataCardItem::new, new Item.Properties());

        PGItems.PORTAL_GUN_WORKBENCH = registerItem("portal_gun_workbench", (properties) -> new BlockItem(PGBlocks.GUN_WORKBENCH, properties), new Item.Properties());
    }

    private static Item registerFluid(String name) {
        return registerItem(name, PortalFluidItem::new, new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).food(PGFoods.PORTAL_FLUID, PGFoods.PORTAL_FLUID_CONSUMABLE));
    }

    private static Item registerItem(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
        return Items.registerItem(keyOf(name), factory, properties);
    }

    private static Item registerGun(String name, Color color) {
        return registerItem(name, PortalGunItem::new, new Item.Properties().stacksTo(1)
                .component(PGDataComponents.PORTAL_COLOUR, color.getRGB())
                .component(PGDataComponents.DEFAULT_COLOUR, color.getRGB())
                .component(PGDataComponents.BOOTLEG, false)
                .component(PGDataComponents.WAYPOINTS, List.of())
                .component(PGDataComponents.MAX_FUEL, 16)
                .component(PGDataComponents.FUEL, 16)
                .component(PGDataComponents.LOCK, false)
                .component(PGDataComponents.PORTAL_POS, BlockPos.ZERO)
                .component(PGDataComponents.PORTAL_DIM, Level.OVERWORLD.registry()));
    }

    private static ResourceKey<Item> keyOf(String id) {
        return ResourceKey.create(Registries.ITEM, Helper.createLocation(id));
    }

    private static Item registerGun(String name) {
        return registerGun(name, Color.GREEN);
    }
}
