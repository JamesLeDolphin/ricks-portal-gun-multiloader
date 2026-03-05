package com.jdolphin.ricksportalgun.common.blockentity;

import com.jdolphin.ricksportalgun.common.block.PortalBlock;
import com.jdolphin.ricksportalgun.common.block.PortalControllerBlock;
import com.jdolphin.ricksportalgun.common.block.PortalFrameBlock;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import com.jdolphin.ricksportalgun.common.init.PGBlocks;
import com.jdolphin.ricksportalgun.common.util.PGPortalAddress;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class PortalControllerBlockEntity extends BlockEntity {
    private BlockPos destinationPos = null;
    private String destinationDim = "";

    private final Queue<Entity> tpQueue = new ArrayDeque<>();

    private final List<BlockPos> framePositions = new ArrayList<>();
    public final int[][] portalShape = new int[][]{
            new int[]{1, 1, 2, 1, 1},
            new int[]{1, 0, 0, 0, 1},
            new int[]{1, 0, 0, 0, 1},
            new int[]{1, 0,0, 0, 1},
            new int[]{1, 0, 0, 0, 1},
            new int[]{1, 1, 1, 1, 1},
    };


    public void queueForTeleport(Entity entity) {
        if (!tpQueue.contains(entity)) {
            tpQueue.offer(entity);
        }
    }

    public PortalControllerBlockEntity(BlockPos pos, BlockState blockState) {
        super(PGBlockEntities.PORTAL_CONTROLLER, pos, blockState);
    }

    public void addFrame(BlockPos pos) {
        if (!framePositions.contains(pos)) framePositions.add(pos);
    }

    public void removeFrame(BlockPos pos) {
        framePositions.remove(pos);
    }

    public void disconnect() {
        if (level instanceof ServerLevel serverLevel) {
            BlockState state = getBlockState();
            BlockPos pos = getBlockPos();
            if (!destinationDim.isEmpty() && destinationPos != null) {
                ResourceKey<Level> key = LevelHelper.getWorldKey(new ResourceLocation(destinationDim));
                ServerLevel destinationLevel = LevelHelper.getServerWorld(serverLevel, key);
                if (destinationLevel != null) {
                    ChunkPos destChunkPos = new ChunkPos(destinationPos);
                    serverLevel.setChunkForced(destChunkPos.x, destChunkPos.z, true);
                    BlockEntity be = destinationLevel.getBlockEntity(destinationPos);
                    if (be instanceof PortalControllerBlockEntity destController) {
                        destController.executeAtPortalSpace(pos1 -> destinationLevel.setBlock(pos1, Blocks.AIR.defaultBlockState(), 2));
                        BlockState destState = destinationLevel.getBlockState(destinationPos);
                        destinationLevel.setBlock(destinationPos, destState.setValue(PortalControllerBlock.ACTIVE, false), 2);
                        destController.validate(destinationLevel, destinationLevel.getBlockState(destinationPos), destinationPos, true, false);
                        serverLevel.setChunkForced(destChunkPos.x, destChunkPos.z, false);
                    }
                }
                BlockEntity be = serverLevel.getBlockEntity(pos);
                if (be instanceof PortalControllerBlockEntity controller) {
                    executeAtPortalSpace(pos1 -> this.level.setBlock(pos1, Blocks.AIR.defaultBlockState(), 2));
                    serverLevel.setBlock(pos, state.setValue(PortalControllerBlock.ACTIVE, false), 2);
                    controller.validate(serverLevel, getBlockState(), pos, true, false);
                }
            }
        }
    }

    public void tryActivate(String address) {
        if (address != null && !address.isEmpty()) {
            PGPortalAddress.DecodedAddress decoded = PGPortalAddress.getLocation(address);
            if (decoded != null) {
                tryActivate(new ChunkPos(decoded.chunkX(), decoded.chunkZ()), decoded.dimension().toString());
            }
        }
    }

    private void executeAtPortalSpace(Consumer<BlockPos> consumer) {
        Direction direction = getBlockState().getValue(PortalControllerBlock.FACING).getClockWise();
        BlockPos emptyStart = getBlockPos().relative(direction).above();

        BlockPos.betweenClosed(emptyStart, emptyStart.above(3).relative(direction.getOpposite(), 2))
                .forEach(consumer);
    }

    public void tryActivate(ChunkPos destinationPos, String dimension) {
        if (level instanceof ServerLevel serverLevel) {
            if (destinationPos != null && dimension != null) {
                BlockState state = getBlockState();
                if (matchesShape(level, getBlockPos(), true, false) && !state.getValue(PortalControllerBlock.ACTIVE)) {
                    BlockEntity be = serverLevel.getBlockEntity(getBlockPos());
                    if (be instanceof PortalControllerBlockEntity controller) {
                        if (controller.matchesShape(serverLevel, getBlockPos(), true, false)) {

                            ResourceKey<Level> key = LevelHelper.getWorldKey(new ResourceLocation(dimension));
                            ServerLevel destinationLevel = LevelHelper.getServerWorld(serverLevel, key);
                            if (destinationLevel != null) {
                                destinationLevel.setChunkForced(destinationPos.x, destinationPos.z, true);
                                LevelChunk chunk = destinationLevel.getChunk(destinationPos.x, destinationPos.z);

                                //Find destination portal
                                for (Map.Entry<BlockPos, BlockEntity> entry : chunk.getBlockEntities().entrySet()) {
                                    if (entry.getValue() instanceof PortalControllerBlockEntity destinationController && !destinationController.getBlockPos().equals(this.getBlockPos())) {
                                        boolean matchesShape = destinationController.matchesShape(destinationLevel, destinationController.getBlockPos(), true, false);
                                        if (matchesShape && !destinationController.isActive()) {
                                            this.setDestination(destinationController.getBlockPos(), dimension);
                                            destinationController.setDestination(this.getBlockPos(), serverLevel.dimension().location().toString());

                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected void setDestination(BlockPos controllerPos, String dimension) {
        if (level instanceof ServerLevel) {
            this.destinationPos = controllerPos;
            this.destinationDim = dimension;

            level.setBlock(getBlockPos(), getBlockState().setValue(PortalControllerBlock.ACTIVE, true), 2);

            BlockState state = getBlockState();
            Direction direction = state.getValue(PortalControllerBlock.FACING).getClockWise();
            BlockState portalState = PGBlocks.PORTAL.defaultBlockState().setValue(PortalBlock.AXIS, direction.getAxis());

            executeAtPortalSpace(pos1 -> {
                level.setBlock(pos1, portalState, 18);

                BlockEntity be = level.getBlockEntity(pos1);
                if (be instanceof PortalBlockEntity portal) {
                    portal.setOwnerControllerPos(getBlockPos());
                    portal.setDestinationControllerPos(controllerPos);
                    portal.setDestinationDimension(dimension);
                }

            });
        }
    }

    public boolean isActive() {
        AtomicBoolean isAir = new AtomicBoolean(false); //TODO Make this actually set the blockstate
        executeAtPortalSpace(pos -> {
            BlockState state = level.getBlockState(pos);
            if (state.is(Blocks.AIR)) {
                isAir.set(true);
            }
        });
        return getBlockState().getValue(PortalControllerBlock.ACTIVE) && !isAir.get();
    }

    private boolean isCorrectBlock(int num, BlockState portalState) {
        return switch (num) {
            case 1 -> portalState.is(PGBlocks.PORTAL_FRAME);
            case 2 -> portalState.is(PGBlocks.PORTAL_CONTROLLER);
            default -> portalState.is(Blocks.AIR) || portalState.is(PGBlocks.PORTAL);
        };
    }

    public void validate(Level level, BlockState state, BlockPos pos, boolean checkAttached, boolean failOnAttached) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof PortalControllerBlockEntity controller) {
            if (matchesShape(level, pos, checkAttached, failOnAttached)) {
                Direction direction = state.getValue(PortalControllerBlock.FACING).getClockWise();
                BlockPos corner = getLowerLeft(state, pos);
                for (int i = 0; i < portalShape.length; i++) {
                    for (int j = 0; j < portalShape[i].length; j++) {
                        BlockPos framePos = corner.above(i).relative(direction.getOpposite(), j);
                        BlockState frameState = level.getBlockState(framePos);
                        if (frameState.is(PGBlocks.PORTAL_FRAME)) {
                            controller.addFrame(framePos);

                            level.setBlock(framePos, frameState.setValue(PortalFrameBlock.ATTACHED, true), 2);

                            BlockEntity be1 = level.getBlockEntity(framePos);
                            if (be1 instanceof PortalFrameBlockEntity frameEntity) {
                                frameEntity.setMasterPos(pos);
                            }
                        }
                    }
                }
                level.setBlock(pos, state.setValue(PortalControllerBlock.ATTACHED, true), 2);
            } else {
                level.setBlock(pos, state.setValue(PortalControllerBlock.ATTACHED, false), 2);
                controller.setFramesAttached(false);
            }
        }
    }

    public boolean matchesShape(Level level, BlockPos pos, boolean checkAttached, boolean failOnAttached) {
        boolean matches = true;
        BlockState state = level.getBlockState(pos);
        if (state.is(PGBlocks.PORTAL_CONTROLLER)) {
            Direction direction = state.getValue(PortalControllerBlock.FACING).getClockWise();
            BlockPos leftCorner = getLowerLeft(state, pos);
            for (int y = 0; y < portalShape.length; y++) {
                for (int x = 0; x < portalShape[y].length; x++) {
                    BlockPos relative = leftCorner.relative(direction.getOpposite(), x).above(y);
                    BlockState portalState = level.getBlockState(relative);
                    boolean correctDir = (portalState.hasProperty(PortalFrameBlock.FACING) &&
                            portalState.getValue(PortalFrameBlock.FACING).equals(state.getValue(PortalFrameBlock.FACING))) || !portalState.hasProperty(PortalFrameBlock.FACING);

                    if (checkAttached) {
                        if (portalState.hasProperty(PortalFrameBlock.ATTACHED) && portalState.getValue(PortalFrameBlock.ATTACHED)) {
                            matches = !failOnAttached;
                            if (!matches) {
                                break;
                            }
                        }
                    }
                    if (!isCorrectBlock(portalShape[y][x], portalState) || !correctDir) {
                        matches = false;
                        break;
                    }
                }
            }
            return matches;
        }
        return false;
    }

    public BlockPos getLowerLeft(BlockState state, BlockPos pos) {
        Direction direction = state.getValue(PortalFrameBlock.FACING).getClockWise();
        return pos.relative(direction, 2);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (tag.contains("DestControllerPos")) {
            long packed = tag.getLong("DestControllerPos");
            if (packed == 0L) destinationPos = null;
            else destinationPos = BlockPos.of(packed);
        }

        if (tag.contains("DestControllerDim")) {
            destinationDim = tag.getString("DestControllerDim");
        }

        framePositions.clear();
        if (tag.contains("FramePos")) {
            ListTag listTag = tag.getList("FramePos", Tag.TAG_LONG);
            listTag.forEach(tag1 -> framePositions.add(BlockPos.of(((LongTag) tag1).getAsLong())));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if (destinationPos != null) {
            tag.putLong("DestControllerPos", destinationPos.asLong());
        }

        tag.putString("DestControllerDim", destinationDim);

        ListTag listTag;
        if (tag.contains("FramePos")) {
            listTag = tag.getList("FramePos", Tag.TAG_LONG);
        } else listTag = new ListTag();
        framePositions.forEach(pos -> listTag.add(LongTag.valueOf(pos.asLong())));
        tag.put("FramePos", listTag);

    }

    public void setFramesAttached(boolean attached) {
        framePositions.forEach(pos -> {
            BlockState state = level.getBlockState(pos);
            if (state.hasProperty(PortalFrameBlock.ATTACHED)) {
                level.setBlock(pos, state.setValue(PortalFrameBlock.ATTACHED, attached), 2);
            }
        });
    }

    public void baseTick() {
        if (!level.isClientSide) {
            BlockState state = getBlockState();
            BlockPos pos = getBlockPos();
            BlockEntity be = level.getBlockEntity(pos.above());
            boolean isActive = state.getValue(PortalControllerBlock.ACTIVE);
            if (isActive && be instanceof PortalBlockEntity portal) {
                Entity entity = tpQueue.poll();
                if (entity != null) {
                    portal.teleportEntity(entity);
                }
            }
            if (!isActive && !tpQueue.isEmpty()) {
                tpQueue.clear();
            }

            if (isActive != isActive()) {
                level.setBlock(pos, state.setValue(PortalControllerBlock.ACTIVE, isActive()), 2);
                if (!isActive()) {
                    this.disconnect();
                }
            }
        }
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState blockState, T t) {
        ((PortalControllerBlockEntity) t).baseTick();
    }
}
