package com.jdolphin.ricksportalgun.common.packet.clientbound;

import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record CBSyncDimensionListPacket(List<String> dimensions) implements PGPayload {

    public static CBSyncDimensionListPacket decode(FriendlyByteBuf buf) {
        return new CBSyncDimensionListPacket(buf.readList(FriendlyByteBuf::readUtf));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeCollection(dimensions, FriendlyByteBuf::writeUtf);
    }

    public static ResourceLocation getID() {
        return PGHelper.id("sync_dimensions");
    }
}
