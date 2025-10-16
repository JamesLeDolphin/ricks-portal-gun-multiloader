package com.jdolphin.ricksportalgun.common.util.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface PGPayload {

    void encode(FriendlyByteBuf buf);

    ResourceLocation getId();
}
