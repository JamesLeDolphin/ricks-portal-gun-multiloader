package com.jdolphin.ricksportalgun.common.packet;

import com.jdolphin.ricksportalgun.common.blockentity.SubetherBarrierBlockEntity;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public record SBSetBarrierCodePacket(String code, BlockPos pos) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, SBSetBarrierCodePacket> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8,
            SBSetBarrierCodePacket::code, BlockPos.STREAM_CODEC, SBSetBarrierCodePacket::pos, SBSetBarrierCodePacket::new);
    public static final Type<SBSetBarrierCodePacket> ID = new Type<>(PGHelper.createLocation("barrier_code"));

    public void handle(ServerPlayer player) {
        Level level = player.serverLevel();
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof SubetherBarrierBlockEntity barrier) {
            barrier.setCode(code);
            player.sendSystemMessage(Component.translatable("notice.ricksportalgun.barrier.code_set"));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
