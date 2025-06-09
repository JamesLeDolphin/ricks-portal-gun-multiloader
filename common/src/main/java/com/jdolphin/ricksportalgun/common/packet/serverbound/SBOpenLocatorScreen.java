package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class SBOpenLocatorScreen implements PGPayload {
    public static final StreamCodec<FriendlyByteBuf, SBOpenLocatorScreen> CODEC = StreamCodec.unit(new SBOpenLocatorScreen());
    public static final Type<SBOpenLocatorScreen> ID = new Type<>(PGHelper.createLocation("open_locator_screen"));

    @Override
    public void handle(ServerPlayer player) {

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
