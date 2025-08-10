package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGConfigHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;


public record SBLocatePacket(String name, int value) implements PGPayload {
    public static final StreamCodec<FriendlyByteBuf, SBLocatePacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SBLocatePacket::name, ByteBufCodecs.INT, SBLocatePacket::value, SBLocatePacket::new);
    public static final Type<SBLocatePacket> ID = new Type<>(PGHelper.id("locate"));

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        ServerLevel level = player.serverLevel();

        InteractionHand hand = PGHelper.getPortalGunHand(player);
        ItemStack stack = player.getItemInHand(hand);
        if (value == 0) {
            if (!PGConfigHelper.disableBiomeLocating()) {
            Optional<Registry<Biome>> optionalRegistry = server.registryAccess().lookup(Registries.BIOME);
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
            } else {
                PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.biome.disabled"));
                return;
            }
        }
        if (value == 1) {
            if (!PGConfigHelper.disablePlayerLocating()) {
                ServerPlayer targetPlayer = server.getPlayerList().getPlayerByName(name);
                if (targetPlayer != null && !targetPlayer.isSpectator()) {
                    BlockPos pos = targetPlayer.blockPosition().above();
                    Vec3 look = Vec3.directionFromRotation(new Vec2(45.0F, targetPlayer.getYRot() + 180.0F));
                    double dx = (double) pos.getX() + look.x * 2d;
                    double dz = (double) pos.getZ() + look.z * 2d;
                    BlockPos destination = new BlockPos((int) dx, pos.getY(), (int) dz);
                    PortalGunItem.setHopLocation(stack, LevelHelper.getPlayerDimensionLocation(targetPlayer), destination);
                    PGHelper.sendSuccessMsg(player, PGHelper.COORDS_SET);
                } else
                    PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.player.not_found", name));
            } else {
                PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.player.disabled"));
                return;
            }
        }
        if (value == 2) {
            if (!PGConfigHelper.disableStructureLocating()) {
                Optional<Registry<Structure>> optionalRegistry = server.registryAccess().lookup(Registries.STRUCTURE);
                if (optionalRegistry.isPresent()) {
                    Registry<Structure> registry = optionalRegistry.get();
                    ResourceLocation location = ResourceLocation.parse(name);
                    Optional<Holder.Reference<Structure>> structureReference = registry.get(location);
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
                    } else
                        PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.structure.unknown", name));
                }
            } else {
                PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.structure.disabled"));
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
