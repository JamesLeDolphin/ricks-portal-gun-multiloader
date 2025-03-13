package com.jdolphin.ricksportalgun.common.packets;

import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public record SBOpenCoordGuiPacketFabric() implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, SBOpenCoordGuiPacketFabric> CODEC = StreamCodec.unit(new SBOpenCoordGuiPacketFabric());
    public static final Type<SBOpenCoordGuiPacketFabric> ID = new Type<>(PGHelper.createLocation("open_coord_menu"));

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;

        PGHelper.sendPacketToClient(player, new CBOpenCoordGuiPacketFabric(LevelHelper.getDimensionsAsString(server.getAllLevels())));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
