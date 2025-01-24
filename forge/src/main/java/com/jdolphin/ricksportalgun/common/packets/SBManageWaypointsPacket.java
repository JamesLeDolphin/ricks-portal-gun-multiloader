package com.jdolphin.ricksportalgun.common.packets;

import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class SBManageWaypointsPacket {
    protected Waypoint wp;
    protected boolean remove;

    public SBManageWaypointsPacket(String waypoint, boolean remove) {
        this.wp = Waypoint.getWaypoint(waypoint);
        this.remove = remove;
    }

    public SBManageWaypointsPacket(FriendlyByteBuf buf) {
        this.wp = Waypoint.getWaypoint(buf.readUtf());
        this.remove = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.wp.getWaypointString());
        buf.writeBoolean(this.remove);
    }

    public boolean handle(CustomPayloadEvent.Context context) {
        ServerPlayer player = context.getSender();
        try {
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (stack.is(PGTags.Items.PORTAL_GUNS)) {
                PortalGunItem item = (PortalGunItem) stack.getItem();
                if (this.remove) {
                    item.deleteWaypoint(stack, this.wp);
                    System.out.println("removed");
                } else {
                    item.addWaypoint(stack, this.wp);
                    System.out.println("added");
                }
            }
            return true;
        } catch (NullPointerException err) {
            err.printStackTrace();
        } return false;
    }
}