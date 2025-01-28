package com.jdolphin.ricksportalgun.common.packet;

import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.item.IWaypointStorage;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helper.Helper;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public record SBManageWaypointsPacket(String waypoint, boolean remove) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, SBManageWaypointsPacket> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, SBManageWaypointsPacket::waypoint,
            ByteBufCodecs.BOOL, SBManageWaypointsPacket::remove, SBManageWaypointsPacket::new);
    public static final Type<SBManageWaypointsPacket> ID = new Type<>(Helper.createLocation("manage_waypoint"));

    public SBManageWaypointsPacket(String waypoint, boolean remove) {
        this.waypoint = waypoint;
        this.remove = remove;
    }

    public SBManageWaypointsPacket(FriendlyByteBuf buf) {
        this(buf.readUtf(), buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.waypoint);
        buf.writeBoolean(this.remove);
    }

    public void handle(ServerPlayer player) {
        Waypoint wp = Waypoint.getWaypoint(waypoint);
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.is(PGTags.Items.PORTAL_GUNS)) {
            if (wp != null) {
                System.out.println("Not null in packet");
                if (!remove) IWaypointStorage.addWaypoint(stack, wp);
                if (remove) IWaypointStorage.deleteWaypoint(stack, wp);
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
