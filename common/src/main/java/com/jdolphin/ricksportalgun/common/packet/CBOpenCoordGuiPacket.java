package com.jdolphin.ricksportalgun.common.packet;

import com.jdolphin.ricksportalgun.client.screen.portalgun.CoordTravelScreen;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public class CBOpenCoordGuiPacket implements CustomPacketPayload {
    public List<String> strings;
    public static final Type<CBOpenCoordGuiPacket> ID = new Type<>(PGHelper.createLocation("open_client_coord_menu"));
    public static final StreamCodec<ByteBuf, CBOpenCoordGuiPacket> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
            CBOpenCoordGuiPacket::getSuggestions, CBOpenCoordGuiPacket::new);

    public CBOpenCoordGuiPacket(List<String> strings) {
        this.strings = strings;
    }

    public List<String> getSuggestions() {
        return strings;
    }

    public CBOpenCoordGuiPacket(FriendlyByteBuf buf) {
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
