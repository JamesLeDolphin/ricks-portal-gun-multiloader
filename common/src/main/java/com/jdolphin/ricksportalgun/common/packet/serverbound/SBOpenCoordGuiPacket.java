package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.packet.clientbound.CBOpenCoordGuiPacket;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record SBOpenCoordGuiPacket() implements PGServerPayload {

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        server.executeIfPossible(() -> {
            ItemStack stack = player.getItemInHand(PGHelper.getPortalGunHand(player));
            if (PGHelper.canPlayerAccessGun(player, stack)) {
                List<String> dims = LevelHelper.getDimensionsAsString(server.getAllLevels());
                if (!dims.contains(PGHelper.id("blender").toString()))
                    dims.add(PGHelper.id("blender").toString());
                PGHelper.sendPacketToClient(player, new CBOpenCoordGuiPacket(dims));
            }
        });
    }

    @Override
    public ResourceLocation getId() {
        return getID();
    }

    public static SBOpenCoordGuiPacket decode(FriendlyByteBuf buf) {
        return new SBOpenCoordGuiPacket();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {}

    public static ResourceLocation getID() {
        return PGHelper.id("open_coord_menu");
    }
}
