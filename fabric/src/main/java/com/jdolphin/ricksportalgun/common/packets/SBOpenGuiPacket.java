package com.jdolphin.ricksportalgun.common.packets;

import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public record SBOpenGuiPacket(String playerUUID) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, SBOpenGuiPacket> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, SBOpenGuiPacket::playerUUID, SBOpenGuiPacket::new);
    public static final CustomPacketPayload.Type<SBOpenGuiPacket> ID = new CustomPacketPayload.Type<>(Helper.createLocation("open_menu"));

    public void handle(ServerPlayNetworking.Context context) {
        ServerPlayer player = context.player();
        MinecraftServer server = player.server;
        Iterable<ServerLevel> worlds = server.getAllLevels();
        List<String> worldList = new ArrayList<>();
        worlds.forEach(world -> {
            ResourceLocation worldKey = world.dimension().location();
            if (worldKey != null) {
                String s = worldKey.toString();
                if (!s.isEmpty()) worldList.add(s);
            }
        });
        ServerPlayNetworking.send(player, new CBOpenGuiPacket(worldList));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
