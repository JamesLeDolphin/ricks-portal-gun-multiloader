package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class PGDataComponents {
    public static DataComponentType<BlockPos> PORTAL_POS;
    public static DataComponentType<ResourceLocation> PORTAL_DIM;
    public static DataComponentType<Integer> DEFAULT_COLOUR;
    public static DataComponentType<Boolean> BOOTLEG;
    public static DataComponentType<Integer> PORTAL_COLOUR;
    public static DataComponentType<Integer> FUEL;
    public static DataComponentType<Integer> MAX_FUEL;
    public static DataComponentType<Boolean> LOCK;
    public static DataComponentType<String> OWNER;
    public static DataComponentType<List<Waypoint>> WAYPOINTS;
    public static DataComponentType<PortalGunType> PORTAL_GUN_TYPE;
    public static DataComponentType<Integer> PRIMARY_DYE;
    public static DataComponentType<Integer> SECONDARY_DYE;
    public static DataComponentType<Float> PORTAL_SIZE;
    public static DataComponentType<String> CODE;
}
