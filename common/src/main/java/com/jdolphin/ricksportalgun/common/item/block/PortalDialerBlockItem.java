package com.jdolphin.ricksportalgun.common.item.block;

import com.jdolphin.ricksportalgun.common.blockentity.PortalControllerBlockEntity;
import com.jdolphin.ricksportalgun.common.blockentity.PortalDialerBlockEntity;
import com.jdolphin.ricksportalgun.common.blockentity.PortalFrameBlockEntity;
import com.jdolphin.ricksportalgun.common.init.PGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class PortalDialerBlockItem extends BlockItem {

    public PortalDialerBlockItem(Properties properties) {
        super(PGBlocks.PORTAL_DIALER, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!level.isClientSide) {
            ItemStack stack = context.getItemInHand();
            BlockPos pos = context.getClickedPos();
            BlockState state = level.getBlockState(pos);
            BlockEntity be = level.getBlockEntity(pos);

            CompoundTag tag = stack.getOrCreateTag();
            if (!tag.contains("LinkPos") && ((state.is(PGBlocks.PORTAL_FRAME) || state.is(PGBlocks.PORTAL_CONTROLLER)))) {
                BlockPos linkPos = BlockPos.ZERO;
                if (be instanceof PortalFrameBlockEntity frame && frame.getMasterPos() != null) {
                    linkPos = frame.getMasterPos();
                } else if (be instanceof PortalControllerBlockEntity controller) {
                    linkPos = controller.getBlockPos();
                }

                CompoundTag posTag = NbtUtils.writeBlockPos(linkPos);
                tag.put("LinkPos", posTag);
                return InteractionResult.SUCCESS;
            } else {
                InteractionResult result = super.useOn(context);
                if (result != InteractionResult.FAIL) {
                    tag.remove("LinkPos");
                }
                return result;
            }
        }
        return InteractionResult.PASS;
    }

    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof PortalDialerBlockEntity dialer) {
                CompoundTag tag = stack.getOrCreateTag();
                if (tag.contains("LinkPos")) {
                    BlockPos linkPos = NbtUtils.readBlockPos(tag.getCompound("LinkPos"));
                    dialer.setControllerPos(linkPos);
                    return true;
                }
            }
        }
        return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains("LinkPos");
    }
}
