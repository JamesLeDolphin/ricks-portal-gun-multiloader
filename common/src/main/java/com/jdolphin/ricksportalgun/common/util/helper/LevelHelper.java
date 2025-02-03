package com.jdolphin.ricksportalgun.common.util.helper;


import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.*;

public class LevelHelper {

    public static ResourceKey<Level> getWorldKey(ResourceLocation dimension) {
        return ResourceKey.create(Registries.DIMENSION, dimension);
    }

    public static ServerLevel getServerWorld(Level level, ResourceKey<Level> dimension) {
        return level.getServer().getLevel(dimension);
    }

    public static ResourceLocation getPlayerDimensionLocation(Player player) {
        return getLevelDimensionLocation(player.level());
    }

    public static BlockPos getRandomCoord(ServerLevel level, int radius) {
        WorldBorder border = level.getWorldBorder();
        RandomSource rand = level.getRandom();
        int xCoord = Mth.nextInt(rand, (int) Math.max(border.getMinX(), -radius), (int) Math.min(border.getMaxX(), radius));
        int yCoord = Mth.nextInt(rand,level.getMinY() + 1, level.getMaxY());
        int zCoord = Mth.nextInt(rand,(int) Math.max(border.getMinZ(), -radius), (int) Math.min(border.getMaxZ(), radius));
        return new BlockPos(xCoord, yCoord, zCoord);
    }

    public static ServerLevel getRandomServerLevel(MinecraftServer server) {
        Random random = new Random();
        Iterable<ServerLevel> worlds = server.getAllLevels();
        List<ServerLevel> worldList = new ArrayList<>();
        worlds.forEach(world -> {
                if (world != null) worldList.add(world);
        });
        return worldList.get(random.nextInt(worldList.size()));
    }

    public static void randomTP(ServerPlayer player, int radius) {
        ServerLevel level = player.serverLevel();
        ServerLevel dest = getRandomServerLevel(player.server);
        teleportEntity(player, dest, getSafePos(getRandomCoord(dest, radius), level));
    }

    public static BlockPos getSafePos(BlockPos bPos, ServerLevel level) {
        ChunkAccess chunk = level.getChunk(bPos);
        level.setChunkForced(chunk.getPos().x, chunk.getPos().z, true);

        int y = bPos.getY();
        int height = level.getHeight();
        int worldCenter = ((level.getMinY() + 2) + height) / 2;

        int direction = y > worldCenter ? -1 : 1;

        while (y >= level.getMinY() + 2 && y <= level.getHeight()) {
            BlockPos pos1 = new BlockPos(bPos.getX(), y, bPos.getZ());

            BlockState blockState = level.getBlockState(pos1);
            BlockState belowState = level.getBlockState(pos1.below());
            BlockState aboveState = level.getBlockState(pos1.above());

            if (belowState.isAir()
                    || aboveState.is(PGTags.Blocks.RANDOMIZER_AVOID)
                    || blockState.isSuffocating(level, pos1)) {

                y += direction;

            } else break;
        }

        bPos = new BlockPos(bPos.getX(), y, bPos.getZ());

        BlockState blockState = level.getBlockState(bPos);
        if (blockState.isSuffocating(level, bPos)
                || blockState.is(PGTags.Blocks.RANDOMIZER_AVOID)
                || y <= level.getMinY() + 2 || y >= level.getHeight()) {
            return getSafePos(getRandomCoord(level, 500), level);
        }
        level.setChunkForced(chunk.getPos().x, chunk.getPos().z, false);
        return bPos;
    }

    public static void teleportEntity(Entity entity, ServerLevel level, BlockPos pos) {
        Set<Relative> relativeSet = new HashSet<>();
        relativeSet.add(Relative.Y_ROT);
        entity.teleportTo(level, pos.getX(), pos.getY(), pos.getZ(), relativeSet, entity.getYRot(), entity.getXRot(), false);
    }

    public static ResourceLocation getLevelDimensionLocation(Level world) {
        return world.dimension().location();
    }

    public static void playSound(Level world, BlockPos pos, SoundEvent sound, SoundSource category) {
        world.playSound(null, pos, sound, category, 100, 1);
    }
}
