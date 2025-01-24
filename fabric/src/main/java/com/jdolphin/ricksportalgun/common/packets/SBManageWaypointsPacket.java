package com.jdolphin.ricksportalgun.common.packets;

import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public record SBManageWaypointsPacket(String waypoint, boolean remove) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, SBManageWaypointsPacket> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, SBManageWaypointsPacket::waypoint,
            ByteBufCodecs.BOOL, SBManageWaypointsPacket::remove, SBManageWaypointsPacket::new);
    public static final CustomPacketPayload.Type<SBManageWaypointsPacket> ID = new CustomPacketPayload.Type<>(Helper.createLocation("manage_waypoint"));



    public void handle(ServerPlayNetworking.Context context) {
        ServerPlayer player = context.player();
        Waypoint wp = Waypoint.getWaypoint(waypoint);
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.is(PGTags.Items.PORTAL_GUNS)) {
            PortalGunItem item = (PortalGunItem) stack.getItem();
            if (wp != null) {
                if (!remove) item.addWaypoint(stack, wp);

                if (remove) item.deleteWaypoint(stack, wp);
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
