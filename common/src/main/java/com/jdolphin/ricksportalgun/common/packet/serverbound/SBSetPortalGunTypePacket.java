package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.menu.workbench.SkinSelectorMenu;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record SBSetPortalGunTypePacket(ResourceLocation loc, int tints) implements PGServerPayload {

    public void handle(ServerPlayer player) {
        if (player.containerMenu instanceof SkinSelectorMenu menu) {
            menu.setPortalGunType(player, loc, tints);
        }
    }

    public static SBSetPortalGunTypePacket decode(FriendlyByteBuf buf) {
        return new SBSetPortalGunTypePacket(buf.readResourceLocation(), buf.readInt());
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(loc).writeInt(tints);
    }

    public static ResourceLocation getID() {
        return PGHelper.id("set_portalgun_type");
    }
}
