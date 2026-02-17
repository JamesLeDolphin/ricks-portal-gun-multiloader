package com.jdolphin.ricksportalgun.common.block;

import com.jdolphin.ricksportalgun.common.blockentity.PortalDialerBlockEntity;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import com.jdolphin.ricksportalgun.common.packet.clientbound.CBOpenDialerGuiPacket;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;


public class PortalDialerBlock extends Block implements EntityBlock {

    public PortalDialerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult result = super.use(state, level, pos, player, hand, hit);
        if (result == InteractionResult.PASS) {
            if (player instanceof ServerPlayer serverPlayer) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof PortalDialerBlockEntity dialer) {
                    PGHelper.sendPacketToClient(serverPlayer, new CBOpenDialerGuiPacket(dialer.getControllerPos()));
                }
            }
        }
        return result;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return PGBlockEntities.PORTAL_DIALER.create(pos, state);
    }
}
