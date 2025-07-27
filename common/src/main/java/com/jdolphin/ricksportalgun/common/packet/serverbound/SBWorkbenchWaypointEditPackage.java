package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.menu.workbench.WaypointTransferMenu;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record SBWorkbenchWaypointEditPackage(Waypoint waypoint, boolean leftSide, boolean copy, boolean delete) implements PGPayload {
    public static final StreamCodec<FriendlyByteBuf, SBWorkbenchWaypointEditPackage> CODEC;
    public static final Type<SBWorkbenchWaypointEditPackage> ID = new Type<>(PGHelper.id("manage_waypoint_workbench"));

    @Override
    public void handle(ServerPlayer player) {
        if (player.containerMenu instanceof WaypointTransferMenu waypointMenu) {
            waypointMenu.editWaypoints(waypoint, leftSide, copy, delete);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    static {
        CODEC = StreamCodec.composite(Waypoint.PACKET_CODEC, SBWorkbenchWaypointEditPackage::waypoint,
                ByteBufCodecs.BOOL, SBWorkbenchWaypointEditPackage::leftSide, ByteBufCodecs.BOOL, SBWorkbenchWaypointEditPackage::copy,
                ByteBufCodecs.BOOL, SBWorkbenchWaypointEditPackage::delete, SBWorkbenchWaypointEditPackage::new);
    }
}
