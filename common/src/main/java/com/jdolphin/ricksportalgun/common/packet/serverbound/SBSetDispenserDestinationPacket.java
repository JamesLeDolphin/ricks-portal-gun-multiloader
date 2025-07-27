package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.menu.PortalDispenserMenu;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.PGConfigHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record SBSetDispenserDestinationPacket(BlockPos pos, String dim) implements PGPayload {
    public static final StreamCodec<ByteBuf, SBSetDispenserDestinationPacket> CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, SBSetDispenserDestinationPacket::pos,
            ByteBufCodecs.STRING_UTF8, SBSetDispenserDestinationPacket::dim, SBSetDispenserDestinationPacket::new);
    public static final Type<SBSetDispenserDestinationPacket> ID = new Type<>(PGHelper.id("dispenser_destination"));

    public void handle(ServerPlayer player) {
        if (player.containerMenu instanceof PortalDispenserMenu menu) {
            if (!PGConfigHelper.getDisabledDimensions().contains(dim)) {
                menu.setCoords(pos, dim);
            } else
                PGHelper.sendFailMsg(player, "error.ricksportalgun.dimension.disabled");
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
