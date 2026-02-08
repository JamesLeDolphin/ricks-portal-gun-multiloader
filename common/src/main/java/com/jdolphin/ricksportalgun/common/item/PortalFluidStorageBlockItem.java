package com.jdolphin.ricksportalgun.common.item;

import com.jdolphin.ricksportalgun.common.blockentity.PortalFluidStorageBlockEntity;
import com.jdolphin.ricksportalgun.common.init.PGBlocks;
import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class PortalFluidStorageBlockItem extends BlockItem {

    public PortalFluidStorageBlockItem(Properties properties) {
        super(PGBlocks.PORTAL_FLUID_TANK, properties);
    }


    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof PortalFluidStorageBlockEntity storage) {
            CompoundTag tag = stack.getOrCreateTag();
            int fuel = tag.getInt(PGNbtKeys.TAG_FUEL);
            storage.setAmount(fuel);
        }
        return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
    }
}
