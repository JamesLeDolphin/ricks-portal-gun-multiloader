package com.jdolphin.ricksportalgun.common.menu;

import com.jdolphin.ricksportalgun.common.init.PGMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
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
        this.addStandardInventorySlots(playerInventory, 8, 84);
    }

    public boolean stillValid(Player player) {
        return this.dispenser.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }
}
