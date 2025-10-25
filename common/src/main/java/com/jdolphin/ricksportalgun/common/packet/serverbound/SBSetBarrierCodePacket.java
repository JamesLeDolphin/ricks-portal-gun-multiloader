package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.blockentity.SubetherBarrierBlockEntity;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public record SBSetBarrierCodePacket(String code, BlockPos pos) implements PGServerPayload {

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        server.executeIfPossible(() -> {
            ServerLevel level = player.serverLevel();

            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof SubetherBarrierBlockEntity barrier) {
                barrier.setCode(code);
                PGHelper.sendSuccessMsg(player, Component.translatable("notice.ricksportalgun.barrier.code_set"));
            }
        });
    }

    public static SBSetBarrierCodePacket decode(FriendlyByteBuf buf) {
        String code = buf.readUtf();
        BlockPos pos = buf.readBlockPos();
        return new SBSetBarrierCodePacket(code, pos);
    }

    @Override
    public ResourceLocation getId() {
        return getID();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(code);
        buf.writeBlockPos(pos);
    }

    public static ResourceLocation getID() {
        return PGHelper.id("barrier_code");
    }
}
