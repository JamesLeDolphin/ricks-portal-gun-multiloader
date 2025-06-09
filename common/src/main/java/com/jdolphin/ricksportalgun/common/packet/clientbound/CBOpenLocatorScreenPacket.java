package com.jdolphin.ricksportalgun.common.packet.clientbound;

import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public record CBOpenLocatorScreenPacket(List<String> playerList, List<String> biomeList, List<String> structureList) implements CustomPacketPayload {

    public static final Type<CBOpenLocatorScreenPacket> ID = new Type<>(PGHelper.createLocation("open_client_locator_screen"));

    public static final StreamCodec<FriendlyByteBuf, CBOpenLocatorScreenPacket> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
            CBOpenLocatorScreenPacket::playerList, ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
            CBOpenLocatorScreenPacket::biomeList, ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
            CBOpenLocatorScreenPacket::structureList, CBOpenLocatorScreenPacket::new);



    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
