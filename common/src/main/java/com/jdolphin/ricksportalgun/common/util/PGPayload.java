package com.jdolphin.ricksportalgun.common.util;

import net.minecraft.server.level.ServerPlayer;

public interface PGPayload {

    void handle(ServerPlayer player);
}
