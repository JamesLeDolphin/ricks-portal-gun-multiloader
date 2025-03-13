package com.jdolphin.ricksportalgun.common.block;

import com.jdolphin.ricksportalgun.common.blockentity.PortalDispenserBlockEntity;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import com.jdolphin.ricksportalgun.common.packet.CBOpenGuiPacket;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SubetherBarrierBlock extends BaseEntityBlock {
    public static final BooleanProperty ACTIVE = BlockStateProperties.ACTIVE;

    public SubetherBarrierBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVE, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(SubetherBarrierBlock::new);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            PGHelper.openScreen(serverPlayer, 0);

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public boolean canBlockPortal(Level level, BlockPos pos) {
        return level.hasNeighborSignal(pos);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return PGBlockEntities.SUBETHER_BARRIER.create(pos, state);
    }
}
