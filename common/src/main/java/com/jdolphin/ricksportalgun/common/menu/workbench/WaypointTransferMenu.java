package com.jdolphin.ricksportalgun.common.menu.workbench;

import com.jdolphin.ricksportalgun.common.init.PGMenuTypes;
import com.jdolphin.ricksportalgun.common.item.IWaypointStorage;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class WaypointTransferMenu extends AbstractWorkbenchMenu {
    private final Container container;
    private final ContainerData data;
    private final ContainerLevelAccess access;

    public WaypointTransferMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(10), new SimpleContainerData(2), ContainerLevelAccess.NULL);
    }

    public WaypointTransferMenu(int i, Inventory inventory, Container container, ContainerData data, ContainerLevelAccess access) {
        super(PGMenuTypes.WORKBENCH_WAYPOINT_TRANSFER, i, access);
        this.data = data;
        this.access = access;
        this.container = container;
        this.addDataSlots(data);
        checkContainerSize(container, 10);
        checkContainerDataCount(data, 2);

        addInventoryExtendedSlots(inventory, 25, 129);
        addInventoryHotbarSlots(inventory, 25, 187);
        addSlots(container);
    }

    protected void addSlots(Container container) {
        this.addSlot(new Slot(container, 5, 97, 44) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof IWaypointStorage;
            }
        });
        this.addSlot(new Slot(container, 6, 97, 73) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof IWaypointStorage;
            }
        });
    }

    public void editWaypoints(Waypoint waypoint, boolean side, boolean copy, boolean remove) {
        this.access.execute(((level, pos) -> {
            if (!level.isClientSide) {
                ItemStack left = this.getSlot(36).getItem();
                ItemStack right = this.getSlot(37).getItem();
                //True for left side, false for right side
                if (copy) {
                    if (side) IWaypointStorage.addWaypoint(right, waypoint);
                    else IWaypointStorage.addWaypoint(left, waypoint);
                } else if (remove) {
                    if (side) IWaypointStorage.deleteWaypoint(left, waypoint);
                    else IWaypointStorage.deleteWaypoint(right, waypoint);
                } else {
                    if (side) {
                        IWaypointStorage.addWaypoint(right, waypoint);
                        IWaypointStorage.deleteWaypoint(left, waypoint);
                    }
                    else {
                        IWaypointStorage.addWaypoint(left, waypoint);
                        IWaypointStorage.deleteWaypoint(right, waypoint);
                    }
                }
            }
        }));
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
            if (!moveItemStackTo(fromStack, 36, 38, false))
                return ItemStack.EMPTY;
        } else if (i < 38) {
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
