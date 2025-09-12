package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.menu.workbench.SkinSelectorMenu;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record SBSetPortalGunTypePacket(ResourceLocation loc, int tints) implements PGPayload {
    public static final StreamCodec<ByteBuf, SBSetPortalGunTypePacket> CODEC = StreamCodec.composite(ResourceLocation.STREAM_CODEC, SBSetPortalGunTypePacket::loc,
            ByteBufCodecs.INT, SBSetPortalGunTypePacket::tints, SBSetPortalGunTypePacket::new);
    public static final Type<SBSetPortalGunTypePacket> ID = new Type<>(PGHelper.id("set_portalgun_type"));

    public void handle(ServerPlayer player) {
        if (player.containerMenu instanceof SkinSelectorMenu menu) {
            menu.setPortalGunType(player, loc, tints);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
