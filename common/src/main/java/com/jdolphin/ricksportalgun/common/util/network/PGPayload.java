package com.jdolphin.ricksportalgun.common.util.network;

import net.minecraft.network.FriendlyByteBuf;

public interface PGPayload {

    void encode(FriendlyByteBuf buf);
}
