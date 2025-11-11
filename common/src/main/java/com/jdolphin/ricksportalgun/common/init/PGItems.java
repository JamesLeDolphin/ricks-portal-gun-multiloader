package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.item.DataCardItem;
import com.jdolphin.ricksportalgun.common.item.PortalFluidItem;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.item.TooltipBlockItem;
import com.jdolphin.ricksportalgun.common.item.upgrade.UpgradeItem;
import com.jdolphin.ricksportalgun.common.item.upgrade.types.UpgradeType;
import com.jdolphin.ricksportalgun.common.util.PGCreativeModeTabs;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@SuppressWarnings("unused")
public class PGItems {
    public static final Map<ResourceLocation, Item> ALL = new HashMap<>();
    public static final Map<Item, ResourceKey<CreativeModeTab>> TABS = new LinkedHashMap<>();
    public static final List<PortalGunItem> PORTAL_GUNS = new ArrayList<>();
    public static final List<UpgradeItem> UPGRADES = new ArrayList<>();

    public static final Item PORTAL_GUN = registerGun("portal_gun");
    public static final Item GOLDEN_PORTAL_GUN = registerGun("golden_portal_gun", 2, Color.YELLOW, null);
    public static final Item PRIME_PORTAL_GUN = registerGun("prime_portal_gun", 2, Color.GREEN, null);

    public static final Item ALT_PORTAL_GUN = registerGun("alt_portal_gun", 3, Color.GREEN, null);
    public static final Item FLINTLOCK_PORTAL_GUN = registerGun("flintlock_portal_gun", 3, Color.GREEN, null);
    public static final Item FUTURISTIC_PORTAL_GUN = registerGun("futuristic_portal_gun", 3, Color.GREEN, null);
    public static final Item JUNK_PORTAL_GUN = registerGun("junk_portal_gun", 3, Color.GREEN, null);
    public static final Item PORTAL_GUN_MK_II = registerGun("portal_gun_mk2", 2, Color.GREEN, null);
    public static final Item SIDESTEP_PORTAL_GUN = registerGun("sidestep_portal_gun", 3, Color.GREEN, null);
    public static final Item SLOPED_PORTAL_GUN = registerGun("sloped_portal_gun", 2, Color.GREEN, null);
    public static final Item SQUARE_PORTAL_GUN = registerGun("square_portal_gun", 2, Color.GREEN, null);
    public static final Item SYMMETRICAL_PORTAL_GUN = registerGun("symmetrical_portal_gun", 3,  Color.GREEN, null);

    public static final Item PORTAL_FLUID = registerFluid("portal_fluid");
    public static final Item BOOTLEG_PORTAL_FLUID = registerFluid("bootleg_portal_fluid");
    public static final Item QUANTUM_LEAP_ELIXIR = registerFluid("quantum_leap_elixir");

    public static final Item DATA_CARD = register("data_card", DataCardItem::new, new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);
    public static final Item DISC_TEMPLATE = register("disc_template", Item::new, new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);

    public static final Item PORTAL_DISPENSER = register("portal_dispenser", (properties) -> new BlockItem(PGBlocks.PORTAL_DISPENSER, properties),
            new Item.Properties(), PGCreativeModeTabs.REDSTONE_BLOCKS);
    public static final Item SUBETHER_BARRIER = register("subether_barrier", (properties) -> new TooltipBlockItem(PGBlocks.SUBETHER_BARRIER, properties,
                    Component.translatable("tooltip.ricksportalgun.barrier")), new Item.Properties(), PGCreativeModeTabs.REDSTONE_BLOCKS);

    public static final Item PORTAL_GUN_WORKBENCH = register("portal_gun_workbench", (properties) -> new BlockItem(PGBlocks.GUN_WORKBENCH, properties),
            new Item.Properties(), PGCreativeModeTabs.FUNCTIONAL_BLOCKS);

    public static final Item CIRCUIT_BOARD = register("circuitboard", Item::new, new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);

    //Upgrades
    public static final Item CREATIVE_UPGRADE = registerUpgrade("upgrade_creative", properties -> new UpgradeItem(properties, PGUpgradeTypes.CREATIVE),
            new Item.Properties().rarity(Rarity.EPIC), PGCreativeModeTabs.INGREDIENTS);

    public static final Item WAYPOINT_UPGRADE = registerUpgrade("upgrade_waypoint", properties -> new UpgradeItem(properties, PGUpgradeTypes.WAYPOINTS),
            new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);

    public static final Item DIM_UPGRADE = registerUpgrade("upgrade_dimension_mk1", properties -> new UpgradeItem(properties,
            PGUpgradeTypes.DIM_1), new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);

    public static final Item BETTER_DIM_UPGRADE = registerUpgrade("upgrade_dimension_mk2", properties -> new UpgradeItem(properties, PGUpgradeTypes.DIM_2),
            new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);

    public static final Item SETTINGS_UPGRADE = registerUpgrade("upgrade_settings", properties -> new UpgradeItem(properties, PGUpgradeTypes.SETTINGS),
            new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);

    public static final Item FUEL_UPGRADE = registerUpgrade("upgrade_fuel", properties -> new UpgradeItem(properties, PGUpgradeTypes.MAX_FUEL),
            new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);

    public static final Item BIOME_LOC_UPGRADE = registerUpgrade("upgrade_biome_locator", properties -> new UpgradeItem(properties, PGUpgradeTypes.BIOME_LOC),
            new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);

    public static final Item PLAYER_LOC_UPGRADE = registerUpgrade("upgrade_player_locator", properties -> new UpgradeItem(properties, PGUpgradeTypes.PLAYER_LOC),
            new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);

    public static final Item STRUCTURE_LOC_UPGRADE = registerUpgrade("upgrade_structure_locator", properties -> new UpgradeItem(properties, PGUpgradeTypes.STRUCTURE_LOC),
            new Item.Properties(), PGCreativeModeTabs.INGREDIENTS);

    public static UpgradeItem getItemFromType(UpgradeType type) {
        return UPGRADES.stream().filter(item -> item.getUpgradeType().equals(type)).findFirst().orElseThrow();
    }

    private static Item registerGun(String name) {
        return registerGun(name, 2, Color.GREEN, PGCreativeModeTabs.TOOLS_AND_UTILITIES);
    }

    private static Item registerGun(String name, int tints, Color color, ResourceKey<CreativeModeTab> tab) {
        Item item = register(name, properties -> new PortalGunItem(properties, tints), gunProperties(color), tab);
        PORTAL_GUNS.add((PortalGunItem) item);
        return item;
    }

    public static Item.Properties gunProperties(Color color) {
        return new Item.Properties().stacksTo(1);
    }

    private static Item registerFluid(String name) {
        return register(name, PortalFluidItem::new, new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).food(PGFoods.PORTAL_FLUID),
                PGCreativeModeTabs.FOOD_AND_DRINKS);
    }

    private static Item registerUpgrade(String name, Function<Item.Properties, Item> factory, Item.Properties properties, ResourceKey<CreativeModeTab> tab) {
        UpgradeItem item = (UpgradeItem) register(name, factory, properties, tab);
        UPGRADES.add(item);
        return item;
    }

    private static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties, ResourceKey<CreativeModeTab> tab) {
        Item item = factory.apply(properties);
        TABS.put(item, tab);
        ALL.put(PGHelper.id(name), item);
        return item;
    }

    public static void init(BiConsumer<Item, ResourceLocation> r) {
        for (var e : ALL.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    private static ResourceKey<Item> keyOf(String id) {
        return ResourceKey.create(Registries.ITEM, PGHelper.id(id));
    }
}
