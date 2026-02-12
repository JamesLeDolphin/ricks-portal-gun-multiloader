package com.jdolphin.ricksportalgun.common.packet.clientbound;

import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record CBOpenDialerGuiPacket(BlockPos pos) implements PGPayload {

    public static CBOpenDialerGuiPacket decode(FriendlyByteBuf buf) {
        return new CBOpenDialerGuiPacket(buf.readBlockPos());
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    @Override
    public ResourceLocation getId() {
        return getID();
    }

    public static ResourceLocation getID() {
        return PGHelper.id("open_portal_block_dialer");
    }
}
