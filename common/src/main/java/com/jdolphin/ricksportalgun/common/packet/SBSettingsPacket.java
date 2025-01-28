package com.jdolphin.ricksportalgun.common.packet;

import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.util.helper.Helper;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record SBSettingsPacket(boolean lock, String name) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, SBSettingsPacket> CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, SBSettingsPacket::lock,
            ByteBufCodecs.STRING_UTF8, SBSettingsPacket::name, SBSettingsPacket::new);
    public static final Type<SBSettingsPacket> ID = new Type<>(Helper.createLocation("settings"));

    public SBSettingsPacket(boolean lock, String name) {
        this.lock = lock;
        this.name = name;
    }

    public SBSettingsPacket(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readUtf());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.lock);
        buf.writeUtf(this.name);
    }

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;

        ItemStack stack = player.getMainHandItem();
        Player newOwner = server.getPlayerList().getPlayerByName(name);
        if (newOwner != null) {
            stack.set(PGDataComponents.OWNER, newOwner.getStringUUID());
            stack.set(PGDataComponents.LOCK, lock);
            player.sendSystemMessage(Component.literal("Applied new portal gun settings").withStyle(ChatFormatting.GREEN), false);
        } else player.sendSystemMessage(Component.literal("Error 404: Player not found").withStyle(ChatFormatting.RED), false);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
