package com.jdolphin.ricksportalgun.common.menu;

import com.jdolphin.ricksportalgun.common.blockentity.PortalDispenserBlockEntity;
import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.init.PGMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PortalDispenserMenu extends AbstractContainerMenu {
    private final Container dispenser;
    private final ContainerData data;
    private final ContainerLevelAccess access;

    public PortalDispenserMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(1), new SimpleContainerData(2), ContainerLevelAccess.NULL);
    }

    public PortalDispenserMenu(int containerId, Inventory playerInventory, Container container, ContainerData data, ContainerLevelAccess access) {
        super(PGMenuTypes.PORTAL_DISPENSER, containerId);
        checkContainerSize(container, 1);
        checkContainerDataCount(data, 2);
        this.data = data;
        this.access = access;
        this.dispenser = container;
        container.startOpen(playerInventory.player);
        this.addSlot(new Slot(container, 0, 26, 52) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(PGItems.PORTAL_FLUID);
            }
        });
        this.addDataSlots(data);
        //this.addStandardInventorySlots(playerInventory, 8, 84);
    }

    public int getFuel() {
        return this.data.get(0);
    }

    public int getMaxFuel() {
        return this.data.get(1);
    }

    public void setCoords(BlockPos pos, String dim) {
        this.access.execute((level, pos1) -> {
            BlockEntity entity = level.getBlockEntity(pos1);
            if (entity instanceof PortalDispenserBlockEntity disp) {
                disp.setDestination(dim, pos);
            }
        });
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
