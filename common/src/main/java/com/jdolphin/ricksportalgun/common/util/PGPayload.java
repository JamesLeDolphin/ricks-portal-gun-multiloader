package com.jdolphin.ricksportalgun.common.util;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public interface PGPayload extends CustomPacketPayload {

    void handle(ServerPlayer player);
}
