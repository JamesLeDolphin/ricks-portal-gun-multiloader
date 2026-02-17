package com.jdolphin.ricksportalgun.common.block;

import com.jdolphin.ricksportalgun.common.blockentity.PortalControllerBlockEntity;
import com.jdolphin.ricksportalgun.common.blockentity.PortalFrameBlockEntity;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.List;

public class PortalFrameBlock extends DirectionalBlock implements EntityBlock {
    public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;

    public PortalFrameBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH).setValue(ATTACHED, false));
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide) {
            if (!state.getBlock().equals(oldState.getBlock())) {
                List<BlockEntity> blockEntities = LevelHelper.getBlockEntitiesInChunks((ServerLevel) level, new ChunkPos(pos), 1);
                for (BlockEntity be : blockEntities) {
                    if (be instanceof PortalControllerBlockEntity controller) {
                        if (!controller.getBlockState().getValue(PortalControllerBlock.ATTACHED)) {
                            controller.validate(level, controller.getBlockState(), controller.getBlockPos(), true, true);
                        }
                    }
                }
            }
        }
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ATTACHED, FACING);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.getBlock().equals(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof PortalFrameBlockEntity frame) {
                BlockPos masterPos = frame.getMasterPos();
                if (masterPos != null) {
                    BlockEntity be1 = level.getBlockEntity(masterPos);
                    if (be1 instanceof PortalControllerBlockEntity controller) {
                        controller.validate(level, level.getBlockState(masterPos), masterPos, true, true);
                    }
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
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
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return PGBlockEntities.PORTAL_FRAME.create(pos, state);
    }
}
