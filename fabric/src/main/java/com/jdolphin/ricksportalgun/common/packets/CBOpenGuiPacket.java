package com.jdolphin.ricksportalgun.common.packets;

import com.jdolphin.ricksportalgun.client.gui.portalgun.CoordTravelScreen;
import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public record CBOpenGuiPacket(List<String> strings) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CBOpenGuiPacket> ID = new CustomPacketPayload.Type<>(Helper.createLocation("open_client_menu"));
    public static final StreamCodec<ByteBuf, CBOpenGuiPacket> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
            CBOpenGuiPacket::strings, CBOpenGuiPacket::new);

    public void handle(ClientPlayNetworking.Context context) {
        Minecraft client = context.client();

        client.setScreen(new CoordTravelScreen(strings));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
