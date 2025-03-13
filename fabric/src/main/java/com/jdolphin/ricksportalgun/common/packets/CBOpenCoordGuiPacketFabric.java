package com.jdolphin.ricksportalgun.common.packets;

import com.jdolphin.ricksportalgun.client.screen.portalgun.CoordTravelScreen;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public class CBOpenCoordGuiPacketFabric implements CustomPacketPayload {
    public List<String> strings;
    public static final Type<CBOpenCoordGuiPacketFabric> ID = new Type<>(PGHelper.createLocation("open_client_coord_menu"));
    public static final StreamCodec<ByteBuf, CBOpenCoordGuiPacketFabric> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
            CBOpenCoordGuiPacketFabric::getSuggestions, CBOpenCoordGuiPacketFabric::new);

    public CBOpenCoordGuiPacketFabric(List<String> strings) {
        this.strings = strings;
    }

    public List<String> getSuggestions() {
        return strings;
    }

    public CBOpenCoordGuiPacketFabric(FriendlyByteBuf buf) {
        this(buf.readList(ByteBufCodecs.STRING_UTF8));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeCollection(this.strings, ByteBufCodecs.STRING_UTF8);
    }

    @Environment(EnvType.CLIENT)
    public void handle(Minecraft client) {
        client.setScreen(new CoordTravelScreen(strings));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
