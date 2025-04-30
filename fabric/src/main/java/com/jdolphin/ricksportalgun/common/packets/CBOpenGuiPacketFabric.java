package com.jdolphin.ricksportalgun.common.packets;

import com.jdolphin.ricksportalgun.client.screen.SubetherBarrierScreen;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record CBOpenGuiPacketFabric(BlockPos pos) implements CustomPacketPayload {
    public static final Type<CBOpenGuiPacketFabric> ID = new Type<>(PGHelper.createLocation("open_barrier_menu"));
    public static final StreamCodec<ByteBuf, CBOpenGuiPacketFabric> CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC,
            CBOpenGuiPacketFabric::pos, CBOpenGuiPacketFabric::new);

    public CBOpenGuiPacketFabric(BlockPos pos) {
        this.pos = pos;
    }

    @Environment(EnvType.CLIENT)
    public void handle(Minecraft client) {
        client.setScreen(new SubetherBarrierScreen(this.pos));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
