package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.packet.clientbound.CBOpenLocatorScreenPacket;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public record SBOpenLocatorScreenPacket() implements PGServerPayload {

    @Override
    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;

        List<String> players = Arrays.asList(server.getPlayerNames());
        int playerCount = server.getPlayerNames().length;
        List<String> biomes = new ArrayList<>();
        List<String> structures = new ArrayList<>();

        Optional<HolderLookup.RegistryLookup<Biome>> biomeRegistry = server.registryAccess().lookup(Registries.BIOME);
        Optional<HolderLookup.RegistryLookup<Structure>> structureRegistry = server.registryAccess().lookup(Registries.STRUCTURE);
        int biomeCount = 0;
        int structureCount = 0;
        if (biomeRegistry.isPresent()) {
            biomeCount = biomeRegistry.get().listElements().toList().size();
            biomeRegistry.ifPresent(registry -> registry.listElementIds().forEach(holders -> {
                String biomeName = holders.location().toString();
                if (!biomes.contains(biomeName)) {
                    biomes.add(biomeName);
                }
            }));
        }
        if (structureRegistry.isPresent()) {
            structureCount = structureRegistry.get().listElements().toList().size();
            structureRegistry.ifPresent(registry -> registry.listElementIds().forEach(holders -> {
                String structureName = holders.location().toString();
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
    public ResourceLocation getId() {
        return getID();
    }

    public static SBOpenLocatorScreenPacket decode(FriendlyByteBuf buf) {
        return new SBOpenLocatorScreenPacket();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {}

    public static ResourceLocation getID() {
        return PGHelper.id("open_locator_screen");
    }
}
