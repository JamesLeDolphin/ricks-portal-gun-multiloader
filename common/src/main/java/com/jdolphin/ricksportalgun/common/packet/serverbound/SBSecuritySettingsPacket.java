package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record SBSecuritySettingsPacket(boolean lock, String name, String code, boolean selfDestruct) implements PGPayload {
    public static final StreamCodec<FriendlyByteBuf, SBSecuritySettingsPacket> CODEC;
    public static final Type<SBSecuritySettingsPacket> ID = new Type<>(PGHelper.createLocation("settings"));

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;

        InteractionHand hand = player.getUsedItemHand();
        ItemStack stack = player.getItemInHand(hand);

        stack.set(PGDataComponents.LOCK, lock);
        stack.set(PGDataComponents.SELF_DESTRUCT, selfDestruct);

        if (!code.isEmpty()) {
            PortalGunItem.setCode(stack, this.code);
        }

        if (!name.isEmpty()) {
            Player newOwner = server.getPlayerList().getPlayerByName(name);
            if (newOwner != null) {
                stack.set(PGDataComponents.OWNER, newOwner.getStringUUID());
            } else {
                PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.player.not_found", name));
            }
        }
        PGHelper.sendSuccessMsg(player, "notice.ricksportalgun.settings.applied");
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    static {
        CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, SBSecuritySettingsPacket::lock,
                ByteBufCodecs.STRING_UTF8, SBSecuritySettingsPacket::name,
                ByteBufCodecs.STRING_UTF8, SBSecuritySettingsPacket::code,
                ByteBufCodecs.BOOL, SBSecuritySettingsPacket::selfDestruct,
                SBSecuritySettingsPacket::new);
    }
}
