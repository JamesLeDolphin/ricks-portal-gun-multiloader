package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGConfigHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Optional;


public record SBLocatePacket(String name, int value) implements PGPayload {
    public static final StreamCodec<FriendlyByteBuf, SBLocatePacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SBLocatePacket::name, ByteBufCodecs.INT, SBLocatePacket::value, SBLocatePacket::new);
    public static final Type<SBLocatePacket> ID = new Type<>(PGHelper.id("locate"));

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        ServerLevel level = player.serverLevel();

        ItemStack stack = player.getItemInHand(PGHelper.getPortalGunHand(player));
        if (value == 0) {
            if (PGConfigHelper.disableBiomeLocating()) {
                PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.biome.disabled"));
                return;
            }
                Optional<HolderLookup.RegistryLookup<Biome>> optionalRegistry = server.registryAccess().lookup(Registries.BIOME);
                if (optionalRegistry.isPresent()) {
                    ResourceLocation location = ResourceLocation.parse(name);
                    Pair<BlockPos, Holder<Biome>> pair = level.findClosestBiome3d((biomeHolder -> biomeHolder.is(location)),
                            player.blockPosition(), 6400, 32, 64);
                    if (pair != null) {
                        BlockPos pos = pair.getFirst();
                        BlockPos safePos = LevelHelper.getSafePos(pos, level);
                        PortalGunItem.setHopLocation(stack, LevelHelper.getPlayerDimensionLocation(player), safePos);
                        PGHelper.sendSuccessMsg(player, PGHelper.COORDS_SET);
                    } else
                        PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.biome.not_in_area", name));
                }
        }
        if (value == 1) {
            if (PGConfigHelper.disablePlayerLocating()) {
                PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.player.disabled"));
                return;
            }

            ServerPlayer targetPlayer = server.getPlayerList().getPlayerByName(name);
            if (targetPlayer != null) {
                PortalGunItem.setHopLocation(stack, LevelHelper.getPlayerDimensionLocation(targetPlayer), targetPlayer.blockPosition().above());
                PGHelper.sendSuccessMsg(player, PGHelper.COORDS_SET);

            } else
                PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.player.not_found", name));
        }
        if (value == 2) {
            if (PGConfigHelper.disableStructureLocating()) {
                PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.structure.disabled"));
                return;
            }
            Optional<HolderLookup.RegistryLookup<Structure>> optionalRegistry = server.registryAccess().lookup(Registries.STRUCTURE);
            if (optionalRegistry.isPresent()) {
                HolderLookup.RegistryLookup<Structure> registry = optionalRegistry.get();
                ResourceLocation location = ResourceLocation.parse(name);
                Optional<Holder.Reference<Structure>> structureReference = registry.get(ResourceKey.create(Registries.STRUCTURE, location));
                if (structureReference.isPresent()) {
                    Structure structure = structureReference.get().value();

                    HolderSet<Structure> set = HolderSet.direct(Holder.direct(structure));

                    Pair<BlockPos, Holder<Structure>> pair = level.getChunkSource().getGenerator()
                            .findNearestMapStructure(level, set, player.blockPosition(), 100, false);
                    if (pair != null) {
                        BlockPos pos = pair.getFirst();
                        BlockPos safePos = LevelHelper.getSafePos(pos, level);
                        PortalGunItem.setHopLocation(stack, LevelHelper.getPlayerDimensionLocation(player), safePos);
                        PGHelper.sendSuccessMsg(player, PGHelper.COORDS_SET);
                    } else {
                        PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.structure.not_in_area", name));
                    }
                } else PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.structure.unknown", name));
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
