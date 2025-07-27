package com.jdolphin.ricksportalgun.common.packet.clientbound;

import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record CBOpenBarrierGuiPacket(BlockPos pos) implements CustomPacketPayload {
    public static final Type<CBOpenBarrierGuiPacket> ID = new Type<>(PGHelper.id("open_menu"));

    public static final StreamCodec<FriendlyByteBuf, CBOpenBarrierGuiPacket> CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC,
            CBOpenBarrierGuiPacket::pos, CBOpenBarrierGuiPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
