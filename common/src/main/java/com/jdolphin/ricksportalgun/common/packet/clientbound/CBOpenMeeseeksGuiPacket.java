package com.jdolphin.ricksportalgun.common.packet.clientbound;

import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record CBOpenMeeseeksGuiPacket(UUID mobId) implements PGPayload {

    public static CBOpenMeeseeksGuiPacket decode(FriendlyByteBuf buf) {
        return new CBOpenMeeseeksGuiPacket(buf.readUUID());
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(mobId);
    }

    @Override
    public ResourceLocation getId() {
        return getID();
    }

    public static ResourceLocation getID() {
        return PGHelper.id("open_meeseeks_gui");
    }
}
