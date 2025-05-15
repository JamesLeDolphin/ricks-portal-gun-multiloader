package com.jdolphin.ricksportalgun.common.packet.clientbound;

import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public record CBSyncGunTypesPacket(List<PortalGunType> types) implements CustomPacketPayload {
    public static final Type<CBSyncGunTypesPacket> ID = new Type<>(PGHelper.createLocation("sync_gun_types"));
    public static final StreamCodec<FriendlyByteBuf, CBSyncGunTypesPacket> CODEC = StreamCodec.composite(PortalGunType.PACKET_CODEC.apply(ByteBufCodecs.list()),
            CBSyncGunTypesPacket::types, CBSyncGunTypesPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
