package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.config.PGCommonConfig;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;


public record SBLocatePlayerPacket(String name) implements PGPayload {
    public static final StreamCodec<FriendlyByteBuf, SBLocatePlayerPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SBLocatePlayerPacket::name, SBLocatePlayerPacket::new);
    public static final Type<SBLocatePlayerPacket> ID = new Type<>(PGHelper.createLocation("locate"));

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;

        ServerLevel world = player.serverLevel();
        if (PGCommonConfig.INSTANCE.disableLocating()) {
            player.sendSystemMessage(Component.translatable("error.ricksportalgun.player_locating_disabled").withStyle(ChatFormatting.RED), false);
            return;
        }

        ServerPlayer targetPlayer = server.getPlayerList().getPlayerByName(name);
        if (targetPlayer != null) {
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            PortalGunItem item = PGHelper.getPortalGun(stack);
            item.setHopLocation(stack, LevelHelper.getPlayerDimensionLocation(targetPlayer), targetPlayer.blockPosition());
            player.sendSystemMessage(Component.translatable("notice.ricksportalgun.destination.set").withStyle(ChatFormatting.GREEN), false);

        } else player.sendSystemMessage(Component.translatable("error.ricksportalgun.player_not_found", name).withStyle(ChatFormatting.RED), false);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
