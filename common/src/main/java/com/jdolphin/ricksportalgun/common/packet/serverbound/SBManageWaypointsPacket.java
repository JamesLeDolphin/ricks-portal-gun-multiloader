package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.item.IWaypointStorage;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record SBManageWaypointsPacket(String waypoint, boolean remove) implements PGServerPayload {

    public void handle(ServerPlayer player) {
        ItemStack stack = player.getItemInHand(PGHelper.getPortalGunHand(player));
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

    public static SBManageWaypointsPacket decode(FriendlyByteBuf buf) {
        String wp = buf.readUtf();
        boolean delete = buf.readBoolean();
        return new SBManageWaypointsPacket(wp, delete);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(waypoint);
        buf.writeBoolean(remove);
    }

    public static ResourceLocation getID() {
        return PGHelper.id("manage_waypoint");
    }
}
