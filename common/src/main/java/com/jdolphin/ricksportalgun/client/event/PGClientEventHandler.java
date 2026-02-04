package com.jdolphin.ricksportalgun.client.event;

import com.jdolphin.ricksportalgun.common.init.PGKeyBinds;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBOpenCoordGuiPacket;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PGClientEventHandler {

    public static void onClientTick(Minecraft minecraft) {
        Player player = minecraft.player;
        if (player != null) {
            ItemStack stack = player.getItemInHand(PGHelper.getPortalGunHand(player));
            if (PGKeyBinds.KEY_PORTAL_MENU.isDown() && minecraft.player != null && stack.is(PGTags.Items.PORTAL_GUNS)) {
                SBOpenCoordGuiPacket packet = new SBOpenCoordGuiPacket();
                PGHelper.sendPacketToServer(packet);
            }
        }
    }
}
