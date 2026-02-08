package com.jdolphin.ricksportalgun.common.event;

import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.common.init.PGDamageTypes;
import com.jdolphin.ricksportalgun.common.packet.clientbound.CBSyncDimensionListPacket;
import com.jdolphin.ricksportalgun.common.util.DimMap;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGConfigHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.LevelResource;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

public class PGCommonEventHandler {

    public static void serverStartEvent(MinecraftServer server) {
        PGDamageTypes.init(server.registryAccess());
        List<String> strings = LevelHelper.getDimensionsAsString(server.getAllLevels(), PGConfigHelper.getDisabledDimensions());
        if (!strings.contains(PGHelper.id("blender").toString())) strings.add(PGHelper.id("blender").toString());
        LevelHelper.addDimensions(strings);
        mapDimensions(server);
    }

    public static void serverTickEvent(MinecraftServer server) {
        if (server.getTickCount() % 6000 == 0) {
            mapDimensions(server);
        }
    }

    public static void mapDimensions(MinecraftServer server) {
        File file = new File(server.getWorldPath(LevelResource.ROOT).getParent() + "/ricksportalgun/dims.json");

        try {
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();

            DimMap dimMap;
            try (FileReader reader = new FileReader(file)) {
                dimMap = PGConstants.GSON.fromJson(reader, DimMap.class);
                if (dimMap == null) dimMap = new DimMap();
            }

            int nextId = dimMap.DIM_MAP.values().stream().max(Integer::compareTo).orElse(0) + 1;

            for (String dim : LevelHelper.DIMENSIONS) {
                if (!dimMap.DIM_MAP.containsKey(dim) && !dim.equals(PGHelper.id("blender").toString())) {
                    dimMap.DIM_MAP.put(dim, nextId++);
                }
            }

            LevelHelper.DIM_TO_INT = dimMap.DIM_MAP;

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(PGConstants.GSON.toJson(dimMap));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
