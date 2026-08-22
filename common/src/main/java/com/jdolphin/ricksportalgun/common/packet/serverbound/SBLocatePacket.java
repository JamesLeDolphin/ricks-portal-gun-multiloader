package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGConfigHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;


public record SBLocatePacket(String name, int value) implements PGServerPayload {

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        server.executeIfPossible(() -> {
            ServerLevel level = player.serverLevel();
            WorldBorder border = level.getWorldBorder();
            ItemStack stack = player.getItemInHand(PGHelper.getPortalGunHand(player));
            if (value == 0) {
                if (PGConfigHelper.disableBiomeLocating()) {
                    PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.biome.disabled"));
                    return;
                }
                Optional<HolderLookup.RegistryLookup<Biome>> optionalRegistry = server.registryAccess().lookup(Registries.BIOME);
                if (optionalRegistry.isPresent()) {
                    ResourceLocation location = new ResourceLocation(name);
                    Pair<BlockPos, Holder<Biome>> pair = level.findClosestBiome3d((biomeHolder -> biomeHolder.is(location)),
                            player.blockPosition(), 6400, 32, 64);
                    if (pair != null) {
                        BlockPos pos = pair.getFirst();
                        if (border.isWithinBounds(pos)) {
                            BlockPos safePos = LevelHelper.getSafePos(pos, level);
                            PortalGunItem.setHopLocation(stack, LevelHelper.getPlayerDimensionLocation(player), safePos);
                            PGHelper.sendSuccessMsg(player, PGHelper.COORDS_SET);

                        } else PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.outside_border"));
                    } else PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.biome.not_in_area", name));
                }
            }
            if (value == 1) {
                if (PGConfigHelper.disablePlayerLocating()) {
                    PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.player.disabled"));
                    return;
                }

                ServerPlayer targetPlayer = server.getPlayerList().getPlayerByName(name);
                if (targetPlayer != null) {
                    Vec3 targetVec = Vec3.directionFromRotation(new Vec2(45.0F, targetPlayer.getYRot() + 180.0F));
                    BlockPos pos = targetPlayer.blockPosition().above();
                    BlockPos betterPos = BlockPos.containing(pos.getX() + targetVec.x * 2, pos.getY(), pos.getZ() + targetVec.z * 2);
                    if (border.isWithinBounds(pos)) {
                        PortalGunItem.setHopLocation(stack, LevelHelper.getPlayerDimensionLocation(targetPlayer), betterPos);
                        PGHelper.sendSuccessMsg(player, PGHelper.COORDS_SET);
                    } else PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.outside_border"));
                } else
                    PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.player.not_found", name));
            }
            if (value == 2) {
                if (PGConfigHelper.disableStructureLocating()) {
                    PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.structure.disabled"));
                    return;
                }
                Registry<Structure> registry = server.registryAccess().registryOrThrow(Registries.STRUCTURE);
                ResourceLocation location = new ResourceLocation(name);
                Structure structure = registry.get(ResourceKey.create(Registries.STRUCTURE, location));
                if (structure != null) {
                    HolderSet<Structure> set = HolderSet.direct(Holder.direct(structure));
                    Pair<BlockPos, Holder<Structure>> pair = level.getChunkSource().getGenerator().findNearestMapStructure(level, set, player.blockPosition(), 256, false);
                    if (pair != null) {
                        BlockPos pos = pair.getFirst();
                        if (border.isWithinBounds(pos)) {
                            BlockPos safePos = LevelHelper.getSafePos(pos, level);
                            PortalGunItem.setHopLocation(stack, LevelHelper.getPlayerDimensionLocation(player), safePos);
                            PGHelper.sendSuccessMsg(player, PGHelper.COORDS_SET);
                        } else PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.outside_border"));
                    } else {
                        PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.structure.not_in_area", name));
                    }
                } else
                    PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.structure.unknown", name));
            }
        });
    }

    @Override
    public ResourceLocation getId() {
        return getID();
    }

    public static SBLocatePacket decode(FriendlyByteBuf buf) {
        String name = buf.readUtf();
        int value = buf.readInt();
        return new SBLocatePacket(name, value);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(name);
        buf.writeInt(value);
    }

    public static ResourceLocation getID() {
        return PGHelper.id("locate");
    }
}
