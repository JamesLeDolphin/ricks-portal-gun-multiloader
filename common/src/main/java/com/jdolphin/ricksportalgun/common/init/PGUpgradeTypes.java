package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.item.types.ComponentUpgrade;
import com.jdolphin.ricksportalgun.common.item.types.CreativeUpgrade;
import com.jdolphin.ricksportalgun.common.item.types.PreConditionUpgrade;
import com.jdolphin.ricksportalgun.common.item.types.UpgradeType;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;

import java.util.HashMap;
import java.util.Map;

public class PGUpgradeTypes {
    public static final Map<ResourceLocation, UpgradeType> UPGRADE_TYPES = new HashMap<>();

    public static final UpgradeType SETTINGS = registerSimple("settings", Component.translatable("tooltip.ricksportalgun.upgrade.settings"));
    public static final UpgradeType WAYPOINTS = registerSimple("waypoints", Component.translatable("tooltip.ricksportalgun.upgrade.waypoints"));
    public static final UpgradeType MAX_FUEL = registerComponent("max_fuel", Component.translatable("tooltip.ricksportalgun.upgrade.max_fuel"), PGDataComponents.MAX_FUEL, 128);
    public static final UpgradeType CREATIVE = registerCreative("creative", Component.translatable("tooltip.ricksportalgun.upgrade.creative"));
    public static final UpgradeType DURABILITY = registerComponent("durability", Component.translatable("tooltip.ricksportalgun.upgrade.durability"), DataComponents.FIRE_RESISTANT, Unit.INSTANCE);

    public static final UpgradeType BIOME_LOC = registerSimple("biome_loc", Component.translatable("tooltip.ricksportalgun.upgrade.biome_loc"));
    public static final UpgradeType PLAYER_LOC = registerPreCondition("player_loc", Component.translatable("tooltip.ricksportalgun.upgrade.player_loc"), "biome_loc");
    public static final UpgradeType STRUCTURE_LOC = registerPreCondition("structure_loc", Component.translatable("tooltip.ricksportalgun.upgrade.structure_loc"), "player_loc");

    public static final UpgradeType DIM_1 = registerSimple("dim_1", Component.translatable("tooltip.ricksportalgun.upgrade.dim_1"));
    public static final UpgradeType DIM_2 = registerPreCondition("dim_2", Component.translatable("tooltip.ricksportalgun.upgrade.dim_2"), "dim_1");

    private static UpgradeType registerCreative(String name, Component desc) {
        CreativeUpgrade type = new CreativeUpgrade(name, desc);
        UPGRADE_TYPES.put(PGHelper.id(name), type);
        return type;
    }

    private static <T> UpgradeType registerComponent(String name, Component desc, DataComponentType<T> componentType, T value) {
        ComponentUpgrade<T> type = new ComponentUpgrade<>(name, desc, componentType, value);
        UPGRADE_TYPES.put(PGHelper.id(name), type);
        return type;
    }

    private static UpgradeType registerSimple(String name, Component component) {
        UpgradeType type = new UpgradeType(name, component);
        UPGRADE_TYPES.put(PGHelper.id(name), type);
        return type;
    }

    private static UpgradeType registerPreCondition(String name, Component component, String condition) {
        UpgradeType type = new PreConditionUpgrade(name, component, condition);
        UPGRADE_TYPES.put(PGHelper.id(name), type);
        return type;
    }

    public static UpgradeType getFromTag(String upgradeTag) {
       return UPGRADE_TYPES.values().stream().filter(type -> type.getId().equals(upgradeTag)).findFirst().orElseThrow();
    }

    public static UpgradeType getFromId(String id) {
        return UPGRADE_TYPES.values().stream().filter(type -> type.getId().equals(id)).findFirst().orElseThrow();
    }
}