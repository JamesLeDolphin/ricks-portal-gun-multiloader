package com.jdolphin.ricksportalgun.common.packets;

import com.jdolphin.ricksportalgun.client.screen.SubetherBarrierScreen;
import com.jdolphin.ricksportalgun.common.packet.CBOpenCoordGuiPacket;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class CBOpenGuiPacketFabric implements CustomPacketPayload {
    public int id;
    public static final Type<CBOpenGuiPacketFabric> ID = new Type<>(PGHelper.createLocation("open_menu"));
    public static final StreamCodec<ByteBuf, CBOpenGuiPacketFabric> CODEC = StreamCodec.composite(ByteBufCodecs.INT,
            CBOpenGuiPacketFabric::getId, CBOpenGuiPacketFabric::new);

    public CBOpenGuiPacketFabric(int screenId) {
        id = screenId;
    }

    public int getId() {
        return id;
    }

    @Environment(EnvType.CLIENT)
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
