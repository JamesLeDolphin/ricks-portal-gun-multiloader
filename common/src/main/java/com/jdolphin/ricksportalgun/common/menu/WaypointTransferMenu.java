package com.jdolphin.ricksportalgun.common.menu;

import com.jdolphin.ricksportalgun.common.blockentity.GunWorkbenchBlockEntity;
import com.jdolphin.ricksportalgun.common.init.PGBlocks;
import com.jdolphin.ricksportalgun.common.item.IWaypointStorage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class WaypointTransferMenu extends AbstractContainerMenu {
    GunWorkbenchBlockEntity workbench;
    public WaypointTransferMenu(int i, Inventory inventory, GunWorkbenchBlockEntity be, ContainerData data) {
        super(PGMenus.WB_WAYPOINT_TRANSFER.get(), i);
        this.workbench = be;
        this.init(inventory, be);
    }

    public WaypointTransferMenu(int i, Inventory inventory, FriendlyByteBuf buf) {
        super(PGMenus.WB_WAYPOINT_TRANSFER.get(), i);
        GunWorkbenchBlockEntity be = (GunWorkbenchBlockEntity) inventory.player.level().getBlockEntity(buf.readBlockPos());
        this.workbench = be;
        this.init(inventory, be);
    }

    private void init(Inventory inv, GunWorkbenchBlockEntity blockEntity) {
        this.createPlayerHotbar(inv);
        this.createPlayerInventory(inv);
        this.createBlockEntityInventory(blockEntity);
    }

    public GunWorkbenchBlockEntity getBlockEntity() {
        return workbench;
    }

    public ItemStack getStackInSlot(int slotId) {
        return this.getSlot(slotId).getItem();
    }

    private void createBlockEntityInventory(GunWorkbenchBlockEntity be) {
        be.getOptional().ifPresent(inventory -> {
            addSlot(
                    new SlotItemHandler(inventory,
                            5,
                            85,
                            44) {

                        public boolean mayPlace(@NotNull ItemStack stack) {
                            return stack.getItem() instanceof IWaypointStorage && !this.hasItem();
                        }
                    });

            addSlot(
                    new SlotItemHandler(inventory,
                            6,
                            85,
                            73) {

                        public boolean mayPlace(@NotNull ItemStack stack) {
                            return stack.getItem() instanceof IWaypointStorage && !this.hasItem();
                        }

                    });
        });
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

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        Slot fromSlot = getSlot(pIndex);
        ItemStack fromStack = fromSlot.getItem();

        if (fromStack.getCount() <= 0)
            fromSlot.set(ItemStack.EMPTY);

        if (!fromSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack copyFromStack = fromStack.copy();

        if (pIndex < 36) {
            // We are inside of the player's inventory
            if (!moveItemStackTo(fromStack, 36, 38, false))
                return ItemStack.EMPTY;
        } else if (pIndex > 36) {
            // We are inside of the block entity inventory
            if (!moveItemStackTo(fromStack, 0, 36, false))
                return ItemStack.EMPTY;
        } else {
            System.err.println("Invalid slot index: " + pIndex);
            return ItemStack.EMPTY;
        }

        fromSlot.setChanged();
        fromSlot.onTake(pPlayer, fromStack);
        return copyFromStack;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(pPlayer.level(), this.workbench.getBlockPos()), pPlayer, PGBlocks.GUN_WORKBENCH);
    }
}