package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.menu.PortalDispenserMenu;
import com.jdolphin.ricksportalgun.common.util.helper.PGConfigHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record SBSetDispenserDestinationPacket(BlockPos pos, String dim) implements PGServerPayload {

    public static SBSetDispenserDestinationPacket decode(FriendlyByteBuf buf) {
        return new SBSetDispenserDestinationPacket(buf.readBlockPos(), buf.readUtf());
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos).writeUtf(dim);
    }

    public static ResourceLocation getID() {
        return PGHelper.id("dispenser_destination");
    }

    public void handle(ServerPlayer player) {
        if (player.containerMenu instanceof PortalDispenserMenu menu) {
            if (!PGConfigHelper.getDisabledDimensions().contains(dim)) {
                menu.setCoords(pos, dim);
            } else
                PGHelper.sendFailMsg(player, "error.ricksportalgun.dimension.disabled");
        }
    }
}
