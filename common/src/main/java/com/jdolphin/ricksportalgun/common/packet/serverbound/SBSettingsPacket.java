package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
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

public record SBSettingsPacket(boolean lock, String name, float size, int lifetime) implements PGPayload {
    public static final StreamCodec<FriendlyByteBuf, SBSettingsPacket> CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, SBSettingsPacket::lock,
            ByteBufCodecs.STRING_UTF8, SBSettingsPacket::name, ByteBufCodecs.FLOAT, SBSettingsPacket::size, ByteBufCodecs.INT, SBSettingsPacket::lifetime, SBSettingsPacket::new);
    public static final Type<SBSettingsPacket> ID = new Type<>(PGHelper.createLocation("settings"));

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;

        ItemStack stack = player.getMainHandItem();
        stack.set(PGDataComponents.LOCK, lock);
        stack.set(PGDataComponents.PORTAL_SIZE, size);
        stack.set(PGDataComponents.PORTAL_LIFETIME, this.lifetime);

        if (!name.isEmpty()) {
            Player newOwner = server.getPlayerList().getPlayerByName(name);
            if (newOwner != null) {
                stack.set(PGDataComponents.OWNER, newOwner.getStringUUID());
            } else {
                PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.player.not_found", name));
            }
            PGHelper.sendSuccessMsg(player, "notice.ricksportalgun.settings.applied");
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
