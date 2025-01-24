package com.jdolphin.ricksportalgun.common.menu;

import com.jdolphin.ricksportalgun.common.blockentity.GunWorkbenchBlockEntity;
import com.jdolphin.ricksportalgun.common.init.PGBlocks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public class WorkbenchCraftingMenu extends AbstractContainerMenu {
    GunWorkbenchBlockEntity workbench;
    private final ContainerData data;

    public WorkbenchCraftingMenu(int i, Inventory inventory, GunWorkbenchBlockEntity blockEntity, ContainerData data) {
        super(PGMenus.WB_CRAFTING.get(), i);
        this.workbench = blockEntity;
        this.data = data;
        this.init(inventory, blockEntity);
    }

    public WorkbenchCraftingMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory, (GunWorkbenchBlockEntity) inventory.player.level().getBlockEntity(buf.readBlockPos()), new SimpleContainerData(8));
    }

    public GunWorkbenchBlockEntity getBlockEntity() {
        return workbench;
    }

    private void init(Inventory inv, GunWorkbenchBlockEntity blockEntity) {
        this.createPlayerHotbar(inv);
        this.createPlayerInventory(inv);
        this.createBlockEntityInventory(blockEntity);

        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);  // Max Progress
        int progressArrowSize = 33; // This is the height in pixels of your arrow
        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    private void createBlockEntityInventory(GunWorkbenchBlockEntity be) {
        be.getOptional().ifPresent(inventory -> {
            addSlot(new SlotItemHandler(inventory,
                            0,
                            59,
                            31));

            addSlot(new Slot(inventory,
                            1,
                            59,
                            54));

            addSlot(new Slot(inventory,
                    2,
                    111,
                    31));

            addSlot(new Slot(inventory,
                    3,
                    111,
                    54));

            addSlot(new Slot(inventory,
                    4,
                    85,
                    84));
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
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        Slot fromSlot = getSlot(pIndex);
        ItemStack fromStack = fromSlot.getItem();

        if (fromStack.getCount() <= 0)
            fromSlot.set(ItemStack.EMPTY);

        if (!fromSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack copyFromStack = fromStack.copy();

        if (pIndex < 36) {
            // We are inside of the player's inventory
            if (!moveItemStackTo(fromStack, 36, 40, false))
                return ItemStack.EMPTY;
        } else if (pIndex < 40) {
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
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(pPlayer.level(), this.workbench.getBlockPos()), pPlayer, PGBlocks.GUN_WORKBENCH);
    }
}