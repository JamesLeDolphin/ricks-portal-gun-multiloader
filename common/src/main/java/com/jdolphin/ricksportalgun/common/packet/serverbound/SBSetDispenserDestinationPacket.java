package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.menu.PortalDispenserMenu;
import com.jdolphin.ricksportalgun.common.util.helper.PGConfigHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public record SBSetDispenserDestinationPacket(BlockPos pos, String dim) implements PGServerPayload {

    public static SBSetDispenserDestinationPacket decode(FriendlyByteBuf buf) {
        return new SBSetDispenserDestinationPacket(buf.readBlockPos(), buf.readUtf());
    }

    @Override
    public ResourceLocation getId() {
        return getID();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos).writeUtf(dim);
    }

    public static ResourceLocation getID() {
        return PGHelper.id("dispenser_destination");
    }

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        server.executeIfPossible(() -> {
            if (player.containerMenu instanceof PortalDispenserMenu menu) {
                String dimension = dim;
                if (PGConfigHelper.getDisabledDimensions().contains(dim)) {
                    dimension = player.level().dimension().location().toString();
                    PGHelper.sendFailMsg(player, "error.ricksportalgun.dimension.disabled");
                }
                menu.setCoords(pos, dimension);
                player.closeContainer(); //Close the container here, fixes a very rare bug of coords not applying
            }
        });
    }
}
