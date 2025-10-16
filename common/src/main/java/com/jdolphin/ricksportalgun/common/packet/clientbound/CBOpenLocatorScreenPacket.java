package com.jdolphin.ricksportalgun.common.packet.clientbound;

import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record CBOpenLocatorScreenPacket(List<String> playerList, List<String> biomeList, List<String> structureList) implements PGPayload {

    public static CBOpenLocatorScreenPacket decode(FriendlyByteBuf buf) {
        return new CBOpenLocatorScreenPacket(buf.readList(FriendlyByteBuf::readUtf),
                buf.readList(FriendlyByteBuf::readUtf),
                buf.readList(FriendlyByteBuf::readUtf));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeCollection(playerList, FriendlyByteBuf::writeUtf);
        buf.writeCollection(biomeList, FriendlyByteBuf::writeUtf);
        buf.writeCollection(structureList, FriendlyByteBuf::writeUtf);
    }

    @Override
    public ResourceLocation getId() {
        return getID();
    }

    public static ResourceLocation getID() {
        return PGHelper.id("open_client_locator_screen");
    }
}
