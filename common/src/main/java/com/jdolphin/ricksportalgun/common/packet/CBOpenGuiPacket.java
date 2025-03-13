package com.jdolphin.ricksportalgun.common.packet;

import com.jdolphin.ricksportalgun.client.screen.SubetherBarrierScreen;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class CBOpenGuiPacket implements CustomPacketPayload {
    public int id;
    public static final Type<CBOpenCoordGuiPacket> ID = new Type<>(PGHelper.createLocation("open_menu"));
    public static final StreamCodec<ByteBuf, CBOpenGuiPacket> CODEC = StreamCodec.composite(ByteBufCodecs.INT,
            CBOpenGuiPacket::getId, CBOpenGuiPacket::new);

    public CBOpenGuiPacket(int screenId) {
        id = screenId;
    }

    public int getId() {
        return id;
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    public void handle(Minecraft client) {
        switch (id) {
            case 0: client.setScreen(new SubetherBarrierScreen());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
