package com.jdolphin.ricksportalgun.common.util.helper;


import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.common.blockentity.SubetherBarrierBlockEntity;
import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LevelHelper {
    public static List<String> DIMENSIONS = new ArrayList<>();
    public static List<String> CLIENT_DIMENSIONS = new ArrayList<>();

    /**Adds a dimension to the list, does not register a new one**/
    public static void addDimension(String dim) {
        if (!DIMENSIONS.contains(dim)) DIMENSIONS.add(dim);
    }

    public static void addDimensions(List<String> dims) {
        for (String s : dims) {
            addDimension(s);
        }
    }

    public static boolean isBlenderDestination(String s) {
        return PGHelper.id("blender").toString().equals(s);
    }

    public static List<String> getDimensionsAsString(Iterable<ServerLevel> levels, List<String> list) {
        levels.forEach(world -> {
            ResourceLocation worldKey = world.dimension().location();
            String s = worldKey.toString();
            if (!s.isEmpty() && !list.contains(s)) list.add(s);
        });
        return list;
    }

    public static List<String> getDimensionsAsString(Iterable<ServerLevel> levels) {
        return getDimensionsAsString(levels, new ArrayList<>());
    }

    public static List<BlockEntity> getBlockEntitiesInChunks(ServerLevel level, ChunkPos pos, int radius) {
        if (level != null) {
            List<BlockEntity> list = new ArrayList<>();
            for (int x = -radius; x <= radius; ++x) {
                for (int z = -radius; z <= radius; ++z) {
                    list.addAll(level.getChunk(pos.x + x, pos.z + z).getBlockEntities().values());
                }
            }
            return list;
        }
        return List.of();
    }

    public static boolean canPortalTo(ServerLevel level, BlockPos pos, ItemStack stack) {
        if (level != null) {
            String code = "";
            if (stack != null) {
                CompoundTag tag = stack.getOrCreateTag();
                code = tag.contains(PGNbtKeys.BARRIER_CODE) ? tag.getString(PGNbtKeys.BARRIER_CODE) : "";
            }
            List<BlockEntity> blockEntities = getBlockEntitiesInChunks(level, new ChunkPos(pos), 3);

            for (BlockEntity be : blockEntities) {
                if (be instanceof SubetherBarrierBlockEntity barrier) {
                    return !barrier.canBlockPortal(level, barrier.getBlockPos(), code);
                }
            }
            return true;
        }
        return false;
    }

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
        int min = radius < 1000 ? 10 : 100;
        int xCoord = rand.nextInt(min, radius);
        int yCoord = Mth.nextInt(rand,level.getMinBuildHeight() + 1, level.getMaxBuildHeight());
        int zCoord =  rand.nextInt(min, radius);
        return border.clampToBounds(xCoord, yCoord, zCoord);
    }

    public static ServerLevel getRandomServerLevel(MinecraftServer server) {
        Iterable<ServerLevel> worlds = server.getAllLevels();
        List<ServerLevel> worldList = new ArrayList<>();
        worlds.forEach(world -> {
                if (world != null) worldList.add(world);
        });
        return worldList.get(PGConstants.RANDOM.nextInt(worldList.size()));
    }

    public static void randomTP(ServerPlayer player, int radius, boolean interdimensional) {
        ServerLevel level = player.serverLevel();
        ServerLevel dest = getRandomServerLevel(player.server);
        teleportEntity(player, interdimensional ? dest : level, getSafePos(getRandomCoord(dest, radius), level));
    }

    public static BlockPos getSafePos(BlockPos bPos, ServerLevel level) {
        return getSafePos(bPos, level, 0);
    }

    private static BlockPos getSafePos(BlockPos bPos, ServerLevel level, int iteration) {
        iteration++;
        ChunkAccess chunk = level.getChunk(bPos);
        level.setChunkForced(chunk.getPos().x, chunk.getPos().z, true);

        int y = bPos.getY();
        int height = level.getHeight(Heightmap.Types.WORLD_SURFACE, bPos.getX(), bPos.getZ());
        int worldCenter = ((level.getMinBuildHeight() + 2) + height) / 2;

        int direction = y > worldCenter ? -1 : 1;

        while (y >= level.getMinBuildHeight() + 2 && y <= level.getMaxBuildHeight()) {
            BlockPos pos1 = new BlockPos(bPos.getX(), y, bPos.getZ());

            if (!isRandomizerSafe(level, pos1)) {

                y += direction;

            } else break;
        }

        bPos = new BlockPos(bPos.getX(), y, bPos.getZ());

        if (!isRandomizerSafe(level, bPos)
                || y <= level.getMinBuildHeight() + 2 || y >= level.getMaxBuildHeight()) {
            return iteration <= 100 ? getSafePos(getRandomCoord(level, 25), level, iteration) : bPos;
        }
        level.setChunkForced(chunk.getPos().x, chunk.getPos().z, false);
        return bPos;
    }


    public static boolean endHasDragons(ServerLevel level) {
        if (level != null) {
            if (Level.END.location().equals(getLevelDimensionLocation(level))) {
                EndDragonFight fight = level.getDragonFight();
                if (fight != null) {
                    EndDragonFight.Data data = fight.saveData();
                    return !data.dragonKilled() || data.isRespawning();
                }
            }
        }
        return false;
    }

    public static boolean isRandomizerSafe(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        BlockState aboveState = level.getBlockState(pos.above());
        BlockState belowState = level.getBlockState(pos.below());

        if (!(belowState.isSuffocating(level, pos) || belowState.is(Blocks.LAVA) || belowState.isAir() || belowState.is(Blocks.KELP_PLANT))) {
            if (!(state.isSuffocating(level, pos) || state.is(Blocks.WATER) || state.is(Blocks.LAVA) || state.is(Blocks.KELP_PLANT))) {
                return !(aboveState.isSuffocating(level, pos) || aboveState.is(Blocks.WATER) || aboveState.is(Blocks.LAVA) || aboveState.is(Blocks.KELP_PLANT));
            }
        }
        return false;
    }

    public static void teleportEntity(Entity entity, ServerLevel level, BlockPos pos) {
        Set<RelativeMovement> relativeSet = new HashSet<>();
        relativeSet.add(RelativeMovement.Y_ROT);
        entity.teleportTo(level, pos.getX(), pos.getY(), pos.getZ(), relativeSet, entity.getYRot(), entity.getXRot());
    }

    public static ResourceLocation getLevelDimensionLocation(Level world) {
        return world.dimension().location();
    }

    public static void playSound(Level world, BlockPos pos, SoundEvent sound, SoundSource category) {
        world.playSound(null, pos, sound, category, 100, 1);
    }
}
