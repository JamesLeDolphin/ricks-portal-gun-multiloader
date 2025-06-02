package com.jdolphin.ricksportalgun.common.block;

import com.jdolphin.ricksportalgun.common.blockentity.GunWorkbenchBlockEntity;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GunWorkbenchBlock extends BaseEntityBlock implements EntityBlock {
    public static final MapCodec<GunWorkbenchBlock> CODEC = simpleCodec(GunWorkbenchBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    public GunWorkbenchBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, PGBlockEntities.GUN_WORKBENCH, GunWorkbenchBlockEntity::tick);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        switch (state.getValue(FACING)) {
            case NORTH -> {
                shape = Shapes.join(shape, Shapes.box(0, 1, 0.8125, 1, 1.75, 0.9375), BooleanOp.OR);
                shape = Shapes.join(shape, Shapes.box(0, 1.75, 0.75, 1, 2, 1), BooleanOp.OR);
            }
            case WEST -> {
                shape = Shapes.join(shape, Shapes.box(0.8125, 1, 0, 0.9375, 1.75, 1), BooleanOp.OR);
                shape = Shapes.join(shape, Shapes.box(0.75, 1.75, 0, 1, 2, 1), BooleanOp.OR);
            }
            case SOUTH -> {
                shape = Shapes.join(shape, Shapes.box(0, 1, 0.0625, 1, 1.75, 0.1875), BooleanOp.OR);
                shape = Shapes.join(shape, Shapes.box(0, 1.75, 0, 1, 2, 0.25), BooleanOp.OR);
            }
            case EAST -> {
                shape = Shapes.join(shape, Shapes.box(0.0625, 1, 0, 0.1875, 1.75, 1), BooleanOp.OR);
                shape = Shapes.join(shape, Shapes.box(0, 1.75, 0, 0.25, 2, 1), BooleanOp.OR);
            }
        }
        return Shapes.join(makeBaseShape(), shape, BooleanOp.OR);
    }

    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        Containers.dropContentsOnDestroy(state, newState, level, pos);
        super.onRemove(state, level, pos, newState, isMoving);
    }

    public VoxelShape makeBaseShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0.25, 0, 1, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.75, 0, 0, 1, 0.25, 0.25), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.75, 0, 0.75, 1, 0.25, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.75, 0.25, 0.25, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 0.25, 0.25, 0.25), BooleanOp.OR);

        return shape;
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof GunWorkbenchBlockEntity workbench) {
                player.openMenu(workbench);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return PGBlockEntities.GUN_WORKBENCH.create(blockPos, blockState);
    }
}
