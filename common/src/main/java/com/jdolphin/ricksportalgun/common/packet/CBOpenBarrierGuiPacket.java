package com.jdolphin.ricksportalgun.common.packet;

import com.jdolphin.ricksportalgun.client.screen.SubetherBarrierScreen;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record CBOpenBarrierGuiPacket(BlockPos pos) implements CustomPacketPayload {
    public static final Type<CBOpenCoordGuiPacket> ID = new Type<>(PGHelper.createLocation("open_menu"));
    public static final StreamCodec<ByteBuf, CBOpenBarrierGuiPacket> CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC,
            CBOpenBarrierGuiPacket::pos, CBOpenBarrierGuiPacket::new);

    public CBOpenBarrierGuiPacket(BlockPos pos) {
        this.pos = pos;
    }


    public void handle(Minecraft client) {
        client.setScreen(new SubetherBarrierScreen(this.pos));
    }

    public CBOpenBarrierGuiPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
