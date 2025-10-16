package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.menu.workbench.WaypointTransferMenu;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record SBWorkbenchWaypointEditPackage(Waypoint waypoint, boolean leftSide, boolean copy, boolean delete) implements PGServerPayload {

    public static SBWorkbenchWaypointEditPackage decode(FriendlyByteBuf buf) {
        return new SBWorkbenchWaypointEditPackage(Waypoint.getWaypoint(buf.readUtf()), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(waypoint.getWaypointString()).writeBoolean(leftSide).writeBoolean(copy).writeBoolean(delete);
    }

    @Override
    public ResourceLocation getId() {
        return getID();
    }

    public static ResourceLocation getID() {
        return PGHelper.id("manage_waypoint_workbench");
    }

    @Override
    public void handle(ServerPlayer player) {
        if (player.containerMenu instanceof WaypointTransferMenu waypointMenu) {
            waypointMenu.editWaypoints(waypoint, leftSide, copy, delete);
        }
    }
}
