package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record SBSetPortalGunStylePacket(PortalGunStyle style) implements PGServerPayload {

    @Override
    public void handle(ServerPlayer player) {
        ItemStack stack = player.getItemInHand(PGHelper.getPortalGunHand(player));
        PortalGunItem.setStyle(stack, style);
    }

    public static SBSetPortalGunStylePacket decode(FriendlyByteBuf buf) {
        return new SBSetPortalGunStylePacket(PortalGunStyle.fromNetwork(buf));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        style.toNetwork(buf);
    }

    public static ResourceLocation getID() {
        return PGHelper.id("portal_gun_style");
    }
}
