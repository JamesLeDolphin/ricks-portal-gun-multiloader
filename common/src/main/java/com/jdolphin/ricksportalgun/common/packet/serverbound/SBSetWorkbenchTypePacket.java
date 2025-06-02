package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.menu.workbench.WorkbenchCraftingMenu;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record SBSetWorkbenchTypePacket(int id) implements PGPayload {
    public static final StreamCodec<ByteBuf, SBSetWorkbenchTypePacket> CODEC = StreamCodec.composite(ByteBufCodecs.INT, SBSetWorkbenchTypePacket::id, SBSetWorkbenchTypePacket::new);
    public static final Type<SBSetWorkbenchTypePacket> ID = new Type<>(PGHelper.createLocation("set_workbench_type"));

    @Override
    public void handle(ServerPlayer player) {
        if (player.containerMenu instanceof WorkbenchCraftingMenu menu) {
            menu.setMenuType(id);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
