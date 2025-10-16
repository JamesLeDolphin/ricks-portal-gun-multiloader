package com.jdolphin.ricksportalgun.common.packet.clientbound;

import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record CBOpenCoordGuiPacket(List<String> strings) implements PGPayload {

    public static CBOpenCoordGuiPacket decode(FriendlyByteBuf buf) {
        return new CBOpenCoordGuiPacket(buf.readList(FriendlyByteBuf::readUtf));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeCollection(strings, FriendlyByteBuf::writeUtf);
    }

    public static ResourceLocation getID() {
        return PGHelper.id("open_client_coord_menu");
    }

    @Override
    public ResourceLocation getId() {
        return getID();
    }
}
