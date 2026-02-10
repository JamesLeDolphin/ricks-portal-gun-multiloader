package com.jdolphin.ricksportalgun.common.block;

import com.jdolphin.ricksportalgun.common.blockentity.PortalControllerBlockEntity;
import com.jdolphin.ricksportalgun.common.blockentity.PortalFrameBlockEntity;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import com.jdolphin.ricksportalgun.common.init.PGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public class PortalControllerBlock extends DirectionalBlock implements EntityBlock {
    public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;
    private final int[][] portalShape = new int[][]{
            new int[]{1, 1, 2, 1, 1},
            new int[]{1, 0, 0, 0, 1},
            new int[]{1, 0, 0, 0, 1},
            new int[]{1, 0,0, 0, 1},
            new int[]{1, 0, 0, 0, 1},
            new int[]{1, 1, 1, 1, 1},
    };

    private Block getState(int num) {
        return switch (num) {
            case 1 -> PGBlocks.PORTAL_FRAME;
            case 2 -> PGBlocks.PORTAL_CONTROLLER;
            default -> Blocks.AIR;
        };
    }

    public PortalControllerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH).setValue(ATTACHED, false));
    }

    public boolean matchesShape(Level level, BlockState state, BlockPos pos) {
        boolean matches = true;
        Direction direction = state.getValue(FACING).getClockWise();
        BlockPos leftCorner = getLowerLeft(state, pos);
        for (int y = 0; y <portalShape.length; y++) {
            for (int x = 0; x < portalShape[y].length; x++) {
                BlockPos relative = leftCorner.relative(direction.getOpposite(), x).above(y);
                BlockState portalState = level.getBlockState(relative);
                Block block = getState(portalShape[y][x]);
                boolean correctDir = (portalState.hasProperty(FACING) && portalState.getValue(FACING).equals(state.getValue(FACING))) || !portalState.hasProperty(FACING);
                if (!portalState.is(block) || !correctDir) {
                    matches = false;
                }
            }
        }
        return matches;
    }

    public BlockPos getLowerLeft(BlockState state, BlockPos pos) {
        Direction direction = state.getValue(FACING).getClockWise();
        return pos.relative(direction, 2);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ATTACHED);
    }

    public void checkValid(Level level, BlockState state, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof PortalControllerBlockEntity controller) {
            if (matchesShape(level, state, pos)) {
                Direction direction = state.getValue(FACING).getClockWise();
                level.setBlock(pos, state.setValue(ATTACHED, true), 2);
                BlockPos corner = getLowerLeft(state, pos);
                for (int i = 0; i < portalShape.length; i++) {
                    for (int j = 0; j < portalShape[i].length; j++) {
                        BlockPos framePos = corner.above(i).relative(direction.getOpposite(), j);
                        BlockState frameState = level.getBlockState(framePos);
                        if (frameState.is(PGBlocks.PORTAL_FRAME)) {
                            controller.addFrame(framePos);
                            BlockEntity be1 = level.getBlockEntity(framePos);
                            level.setBlock(framePos, frameState.setValue(ATTACHED, true), 2);
                            if (be1 instanceof PortalFrameBlockEntity frameEntity) {
                                frameEntity.setMasterPos(pos);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!level.isClientSide) {
            checkValid(level, state, pos);
        }
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof PortalControllerBlockEntity controller) {
            controller.setAttached(false);
        }
        super.destroy(level, pos, state);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return PGBlockEntities.PORTAL_CONTROLLER.create(pos, state);
    }
}
