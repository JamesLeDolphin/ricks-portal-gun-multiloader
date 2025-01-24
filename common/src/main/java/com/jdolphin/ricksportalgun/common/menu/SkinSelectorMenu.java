package com.jdolphin.ricksportalgun.common.menu;

import com.jdolphin.ricksportalgun.common.blockentity.GunWorkbenchBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SkinSelectorMenu extends AbstractContainerMenu {
    GunWorkbenchBlockEntity workbench;

    public SkinSelectorMenu(int i, Inventory inventory, GunWorkbenchBlockEntity blockEntity, ContainerData data) {
        super(PGMenus.WB_SKIN_CHOICE.get(), i);
        this.workbench = blockEntity;
        this.init(inventory, workbench);
    }

    public SkinSelectorMenu(int i, Inventory inventory, FriendlyByteBuf buf) {
        super(PGMenus.WB_SKIN_CHOICE.get(), i);
        GunWorkbenchBlockEntity be = (GunWorkbenchBlockEntity) inventory.player.level().getBlockEntity(buf.readBlockPos());
        this.workbench = be;
        this.init(inventory, be);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    public GunWorkbenchBlockEntity getBlockEntity() {
        return workbench;
    }

    private void init(Inventory inv, GunWorkbenchBlockEntity blockEntity) {
        this.createPlayerHotbar(inv);
        this.createPlayerInventory(inv);
        //this.createBlockEntityInventory(blockEntity);
    }

    private void createPlayerInventory(Inventory playerInv) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInv,
                        9 + column + (row * 9),
                        13 + (column * 18),
                        129 + (row * 18)));
            }
        }
    }

    private void createPlayerHotbar(Inventory playerInv) {
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInv,
                    column,
                    13 + (column * 18),
                    187));
        }
    }
}