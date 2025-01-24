package com.jdolphin.ricksportalgun.common.packets;

import com.jdolphin.ricksportalgun.common.config.Config;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import com.jdolphin.ricksportalgun.common.util.helpers.LevelHelper;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;


public record SBLocatePlayerPacket(String name) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, SBLocatePlayerPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SBLocatePlayerPacket::name, SBLocatePlayerPacket::new);
    public static final CustomPacketPayload.Type<SBLocatePlayerPacket> ID = new CustomPacketPayload.Type<>(Helper.createLocation("locate"));

    public void handle(ServerPlayNetworking.Context context) {
        ServerPlayer player = context.player();
        MinecraftServer server = context.server();

        ServerLevel world = player.serverLevel();
        if(!Config.getInstance().ALLOW_PLAYER_LOCATING) {
            //TODO: use translatable strings for these messages
            player.sendSystemMessage(Component.literal("Error 403: Player locating not allowed in this world").withStyle(ChatFormatting.RED), false);
            return;
        }

        ServerPlayer targetPlayer = server.getPlayerList().getPlayerByName(name);
        if (targetPlayer != null) {
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            PortalGunItem item = Helper.getPortalGun(stack);
            item.setHopLocation(stack, LevelHelper.getPlayerDimensionLocation(targetPlayer), targetPlayer.blockPosition());
            player.sendSystemMessage(Component.literal("Coordinates set!").withStyle(ChatFormatting.GREEN), false);

        } else player.sendSystemMessage(Component.literal("Error 404: Player not found").withStyle(ChatFormatting.RED), false);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
