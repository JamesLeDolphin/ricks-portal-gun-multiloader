package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.item.DataCardItem;
import com.jdolphin.ricksportalgun.common.item.PortalFluidItem;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PGItems {
    private static final Map<ResourceLocation, Item> ALL = new HashMap<>();

    public static final Item PORTAL_GUN = registerGun("portal_gun");
    public static final Item GOLDEN_PORTAL_GUN = registerGun("golden_portal_gun", Color.YELLOW);
    public static final Item PRIME_PORTAL_GUN = registerGun("prime_portal_gun");

    public static final Item PORTAL_FLUID = registerFluid("portal_fluid");
    public static final Item BOOTLEG_PORTAL_FLUID = registerFluid("bootleg_portal_fluid");
    public static final Item QUANTUM_LEAP_ELIXIR = registerFluid("quantum_leap_elixir");

    public static final Item DATA_CARD = register("data_card", DataCardItem::new, new Item.Properties());

    public static final Item PORTAL_GUN_WORKBENCH = register("portal_gun_workbench", (properties) -> new BlockItem(PGBlocks.GUN_WORKBENCH, properties),
            new Item.Properties());
    public static final Item PORTAL_DISPENSER = register("portal_dispenser", (properties) -> new BlockItem(PGBlocks.PORTAL_DISPENSER, properties), new Item.Properties());

    private static Item registerGun(String name) {
        return registerGun(name, Color.GREEN);
    }

    private static Item registerGun(String name, Color color) {
        return register(name, PortalGunItem::new, new Item.Properties().stacksTo(1)
                .component(PGDataComponents.PORTAL_COLOUR, color.getRGB())
                .component(PGDataComponents.DEFAULT_PORTAL_COLOUR, color.getRGB())
                .component(PGDataComponents.BOOTLEG, false)
                .component(PGDataComponents.WAYPOINTS, List.of())
                .component(PGDataComponents.PORTAL_SIZE, 1.0f)
                .component(PGDataComponents.MAX_FUEL, 64)
                .component(PGDataComponents.FUEL, 64)
                .component(PGDataComponents.LOCK, false)
                .component(PGDataComponents.PORTAL_POS, BlockPos.ZERO)
                .component(PGDataComponents.PORTAL_DIM, Level.OVERWORLD.registry()));
    }

    private static Item registerFluid(String name) {
        return register(name, PortalFluidItem::new, new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).food(PGFoods.PORTAL_FLUID, PGFoods.PORTAL_FLUID_CONSUMABLE));
    }

    private static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
        Item block = factory.apply(properties.setId(keyOf(name)));
        ALL.put(PGHelper.createLocation(name), block);
        return block;
    }

    public static void init(BiConsumer<Item, ResourceLocation> r) {
        for (var e : ALL.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    private static ResourceKey<Item> keyOf(String id) {
        return ResourceKey.create(Registries.ITEM, PGHelper.createLocation(id));
    }
}
