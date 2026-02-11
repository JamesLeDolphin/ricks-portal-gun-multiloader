package com.jdolphin.ricksportalgun.common.block;

import com.jdolphin.ricksportalgun.common.blockentity.PortalControllerBlockEntity;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class PortalControllerBlock extends DirectionalBlock implements EntityBlock {
    public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;


    public PortalControllerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH).setValue(ATTACHED, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ATTACHED);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof PortalControllerBlockEntity controller) {
            controller.validate(level, state, pos, true, false);
            controller.activate();
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);

            if (be instanceof PortalControllerBlockEntity controller) {
                controller.validate(level, state, pos, true, false);
            }
        }
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof PortalControllerBlockEntity controller) {
                controller.setFramesAttached(false);
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
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return PGBlockEntities.PORTAL_CONTROLLER.create(pos, state);
    }
}
