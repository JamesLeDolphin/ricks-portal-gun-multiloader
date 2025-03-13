package com.jdolphin.ricksportalgun.common.menu;

import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.init.PGMenuTypes;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PortalDispenserMenu extends AbstractContainerMenu {
    private final Container dispenser;
    private final ContainerData data;

    public PortalDispenserMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(1), new SimpleContainerData(3));
    }

    public PortalDispenserMenu(int containerId, Inventory playerInventory, Container container, ContainerData data) {
        super(PGMenuTypes.PORTAL_DISPENSER, containerId);
        checkContainerSize(container, 1);
        checkContainerDataCount(data, 3);
        this.data = data;
        this.dispenser = container;
        container.startOpen(playerInventory.player);
        this.addSlot(new Slot(container, 0, 26, 52) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(PGItems.PORTAL_FLUID);
            }
        });
        this.addDataSlots(data);
        this.addStandardInventorySlots(playerInventory, 8, 84);
    }

    public int getFuel() {
        return this.data.get(0);
    }

    public int getMaxFuel() {
        return this.data.get(1);
    }

    public int getColor() {
        return this.data.get(2);
    }

    public boolean stillValid(Player player) {
        return this.dispenser.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack original = slot.getItem();
            itemstack = original.copy();
            if (index == 0) {
                if (!this.moveItemStackTo(original, 1, 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(original, 0, 1, false)) {
                return ItemStack.EMPTY;
            }
            if (original.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (original.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, original);
        }
        return itemstack;
    }
}
