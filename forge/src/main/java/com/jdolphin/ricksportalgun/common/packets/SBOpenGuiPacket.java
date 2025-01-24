package com.jdolphin.ricksportalgun.common.packets;

import com.jdolphin.ricksportalgun.common.init.ForgePackets;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.ArrayList;
import java.util.List;

public class SBOpenGuiPacket {

    public SBOpenGuiPacket() {}

    public SBOpenGuiPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public boolean handle(CustomPayloadEvent.Context context) {
        ServerPlayer player = context.getSender();
        assert player != null;
        MinecraftServer server = player.server;

        Iterable<ServerLevel> worlds = server.getAllLevels();
        List<String> worldList = new ArrayList<>();
        worlds.forEach(world -> {
            ResourceLocation worldKey = world.dimension().location();
            if (worldKey != null) {
                String s = worldKey.toString();
                if (!s.isEmpty()) worldList.add(s);
            }
        });
        ForgePackets.sendToPlayer(new CBOpenGuiPacket(worldList), player);
        return true;
    }
}