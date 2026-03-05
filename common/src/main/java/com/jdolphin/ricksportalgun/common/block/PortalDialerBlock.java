package com.jdolphin.ricksportalgun.common.block;

import com.jdolphin.ricksportalgun.common.blockentity.PortalControllerBlockEntity;
import com.jdolphin.ricksportalgun.common.blockentity.PortalDialerBlockEntity;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import com.jdolphin.ricksportalgun.common.packet.clientbound.CBOpenDialerGuiPacket;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;


public class PortalDialerBlock extends Block implements EntityBlock {
    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");

    public PortalDialerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(CONNECTED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            if (player instanceof ServerPlayer serverPlayer && hand.equals(InteractionHand.MAIN_HAND)) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof PortalDialerBlockEntity dialer && isConnected(level, pos)) {
                    BlockPos masterPos = dialer.getControllerPos();
                    if (masterPos != null) {
                        BlockEntity master = level.getBlockEntity(masterPos);
                        if (master instanceof PortalControllerBlockEntity controller) {
                            if (controller.matchesShape(level, masterPos, true, false)) {
                                PGHelper.sendPacketToClient(serverPlayer, new CBOpenDialerGuiPacket(dialer.getControllerPos()));
                            }
                        }
                    } else {
                        player.sendSystemMessage(Component.literal("Disconnected"));
                        dialer.setControllerPos(null);
                    }
                }
            }
        return super.use(state, level, pos, player, hand, hit);
    }

    public boolean isConnected(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.getValue(CONNECTED);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(CONNECTED, false);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONNECTED);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof PortalDialerBlockEntity dialer && dialer.getControllerPos() != null) {
            BlockPos masterPos = dialer.getControllerPos();
            int distance = (int) Math.round(Math.sqrt(masterPos.distSqr(pos)));
            if (distance > 16) {
                if (placer instanceof Player player) {
                    player.sendSystemMessage(Component.literal("too far lol"));
                }
            }
        }
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return PGBlockEntities.PORTAL_DIALER.create(pos, state);
    }
}
