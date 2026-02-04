package com.jdolphin.ricksportalgun.common.event;

import com.jdolphin.ricksportalgun.common.init.PGDamageTypes;
import com.jdolphin.ricksportalgun.common.packet.clientbound.CBSyncDimensionListPacket;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGConfigHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class PGCommonEventHandler {

    public static void serverStartEvent(MinecraftServer server) {
        PGDamageTypes.init(server.registryAccess());
        List<String> strings = LevelHelper.getDimensionsAsString(server.getAllLevels(), PGConfigHelper.getDisabledDimensions());
        if (!strings.contains(PGHelper.id("blender").toString())) strings.add(PGHelper.id("blender").toString());
        LevelHelper.addDimensions(strings);
    }

    public static void playerJoinEvent(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            MinecraftServer server = serverPlayer.server;
            List<String> dims = LevelHelper.getDimensionsAsString(server.getAllLevels(), PGConfigHelper.getDisabledDimensions());

            if (!dims.contains(PGHelper.id("blender").toString())) dims.add(PGHelper.id("blender").toString());
            CBSyncDimensionListPacket dimPacket = new CBSyncDimensionListPacket(dims);
            PGHelper.sendPacketToClient(serverPlayer, dimPacket);
        }
    }

}
