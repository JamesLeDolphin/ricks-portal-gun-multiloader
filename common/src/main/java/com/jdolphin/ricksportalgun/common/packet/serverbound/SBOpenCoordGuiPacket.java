package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.packet.clientbound.CBOpenCoordGuiPacket;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record SBOpenCoordGuiPacket() implements PGPayload {
    public static final StreamCodec<FriendlyByteBuf, SBOpenCoordGuiPacket> CODEC = StreamCodec.unit(new SBOpenCoordGuiPacket());
    public static final Type<SBOpenCoordGuiPacket> ID = new Type<>(PGHelper.createLocation("open_coord_menu"));

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        InteractionHand hand = player.getUsedItemHand();
        ItemStack stack = player.getItemInHand(hand);

        if (PGHelper.canPlayerAccessGun(player, stack)) {
            List<String> dims = LevelHelper.getDimensionsAsString(server.getAllLevels());
            if (!dims.contains(PGHelper.createLocation("blender").toString()))
                dims.add(PGHelper.createLocation("blender").toString());
            PGHelper.sendPacketToClient(player, new CBOpenCoordGuiPacket(dims));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
