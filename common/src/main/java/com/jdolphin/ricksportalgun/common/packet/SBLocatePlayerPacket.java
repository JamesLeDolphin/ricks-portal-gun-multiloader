package com.jdolphin.ricksportalgun.common.packet;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helper.Helper;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import io.netty.buffer.ByteBuf;
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


public record SBLocatePlayerPacket(String name) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, SBLocatePlayerPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SBLocatePlayerPacket::name, SBLocatePlayerPacket::new);
    public static final Type<SBLocatePlayerPacket> ID = new Type<>(Helper.createLocation("locate"));

    public SBLocatePlayerPacket(String name) {
        this.name = name;
    }

    public SBLocatePlayerPacket(FriendlyByteBuf buf) {
        this(buf.readUtf());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.name);
    }

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;

        ServerLevel world = player.serverLevel();
        if(true /*TODO: figure out proper configs*/) {
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
