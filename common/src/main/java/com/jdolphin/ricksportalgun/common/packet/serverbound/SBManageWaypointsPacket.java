package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.item.IWaypointStorage;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public record SBManageWaypointsPacket(String waypoint, boolean remove) implements PGPayload {
    public static final StreamCodec<FriendlyByteBuf, SBManageWaypointsPacket> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, SBManageWaypointsPacket::waypoint,
            ByteBufCodecs.BOOL, SBManageWaypointsPacket::remove, SBManageWaypointsPacket::new);

    public static final Type<SBManageWaypointsPacket> ID = new Type<>(PGHelper.createLocation("manage_waypoint"));


    public void handle(ServerPlayer player) {
        InteractionHand hand = player.getUsedItemHand();
        ItemStack stack = player.getItemInHand(hand);

        Waypoint wp = Waypoint.getWaypoint(waypoint);
        if (stack.getItem() instanceof IWaypointStorage) {
            if (wp != null) {
                if (!remove) {
                    IWaypointStorage.addWaypoint(stack, wp);
                }
                if (remove) {
                    IWaypointStorage.deleteWaypoint(stack, wp);
                    PGHelper.sendSuccessMsg(player, Component.translatable("ricksportalgun.deleted", wp.getName()));
                }
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
