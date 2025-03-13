package com.jdolphin.ricksportalgun.common.packet;

import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public record CBSyncDimensionListPacket(List<String> dimensions) implements CustomPacketPayload {
    public static final Type<CBSyncDimensionListPacket> ID = new Type<>(PGHelper.createLocation("sync_dimensions"));
    public static final StreamCodec<ByteBuf, CBSyncDimensionListPacket> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
            CBSyncDimensionListPacket::dimensions, CBSyncDimensionListPacket::new);

    public void handle() {
        LevelHelper.CLIENT_DIMENSIONS = dimensions;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
