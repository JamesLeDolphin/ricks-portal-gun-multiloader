package com.jdolphin.ricksportalgun.common.blockentity;

import com.jdolphin.ricksportalgun.common.init.PGBlockEntities;
import com.jdolphin.ricksportalgun.common.menu.PortalDispenserMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PortalDispenserBlockEntity extends BaseContainerBlockEntity {
    private ItemStack stack;
    public PortalDispenserBlockEntity(BlockPos pos, BlockState blockState) {
        super(PGBlockEntities.PORTAL_DISPENSER, pos, blockState);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("menu.ricksportalgun.portal_dispenser");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return NonNullList.of(stack);
    }

    @Override
    protected void setItems(NonNullList<ItemStack> list) {
        this.stack = list.getFirst();
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new PortalDispenserMenu(i, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return 1;
    }
}
