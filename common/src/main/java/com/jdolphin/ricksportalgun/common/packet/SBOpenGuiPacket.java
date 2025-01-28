package com.jdolphin.ricksportalgun.common.packet;

import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
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
    public static final Type<SBOpenGuiPacket> ID = new Type<>(Helper.createLocation("open_menu"));

    public SBOpenGuiPacket(String playerUUID) {
        this.playerUUID = playerUUID;
    }

    public SBOpenGuiPacket(FriendlyByteBuf buf) {
        this(buf.readUtf());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.playerUUID);
    }

    public void handle(ServerPlayer player) {
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

        Helper.sendPacketToClient(player, new CBOpenGuiPacket(worldList));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
