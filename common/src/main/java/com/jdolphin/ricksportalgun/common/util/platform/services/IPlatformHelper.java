package com.jdolphin.ricksportalgun.common.util.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public interface IPlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    <P extends CustomPacketPayload> void sendPacketToServer(P packet);

    <P extends CustomPacketPayload> void sendPacketToClient(ServerPlayer player, P packet);

    String getConfigPath();

    void openBarrierScreen(ServerPlayer player, BlockPos pos);

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }
}
