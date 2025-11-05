package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.item.upgrade.types.CreativeUpgrade;
import com.jdolphin.ricksportalgun.common.item.upgrade.types.PreConditionUpgrade;
import com.jdolphin.ricksportalgun.common.item.upgrade.types.UpgradeType;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class PGUpgradeTypes {
    public static final Map<ResourceLocation, UpgradeType> UPGRADE_TYPES = new HashMap<>();

    public static final UpgradeType SETTINGS = registerSimple("settings", Component.translatable("tooltip.ricksportalgun.upgrade.settings"), "settings");
    public static final UpgradeType WAYPOINTS = registerSimple("waypoints", Component.translatable("tooltip.ricksportalgun.upgrade.waypoints"), "waypoints");
    public static final UpgradeType MAX_FUEL = registerSimple("max_fuel", Component.translatable("tooltip.ricksportalgun.upgrade.max_fuel"), "fuel");
    public static final UpgradeType CREATIVE = registerCreative("creative", Component.translatable("tooltip.ricksportalgun.upgrade.creative"));

    public static final UpgradeType BIOME_LOC = registerSimple("biome_loc", Component.translatable("tooltip.ricksportalgun.upgrade.biome_loc"), "biome_loc");
    public static final UpgradeType PLAYER_LOC = registerPreCondition("player_loc", Component.translatable("tooltip.ricksportalgun.upgrade.player_loc"), "player_loc", "biome_loc");
    public static final UpgradeType STRUCTURE_LOC = registerPreCondition("structure_loc", Component.translatable("tooltip.ricksportalgun.upgrade.structure_loc"), "structure_loc", "player_loc");

    public static final UpgradeType DIM_1 = registerSimple("dim_1", Component.translatable("tooltip.ricksportalgun.upgrade.dim_1"), "dim_1");
    public static final UpgradeType DIM_2 = registerPreCondition("dim_2", Component.translatable("tooltip.ricksportalgun.upgrade.dim_2"), "dim_2", "dim_1");

    private static UpgradeType registerCreative(String name, Component desc) {
        ResourceLocation rl = PGHelper.id(name);
        CreativeUpgrade type = new CreativeUpgrade(rl.toString(), desc);
        UPGRADE_TYPES.put(rl, type);
        return type;
    }

    private static UpgradeType registerSimple(String name, Component component, String tag) {
        ResourceLocation rl = PGHelper.id(name);
        UpgradeType type = new UpgradeType(rl.toString(), component, tag);
        UPGRADE_TYPES.put(rl, type);
        return type;
    }

    private static UpgradeType registerPreCondition(String name, Component component, String tag, String condition) {
        ResourceLocation rl = PGHelper.id(name);
        UpgradeType type = new PreConditionUpgrade(rl.toString(), component, tag, condition);
        UPGRADE_TYPES.put(rl, type);
        return type;
    }

    public static UpgradeType getFromString(String s) {
        ResourceLocation rl = new ResourceLocation(s);
        return UPGRADE_TYPES.getOrDefault(rl, null);
    }
}
