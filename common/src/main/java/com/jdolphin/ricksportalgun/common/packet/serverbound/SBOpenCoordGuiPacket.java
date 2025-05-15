package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.packet.clientbound.CBOpenCoordGuiPacket;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public record SBOpenCoordGuiPacket() implements PGPayload {
    public static final StreamCodec<FriendlyByteBuf, SBOpenCoordGuiPacket> CODEC = StreamCodec.unit(new SBOpenCoordGuiPacket());
    public static final Type<SBOpenCoordGuiPacket> ID = new Type<>(PGHelper.createLocation("open_coord_menu"));

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        PGHelper.sendPacketToClient(player, new CBOpenCoordGuiPacket(LevelHelper.getDimensionsAsString(server.getAllLevels())));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
