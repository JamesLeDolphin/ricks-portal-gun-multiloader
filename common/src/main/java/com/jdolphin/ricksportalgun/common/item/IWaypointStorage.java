package com.jdolphin.ricksportalgun.common.item;

import com.jdolphin.ricksportalgun.common.util.Waypoint;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static com.jdolphin.ricksportalgun.common.init.PGNbtKeys.TAG_WAYPOINTS;

public interface IWaypointStorage {

    static List<Waypoint> getWaypoints(ItemStack itemStack) {
        return itemStack.getOrCreateTag().getList(TAG_WAYPOINTS, Tag.TAG_STRING).stream().map(Tag::getAsString).map(Waypoint::getWaypoint).toList();
    }

    static void addWaypoint(ItemStack stack, Waypoint waypoint) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag listtag = tag.getList(TAG_WAYPOINTS, Tag.TAG_STRING);
        listtag.add(StringTag.valueOf(waypoint.getWaypointString()));
        tag.put(TAG_WAYPOINTS, listtag);
    }

    static void deleteWaypoint(ItemStack stack, Waypoint waypoint) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag listtag = tag.getList(TAG_WAYPOINTS, Tag.TAG_STRING);
        listtag.remove(StringTag.valueOf(waypoint.getWaypointString()));
        tag.put(TAG_WAYPOINTS, listtag);
    }

}
