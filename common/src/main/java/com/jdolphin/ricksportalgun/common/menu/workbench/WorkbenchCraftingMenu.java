package com.jdolphin.ricksportalgun.common.menu.workbench;

import com.jdolphin.ricksportalgun.common.init.PGMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class WorkbenchCraftingMenu extends AbstractWorkbenchMenu {
    private ContainerData data;
    private ContainerLevelAccess access;
    private Container container;

    public WorkbenchCraftingMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(10), new SimpleContainerData(2), ContainerLevelAccess.NULL);
    }

    public WorkbenchCraftingMenu(int i, Inventory inventory, Container container, ContainerData data, ContainerLevelAccess access) {
        super(PGMenuTypes.WORKBENCH_CRAFTING, i, access);
        this.container = container;
        this.data = data;
        this.access = access;
        this.addDataSlots(data);
        checkContainerDataCount(data, 2);
        checkContainerSize(container, 10);

        addInventoryExtendedSlots(inventory, 25, 129);
        addInventoryHotbarSlots(inventory, 25, 187);

        createInventory(container);
    }

    private void createInventory(Container container) {
        this.addSlot(new Slot(container, 0, 71, 31));
        this.addSlot(new Slot(container, 1, 123, 31));
        this.addSlot(new Slot(container, 2, 71, 54));
        this.addSlot(new Slot(container, 3, 123, 54));
        this.addSlot(new Slot(container, 4, 97, 84) {

            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
    }

    public int getProgress() {
        return data.get(0);
    }

    public int getScaledProgress() {
        int progress = getProgress();
        int maxProgress = Math.max(getMaxProgress(), 1);
        int progressArrowSize = 46;
        return progress * progressArrowSize / maxProgress;
    }

    public int getMaxProgress() {
        return data.get(1);
    }

    public boolean isCrafting() {
        return getProgress() > 0;
    }

    public ContainerData getData() {
        return data;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        Slot fromSlot = getSlot(i);
        ItemStack fromStack = fromSlot.getItem();

        if (fromStack.getCount() <= 0)
            fromSlot.set(ItemStack.EMPTY);

        if (!fromSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack copyFromStack = fromStack.copy();

        if (i < 36) {
            // We are inside of the player's inventory
            if (!moveItemStackTo(fromStack, 36, 41, false))
                return ItemStack.EMPTY;
        } else if (i < 41) {
            // We are inside of the block entity inventory
            if (!moveItemStackTo(fromStack, 0, 36, false))
                return ItemStack.EMPTY;
        } else {
            System.err.println("Invalid slot index: " + i);
            return ItemStack.EMPTY;
        }

        fromSlot.setChanged();
        fromSlot.onTake(player, fromStack);
        return copyFromStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }
}
