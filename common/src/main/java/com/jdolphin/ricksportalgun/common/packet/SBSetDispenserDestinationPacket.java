package com.jdolphin.ricksportalgun.common.packet;

import com.jdolphin.ricksportalgun.common.config.PGCommonConfig;
import com.jdolphin.ricksportalgun.common.menu.PortalDispenserMenu;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record SBSetDispenserDestinationPacket(BlockPos pos, String dim, int id) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, SBSetDispenserDestinationPacket> CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, SBSetDispenserDestinationPacket::pos,
            ByteBufCodecs.STRING_UTF8, SBSetDispenserDestinationPacket::dim, ByteBufCodecs.INT, SBSetDispenserDestinationPacket::id, SBSetDispenserDestinationPacket::new);
    public static final Type<SBSetDispenserDestinationPacket> ID = new Type<>(PGHelper.createLocation("dispenser_destination"));

    public SBSetDispenserDestinationPacket(BlockPos pos, String dim, int id) {
        this.pos = pos;
        this.dim = dim;
        this.id = id;
    }

    public SBSetDispenserDestinationPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readUtf(), buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.dim);
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.id);
    }

    public void handle(ServerPlayer player) {
        if (player.containerMenu instanceof PortalDispenserMenu menu) {
            if (!PGCommonConfig.INSTANCE.getDisabledDimensions().contains(dim)) {
                menu.setCoords(pos, dim);
            } else
                player.sendSystemMessage(Component.translatable("notice.ricksportalgun.dimension_disabled").withStyle(ChatFormatting.RED), false);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
