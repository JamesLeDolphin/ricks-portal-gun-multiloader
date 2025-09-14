package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.packet.clientbound.CBOpenSecurityGuiPacket;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Arrays;
import java.util.List;

public record SBOpenSecuritySettingsPacket() implements PGServerPayload {

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

    public static SBOpenSecuritySettingsPacket decode(FriendlyByteBuf buf) {
        return new SBOpenSecuritySettingsPacket();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {}

    public static ResourceLocation getID() {
        return PGHelper.id("open_security_screen");
    }
}
