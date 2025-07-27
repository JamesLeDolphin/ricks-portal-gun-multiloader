package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public record SBCustomizeSettingsPacket(double size, int age) implements PGPayload {
    public static final StreamCodec<ByteBuf, SBCustomizeSettingsPacket> CODEC = StreamCodec.composite(ByteBufCodecs.DOUBLE,
            SBCustomizeSettingsPacket::size, ByteBufCodecs.INT, SBCustomizeSettingsPacket::age, SBCustomizeSettingsPacket::new);

    public static final Type<SBCustomizeSettingsPacket> ID = new Type<>(PGHelper.id("portal_settings"));

    @Override
    public void handle(ServerPlayer player) {
        InteractionHand hand = player.getUsedItemHand();
        ItemStack stack = player.getItemInHand(hand);

        stack.set(PGDataComponents.PORTAL_SIZE, ((float) size));
        stack.set(PGDataComponents.PORTAL_LIFETIME, this.age);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
