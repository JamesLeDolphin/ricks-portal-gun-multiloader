package com.jdolphin.ricksportalgun.common.menu;

import com.jdolphin.ricksportalgun.common.blockentity.PortalDispenserBlockEntity;
import com.jdolphin.ricksportalgun.common.init.PGMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PortalDispenserMenu extends AbstractContainerMenu {
    private final Container dispenser;

    public PortalDispenserMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(1));
    }

    public PortalDispenserMenu(int containerId, Inventory playerInventory, Container container) {
        super(PGMenuTypes.PORTAL_DISPENSER, containerId);
        checkContainerSize(container, 1);
        this.dispenser = container;
        container.startOpen(playerInventory.player);
        this.addSlot(new Slot(container, 0, 26, 52));
        this.addStandardInventorySlots(playerInventory, 8, 84);
    }

    public boolean stillValid(Player player) {
        return this.dispenser.stillValid(player);
    }

    public PortalDispenserBlockEntity getDispenser() {
        return (PortalDispenserBlockEntity) this.dispenser;
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
