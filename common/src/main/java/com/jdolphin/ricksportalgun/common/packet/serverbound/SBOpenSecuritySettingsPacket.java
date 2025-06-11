package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.packet.clientbound.CBOpenSecurityGuiPacket;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Arrays;
import java.util.List;

public record SBOpenSecuritySettingsPacket() implements PGPayload {
    public static final StreamCodec<FriendlyByteBuf, SBOpenSecuritySettingsPacket> CODEC = StreamCodec.unit(new SBOpenSecuritySettingsPacket());
    public static final Type<SBOpenSecuritySettingsPacket> ID = new Type<>(PGHelper.createLocation("open_security_screen"));

    @Override
    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        List<String> players = Arrays.asList(server.getPlayerNames());
        int playerCount = server.getPlayerNames().length;
        if (playerCount == players.size()) {
            CBOpenSecurityGuiPacket packet = new CBOpenSecurityGuiPacket(players);
            PGHelper.sendPacketToClient(player, packet);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
