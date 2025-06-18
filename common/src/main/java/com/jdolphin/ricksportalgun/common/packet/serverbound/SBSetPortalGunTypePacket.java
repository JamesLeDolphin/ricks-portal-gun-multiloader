package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.menu.workbench.SkinSelectorMenu;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record SBSetPortalGunTypePacket(PortalGunType portalGunType) implements PGPayload {
    public static final StreamCodec<FriendlyByteBuf, SBSetPortalGunTypePacket> CODEC = StreamCodec.composite(PortalGunType.PACKET_CODEC, SBSetPortalGunTypePacket::portalGunType,
            SBSetPortalGunTypePacket::new);

    public static final Type<SBSetPortalGunTypePacket> ID = new Type<>(PGHelper.createLocation("set_portal_gun_type"));

    @Override
    public void handle(ServerPlayer player) {
        if (player.containerMenu instanceof SkinSelectorMenu menu) {
            menu.setPortalGunType(player, portalGunType);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
