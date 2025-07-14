package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.item.DataCardItem;
import com.jdolphin.ricksportalgun.common.item.PortalFluidItem;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.item.upgrade.CreativeUpgradeItem;
import com.jdolphin.ricksportalgun.common.item.upgrade.SimpleConditionalUpgradeItem;
import com.jdolphin.ricksportalgun.common.item.upgrade.SimpleUpgradeItem;
import com.jdolphin.ricksportalgun.common.util.PGCreativeModeTabs;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

@SuppressWarnings("unused")
public class PGItems {
    public static final Map<ResourceLocation, Item> ALL = new HashMap<>();
    public static final Map<Item, ResourceKey<CreativeModeTab>> TABS = new LinkedHashMap<>();

    public static final Item PORTAL_GUN = registerGun("portal_gun");
    public static final Item GOLDEN_PORTAL_GUN = registerGun("golden_portal_gun", Color.YELLOW, null);
    public static final Item PRIME_PORTAL_GUN = registerGun("prime_portal_gun", Color.GREEN, null);

    public static final Item PORTAL_FLUID = registerFluid("portal_fluid");
    public static final Item BOOTLEG_PORTAL_FLUID = registerFluid("bootleg_portal_fluid");
    public static final Item QUANTUM_LEAP_ELIXIR = registerFluid("quantum_leap_elixir");

    public static final Item DATA_CARD = register("data_card", DataCardItem::new, new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);

    public static final Item PORTAL_DISPENSER = register("portal_dispenser", (properties) -> new BlockItem(PGBlocks.PORTAL_DISPENSER, properties),
            new Item.Properties(), PGCreativeModeTabs.FUNCTIONAL_BLOCKS);

    public static final Item PORTAL_GUN_WORKBENCH = register("portal_gun_workbench", (properties) -> new BlockItem(PGBlocks.GUN_WORKBENCH, properties),
            new Item.Properties(), PGCreativeModeTabs.FUNCTIONAL_BLOCKS);

    public static final Item CIRCUIT_BOARD = register("circuitboard", Item::new, new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);

    //Upgrades
    public static final Item CREATIVE_UPGRADE = register("upgrade_creative", CreativeUpgradeItem::new, new Item.Properties().rarity(Rarity.EPIC), PGCreativeModeTabs.INGREDIENTS);

    public static final Item DURABILITY_UPGRADE = register("upgrade_durability", properties -> new SimpleUpgradeItem(properties,
            (stack, portalGun) -> stack.set(DataComponents.FIRE_RESISTANT, Unit.INSTANCE)),
            new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);

    public static final Item WAYPOINT_UPGRADE = register("upgrade_waypoint", properties -> new SimpleUpgradeItem(properties,
            (stack, portalGun) -> stack.set(PGDataComponents.HAS_WAYPOINTS, true)), new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);

    public static final Item DIM_UPGRADE = register("upgrade_dimension_mk1", properties -> new SimpleUpgradeItem(properties,
            (stack, portalGun) -> stack.set(PGDataComponents.EXTRA_DIMENSIONS, true)), new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);

    public static final Item BETTER_DIM_UPGRADE = register("upgrade_dimension_mk2", properties -> new SimpleConditionalUpgradeItem(properties,
            (stack, portalGun) -> stack.set(PGDataComponents.EXTRA_DIMENSIONS_2, true),
            ((stack, portalGun) -> stack.getOrDefault(PGDataComponents.EXTRA_DIMENSIONS, false)),
                    Component.translatable("error.ricksportalgun.upgrade.needs_dimension")),
            new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);

    public static final Item SETTINGS_UPGRADE = register("upgrade_settings", properties -> new SimpleUpgradeItem(properties,
            (stack, portalGun) -> stack.set(PGDataComponents.SETTINGS, true)), new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);

    public static final Item FUEL_UPGRADE = register("upgrade_fuel", properties -> new SimpleUpgradeItem(properties,
            (stack, portalGun) -> stack.set(PGDataComponents.MAX_FUEL, 128)), new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);

    public static final Item BIOME_LOC_UPGRADE = register("upgrade_biome_locator", properties -> new SimpleUpgradeItem(properties,
            (stack, portalGun) -> stack.set(PGDataComponents.BIOME_LOC, true)), new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);

    public static final Item PLAYER_LOC_UPGRADE = register("upgrade_player_locator", properties -> new SimpleConditionalUpgradeItem(properties,
            (stack, portalGun) -> stack.set(PGDataComponents.PLAYER_LOC, true),
            (stack, item) -> stack.getOrDefault(PGDataComponents.BIOME_LOC, false),
            Component.translatable("error.ricksportalgun.upgrade.needs_biome")), new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);

    public static final Item STRUCTURE_LOC_UPGRADE = register("upgrade_structure_locator", properties -> new SimpleConditionalUpgradeItem(properties,
            (stack, portalGun) -> stack.set(PGDataComponents.STRUCTURE_LOC, true),
            (stack, item) -> stack.getOrDefault(PGDataComponents.PLAYER_LOC, false),
            Component.translatable("error.ricksportalgun.upgrade.needs_player")), new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);

    private static Item registerGun(String name) {
        return registerGun(name, Color.GREEN, PGCreativeModeTabs.TOOLS_AND_UTILITIES);
    }

    private static Item registerGun(String name, Color color, ResourceKey<CreativeModeTab> tab) {
        return register(name, PortalGunItem::new, gunProperties(color), tab);
    }

    public static Item.Properties gunProperties(Color color) {
        return new Item.Properties().stacksTo(1)
                .component(PGDataComponents.PORTAL_COLOUR, color.getRGB())
                .component(PGDataComponents.DEFAULT_PORTAL_COLOUR, color.getRGB())
                .component(PGDataComponents.BOOTLEG, false)
                .component(PGDataComponents.WAYPOINTS, List.of())
                .component(PGDataComponents.PORTAL_SIZE, 1.0f)
                .component(PGDataComponents.MAX_FUEL, 64)
                .component(PGDataComponents.FUEL, 64)
                .component(PGDataComponents.LOCK, false)
                .component(PGDataComponents.HAS_WAYPOINTS, false)
                .component(PGDataComponents.EXTRA_DIMENSIONS, false)
                .component(PGDataComponents.SETTINGS, false)
                .component(PGDataComponents.BIOME_LOC, false)
                .component(PGDataComponents.PLAYER_LOC, false)
                .component(PGDataComponents.STRUCTURE_LOC, false)
                .component(PGDataComponents.PORTAL_POS, BlockPos.ZERO)
                .component(PGDataComponents.PORTAL_DIM, Level.OVERWORLD.location());
    }

    private static Item registerFluid(String name) {
        return register(name, PortalFluidItem::new, new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).food(PGFoods.PORTAL_FLUID),
                PGCreativeModeTabs.FOOD_AND_DRINKS);
    }

    private static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties, ResourceKey<CreativeModeTab> tab) {
        Item item = factory.apply(properties);
        TABS.put(item, tab);
        ALL.put(PGHelper.createLocation(name), item);
        return item;
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
