package com.jdolphin.ricksportalgun.common.item;

import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public interface IWaypointStorage {

    static List<Waypoint> getWaypoints(ItemStack stack) {
        return stack.getOrDefault(PGDataComponents.WAYPOINTS, List.of());
    }

    static void addWaypoint(ItemStack stack, Waypoint waypoint) {
        List<Waypoint> list = stack.getOrDefault(PGDataComponents.WAYPOINTS, List.of());
        ArrayList<Waypoint> waypoints = new ArrayList<>(list);
        waypoints.add(waypoint);
        stack.set(PGDataComponents.WAYPOINTS, waypoints);
    }

    static void deleteWaypoint(ItemStack stack, Waypoint waypoint) {
        List<Waypoint> list = stack.getOrDefault(PGDataComponents.WAYPOINTS, List.of());
        ArrayList<Waypoint> waypoints = new ArrayList<>(list);
        waypoints.remove(waypoint);
        stack.set(PGDataComponents.WAYPOINTS, waypoints);
    }

}
