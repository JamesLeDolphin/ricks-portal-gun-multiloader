package com.jdolphin.ricksportalgun.common.packet;

import com.jdolphin.ricksportalgun.client.gui.portalgun.CoordTravelScreen;
import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public record CBOpenGuiPacket(List<String> strings) implements CustomPacketPayload {
    public static final Type<CBOpenGuiPacket> ID = new Type<>(Helper.createLocation("open_client_menu"));
    public static final StreamCodec<ByteBuf, CBOpenGuiPacket> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
            CBOpenGuiPacket::strings, CBOpenGuiPacket::new);

    public CBOpenGuiPacket(List<String> strings) {
        this.strings = strings;
    }

    public CBOpenGuiPacket(FriendlyByteBuf buf) {
        this(buf.readList(ByteBufCodecs.STRING_UTF8));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeCollection(this.strings, ByteBufCodecs.STRING_UTF8);
    }

    public void handle(Minecraft client) {
        client.setScreen(new CoordTravelScreen(strings));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
