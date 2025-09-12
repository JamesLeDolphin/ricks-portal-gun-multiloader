package com.jdolphin.ricksportalgun.common.packet.clientbound;

import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public record CBOpenCoordGuiPacket(List<String> strings) implements CustomPacketPayload {
    public static final Type<CBOpenCoordGuiPacket> ID = new Type<>(PGHelper.id("open_client_coord_menu"));
    public static final StreamCodec<FriendlyByteBuf, CBOpenCoordGuiPacket> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
            CBOpenCoordGuiPacket::strings, CBOpenCoordGuiPacket::new);


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
