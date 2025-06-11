package com.jdolphin.ricksportalgun.common.packet.clientbound;

import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public record CBOpenSecurityGuiPacket(List<String> strings) implements CustomPacketPayload {
    public static final Type<CBOpenSecurityGuiPacket> ID = new Type<>(PGHelper.createLocation("open_client_security_screen"));
    public static final StreamCodec<FriendlyByteBuf, CBOpenSecurityGuiPacket> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
            CBOpenSecurityGuiPacket::strings, CBOpenSecurityGuiPacket::new);


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
