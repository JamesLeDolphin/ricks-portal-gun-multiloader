package com.jdolphin.ricksportalgun.common.block;

import com.jdolphin.ricksportalgun.common.blockentity.PortalControllerBlockEntity;
import com.jdolphin.ricksportalgun.common.blockentity.PortalFrameBlockEntity;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PortalFrameBlock extends DirectionalBlock implements EntityBlock {
    public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;

    public PortalFrameBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH).setValue(ATTACHED, false));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!level.isClientSide) {
            List<BlockEntity> blockEntities = LevelHelper.getBlockEntitiesInChunks((ServerLevel) level, new ChunkPos(pos), 1);
            blockEntities.stream().filter(blockEntity -> blockEntity instanceof PortalControllerBlockEntity)
                    .forEach(be -> {
                        PortalControllerBlockEntity controller = ((PortalControllerBlockEntity) be);
                if (!controller.isAttached()) {
                    PortalControllerBlock controllerBlock = (PortalControllerBlock) controller.getBlockState().getBlock();
                    controllerBlock.checkValid(level, controller.getBlockState(), controller.getBlockPos());
                }
            });
        }
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ATTACHED, FACING);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof PortalFrameBlockEntity frame) {
            BlockPos masterPos = frame.getMasterPos();
            if (masterPos != null) {
                BlockState masterState = level.getBlockState(masterPos);
                level.setBlock(masterPos, masterState.setValue(ATTACHED, false), 2);

                BlockEntity be1 = level.getBlockEntity(masterPos);
                if (be1 instanceof PortalControllerBlockEntity controller) {
                    controller.setAttached(false);
                }
            }
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
        return PGBlockEntities.PORTAL_FRAME.create(pos, state);
    }
}
