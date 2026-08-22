package com.jdolphin.ricksportalgun.common.util.helper;


import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.common.blockentity.SubetherBarrierBlockEntity;
import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
        if (stack != null) {
            CompoundTag tag = stack.getOrCreateTag();
            return canPortalTo(level, pos, tag.contains(PGNbtKeys.BARRIER_CODE) ? tag.getString(PGNbtKeys.BARRIER_CODE) : "");
        }
        return false;
    }

    public static boolean canPortalTo(ServerLevel level, BlockPos pos, String code) {
        if (level != null && code != null) {
            if (!PGConfigHelper.getDisabledDimensions().contains(level.dimension().location().toString())) {
                List<BlockEntity> blockEntities = getBlockEntitiesInChunks(level, new ChunkPos(pos), 3);

                for (BlockEntity be : blockEntities) {
                    if (be instanceof SubetherBarrierBlockEntity barrier) {
                        return !barrier.canBlockPortal(level, barrier.getBlockPos(), code);
                    }
                }
                return true;
            }
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

    public static BlockPos getRandomCoord(BlockPos pos, ServerLevel level, int radius) {
        WorldBorder border = level.getWorldBorder();
        RandomSource rand = level.getRandom();
        int xCoord = Mth.nextInt(rand, pos.getX() - radius, pos.getX() + radius);
        int yCoord = Mth.nextInt(rand,level.getMinBuildHeight() + 1, level.getMaxBuildHeight());
        int zCoord = Mth.nextInt(rand, pos.getZ() - radius, pos.getZ() + radius);
        return border.clampToBounds(xCoord, yCoord, zCoord);
    }

    public static BlockPos getSafeRandomCoords(BlockPos pos, ServerLevel level, int minDistance, int maxDistance) {
        WorldBorder border = level.getWorldBorder();
        RandomSource rand = level.getRandom();

        for (int attempts = 0; attempts < 5; attempts++) {
            double dist = minDistance + rand.nextDouble() * (maxDistance - minDistance);
            double angle = rand.nextDouble() * Math.PI * 2D;

            int x = Mth.floor(Math.cos(angle) * dist);
            int y = 256;
            int z = Mth.floor(Math.sin(angle) * dist);

            BlockPos randomPos = new BlockPos(pos.getX() + x, y, pos.getZ() + z);
            if (border.isWithinBounds(randomPos)) {
                level.getChunkAt(randomPos);
                BlockPos hmPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, randomPos);

                if (hmPos.getY() > 0) {
                    BlockPos goodPos = null;
                    if (hmPos.getY() < level.getLogicalHeight()) {
                        goodPos = hmPos;
                    } else {
                        // broken heightmap (nether, other mod dimensions)
                        for (BlockPos newPos : BlockPos.spiralAround(new BlockPos(hmPos.getX(), level.getSeaLevel(), hmPos.getZ()), 16, Direction.EAST, Direction.SOUTH)) {
                            BlockState bs = level.getBlockState(newPos);
                            if (bs.blocksMotion() && level.isEmptyBlock(newPos.above(1)) && level.isEmptyBlock(newPos.above(2)) &&
                                    level.isEmptyBlock(newPos.above(3))) {
                                goodPos = newPos.immutable();
                                break;
                            }
                        }
                    }
                    if (goodPos != null) {
                        return goodPos.above();
                    }
                }
            }
        }

        return null;
    }

    public static ServerLevel getRandomServerLevel(MinecraftServer server) {
        Iterable<ServerLevel> worlds = server.getAllLevels();
        List<ServerLevel> worldList = new ArrayList<>();
        worlds.forEach(world -> {
                if (world != null) worldList.add(world);
        });
        return worldList.get(PGConstants.RANDOM.nextInt(worldList.size()));
    }

    public static void randomTP(ServerPlayer player, int minDist, int maxDist, boolean interdimensional) {
        ServerLevel level = player.serverLevel();
        ServerLevel dest = getRandomServerLevel(player.server);
        BlockPos safePos = getSafeRandomCoords(player.blockPosition(), dest, minDist, maxDist);
        if (safePos != null) teleportEntity(player, interdimensional ? dest : level, safePos);
    }

    public static BlockPos getSafePos(BlockPos pos, ServerLevel level) {
        level.getChunkAt(pos);
        BlockPos hmPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos);

        if (hmPos.getY() > 0) {
            BlockPos goodPos = null;
            if (hmPos.getY() < level.getLogicalHeight()) {
                goodPos = hmPos;
            } else {
                // broken heightmap (nether, other mod dimensions)
                for (BlockPos newPos : BlockPos.spiralAround(new BlockPos(hmPos.getX(), level.getSeaLevel(), hmPos.getZ()), 16, Direction.EAST, Direction.SOUTH)) {
                    BlockState bs = level.getBlockState(newPos);
                    if (bs.blocksMotion() && level.isEmptyBlock(newPos.above(1)) && level.isEmptyBlock(newPos.above(2)) &&
                            level.isEmptyBlock(newPos.above(3))) {
                        goodPos = newPos.immutable();
                        break;
                    }
                }
            }
            if (goodPos != null) {
                return goodPos.above();
            }
        }
        return null;
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
        world.playSound(null, pos, sound, category, 1, 1);
    }
}
