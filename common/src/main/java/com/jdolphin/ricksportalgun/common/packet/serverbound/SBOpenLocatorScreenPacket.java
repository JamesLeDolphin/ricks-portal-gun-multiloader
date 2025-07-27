package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.packet.clientbound.CBOpenLocatorScreenPacket;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public record SBOpenLocatorScreenPacket() implements PGPayload {
    public static final StreamCodec<FriendlyByteBuf, SBOpenLocatorScreenPacket> CODEC = StreamCodec.unit(new SBOpenLocatorScreenPacket());
    public static final Type<SBOpenLocatorScreenPacket> ID = new Type<>(PGHelper.id("open_locator_screen"));

    @Override
    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;

        List<String> players = Arrays.asList(server.getPlayerNames());
        int playerCount = server.getPlayerNames().length;
        List<String> biomes = new ArrayList<>();
        List<String> structures = new ArrayList<>();

        Optional<Registry<Biome>> biomeRegistry = server.registryAccess().lookup(Registries.BIOME);
        Optional<Registry<Structure>> structureRegistry = server.registryAccess().lookup(Registries.STRUCTURE);
        int biomeCount = 0;
        int structureCount = 0;
        if (biomeRegistry.isPresent()) {
            biomeCount = biomeRegistry.get().size();
            biomeRegistry.ifPresent(registry -> registry.asHolderIdMap().forEach(holders -> {
                String biomeName = holders.getRegisteredName();
                if (!biomes.contains(biomeName)) {
                    biomes.add(biomeName);
                }
            }));
        }
        if (structureRegistry.isPresent()) {
            structureCount = structureRegistry.get().size();
            structureRegistry.ifPresent(registry -> registry.asHolderIdMap().forEach(holders -> {
                String structureName = holders.getRegisteredName();
                if (!structures.contains(structureName)) {
                    structures.add(structureName);
                }
            }));
        }
        if (playerCount == players.size() && biomeCount == biomes.size() && structureCount == structures.size()) {
            CBOpenLocatorScreenPacket packet = new CBOpenLocatorScreenPacket(players, biomes, structures);
            PGHelper.sendPacketToClient(player, packet);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
