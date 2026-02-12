package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.blockentity.PortalControllerBlockEntity;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public record SBPortalDialActionPacket(BlockPos pos, String address, boolean disconnect) implements PGServerPayload {

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos).writeUtf(address).writeBoolean(disconnect);
    }

    public static SBPortalDialActionPacket decode(FriendlyByteBuf buf) {
        BlockPos pos1 = buf.readBlockPos();
        String address = buf.readUtf();
        boolean delete = buf.readBoolean();
        return new SBPortalDialActionPacket(pos1, address, delete);
    }

    @Override
    public ResourceLocation getId() {
        return getID();
    }

    public static ResourceLocation getID() {
        return PGHelper.id("portal_dial_action");
    }

    @Override
    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        server.executeIfPossible(() -> {
            ServerLevel level = player.serverLevel();
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof PortalControllerBlockEntity controller) {
                if (disconnect) {
                    controller.disconnect();
                } else {
                    controller.tryActivate(address);
                }
            }
        });
    }
}
