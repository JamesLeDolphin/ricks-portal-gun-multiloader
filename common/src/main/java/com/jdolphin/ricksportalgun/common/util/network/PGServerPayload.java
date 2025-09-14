package com.jdolphin.ricksportalgun.common.util.network;
import net.minecraft.server.level.ServerPlayer;

public interface PGServerPayload extends PGPayload {

    void handle(ServerPlayer player);
}
