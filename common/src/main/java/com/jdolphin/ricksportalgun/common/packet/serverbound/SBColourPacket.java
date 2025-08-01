package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record SBColourPacket(int colour) implements PGPayload {
    public static final StreamCodec<FriendlyByteBuf, SBColourPacket> CODEC = StreamCodec.composite(ByteBufCodecs.INT, SBColourPacket::colour, SBColourPacket::new);
    public static final Type<SBColourPacket> ID = new Type<>(PGHelper.id("color"));

    public void handle(ServerPlayer player) {
        InteractionHand hand = PGHelper.getPortalGunHand(player);
        ItemStack stack = player.getItemInHand(hand);
        PortalGunItem.setColor(stack, this.colour);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
