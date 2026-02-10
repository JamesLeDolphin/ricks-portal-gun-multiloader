package com.jdolphin.ricksportalgun.common.block;

import com.jdolphin.ricksportalgun.common.blockentity.PortalFluidStorageBlockEntity;
import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.item.IPortalFluidItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PortalFluidStorageBlock extends Block implements EntityBlock {

    public PortalFluidStorageBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.box(0, 0.1, 0, 16, 16, 16);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof PortalFluidStorageBlockEntity storage) {
                ItemStack stack = player.getItemInHand(hand);
                if (stack.getItem() instanceof IPortalFluidItem fluidItem) {
                    int stored = storage.getAmount();
                    int capacity = storage.getMaxAmount();
                    int itemFluid = fluidItem.getFluid(stack);
                    if (itemFluid > 0 && stored < capacity) {
                        storage.setAmount(stored +  Math.min(capacity - stored, itemFluid));
                        int transferred = storage.getAmount() - stored;
                        if (transferred > 0) {
                            int remainder = itemFluid - transferred;
                            if (remainder > 0) {
                                fluidItem.setAmount(stack, remainder);
                            } else {
                                fluidItem.empty(stack, player, hand);
                            }
                        }
                    }
                }
            }
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof PortalFluidStorageBlockEntity storage) {
            ItemStack stack = PGItems.PORTAL_FLUID_TANK.getDefaultInstance();
            CompoundTag tag = stack.getOrCreateTag();
            tag.putInt(PGNbtKeys.TAG_FUEL, storage.getAmount());
            if (!player.isCreative())
                Block.popResource(level, pos, stack);
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return PGBlockEntities.PORTAL_FLUID_TANK.create(pos, state);
    }
}
