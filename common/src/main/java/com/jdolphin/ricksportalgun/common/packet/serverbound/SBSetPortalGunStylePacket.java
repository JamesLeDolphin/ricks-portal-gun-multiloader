package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public record SBSetPortalGunStylePacket(PortalGunStyle style) implements PGPayload {
    public static final StreamCodec<FriendlyByteBuf, SBSetPortalGunStylePacket> CODEC = StreamCodec.composite(PortalGunStyle.PACKET_CODEC, SBSetPortalGunStylePacket::style, SBSetPortalGunStylePacket::new);
    public static final Type<SBSetPortalGunStylePacket> ID = new Type<>(PGHelper.createLocation("portal_gun_style"));

    @Override
    public void handle(ServerPlayer player) {
        InteractionHand hand = player.getUsedItemHand();
        ItemStack stack = player.getItemInHand(hand);
        PortalGunItem.setStyle(stack, style);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
