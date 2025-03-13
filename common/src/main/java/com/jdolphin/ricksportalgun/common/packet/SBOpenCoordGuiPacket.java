package com.jdolphin.ricksportalgun.common.packet;

import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public record SBOpenCoordGuiPacket(String playerUUID) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, SBOpenCoordGuiPacket> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, SBOpenCoordGuiPacket::playerUUID, SBOpenCoordGuiPacket::new);
    public static final Type<SBOpenCoordGuiPacket> ID = new Type<>(PGHelper.createLocation("open_coord_menu"));

    public SBOpenCoordGuiPacket(String playerUUID) {
        this.playerUUID = playerUUID;
    }

    public SBOpenCoordGuiPacket(FriendlyByteBuf buf) {
        this(buf.readUtf());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.playerUUID);
    }

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        PGHelper.sendPacketToClient(player, new CBOpenCoordGuiPacket(LevelHelper.getDimensionsAsString(server.getAllLevels())));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
