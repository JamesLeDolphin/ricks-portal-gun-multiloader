package com.jdolphin.ricksportalgun.common.blockentity;

import com.jdolphin.ricksportalgun.common.block.PortalBlock;
import com.jdolphin.ricksportalgun.common.block.PortalControllerBlock;
import com.jdolphin.ricksportalgun.common.block.PortalFrameBlock;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import com.jdolphin.ricksportalgun.common.init.PGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class PortalControllerBlockEntity extends BlockEntity {

    private final List<BlockPos> frames = new ArrayList<>();
    public final int[][] portalShape = new int[][]{
            new int[]{1, 1, 2, 1, 1},
            new int[]{1, 0, 0, 0, 1},
            new int[]{1, 0, 0, 0, 1},
            new int[]{1, 0,0, 0, 1},
            new int[]{1, 0, 0, 0, 1},
            new int[]{1, 1, 1, 1, 1},
    };



    public PortalControllerBlockEntity(BlockPos pos, BlockState blockState) {
        super(PGBlockEntities.PORTAL_CONTROLLER, pos, blockState);
    }

    public void addFrame(BlockPos pos) {
        if (!frames.contains(pos)) frames.add(pos);
    }

    public boolean isAttached() {
        return getBlockState().getValue(PortalControllerBlock.ATTACHED);
    }

    public void removeFrame(BlockPos pos) {
        frames.remove(pos);
    }

    public void activate() {
        if (!level.isClientSide) {
            BlockState state = getBlockState();
            BlockEntity be = level.getBlockEntity(getBlockPos());
            if (be instanceof PortalControllerBlockEntity controller) {
                if (controller.matchesShape(level, state, getBlockPos(), true, false)) {
                    Direction direction = state.getValue(PortalControllerBlock.FACING).getClockWise();
                    BlockPos emptyStart = getBlockPos().relative(direction).above();
                    BlockState portalState = PGBlocks.PORTAL.defaultBlockState().setValue(PortalBlock.AXIS, direction.getAxis());
                    BlockPos.betweenClosed(emptyStart, emptyStart.above(3).relative(direction.getOpposite(), 2))
                            .forEach(pos -> this.level.setBlock(pos, portalState, 2));
                }
            }
        }
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
            if (matchesShape(level, state, pos, checkAttached, failOnAttached)) {
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

    public boolean matchesShape(Level level, BlockState state, BlockPos pos, boolean checkAttached, boolean failOnAttached) {
        boolean matches = true;
        Direction direction = state.getValue(PortalControllerBlock.FACING).getClockWise();
        BlockPos leftCorner = getLowerLeft(state, pos);
        for (int y = 0; y <portalShape.length; y++) {
            for (int x = 0; x < portalShape[y].length; x++) {
                BlockPos relative = leftCorner.relative(direction.getOpposite(), x).above(y);
                BlockState portalState = level.getBlockState(relative);
                boolean correctDir = (portalState.hasProperty(PortalFrameBlock.FACING) &&
                        portalState.getValue(PortalFrameBlock.FACING).equals(state.getValue(PortalFrameBlock.FACING))) || !portalState.hasProperty(PortalFrameBlock.FACING);

                if (checkAttached) {
                    if (portalState.hasProperty(PortalFrameBlock.ATTACHED) && portalState.getValue(PortalFrameBlock.ATTACHED)) {
                        matches = !failOnAttached;
                        break;
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

    public BlockPos getLowerLeft(BlockState state, BlockPos pos) {
        Direction direction = state.getValue(PortalFrameBlock.FACING).getClockWise();
        return pos.relative(direction, 2);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        frames.clear();
        if (tag.contains("FramePos")) {
            ListTag listTag = tag.getList("FramePos", Tag.TAG_LONG);
            listTag.forEach(tag1 -> frames.add(BlockPos.of(((LongTag) tag1).getAsLong())));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        ListTag listTag;
        if (tag.contains("FramePos")) {
            listTag = tag.getList("FramePos", Tag.TAG_LONG);
        } else listTag = new ListTag();
        frames.forEach(pos -> listTag.add(LongTag.valueOf(pos.asLong())));
        tag.put("FramePos", listTag);

    }

    public void setFramesAttached(boolean attached) {
        frames.forEach(pos -> {
            BlockState state = level.getBlockState(pos);
            if (state.hasProperty(PortalFrameBlock.ATTACHED)) {
                level.setBlock(pos, state.setValue(PortalFrameBlock.ATTACHED, attached), 2);
            }
        });
    }
}
