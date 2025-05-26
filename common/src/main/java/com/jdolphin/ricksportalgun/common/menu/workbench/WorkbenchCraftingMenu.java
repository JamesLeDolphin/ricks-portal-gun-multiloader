package com.jdolphin.ricksportalgun.common.menu.workbench;

import com.jdolphin.ricksportalgun.common.init.PGMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;

public class WorkbenchCraftingMenu extends AbstractContainerMenu {

    public WorkbenchCraftingMenu(int i, Inventory inventory, Container container, ContainerData data, ContainerLevelAccess access) {
        super(PGMenuTypes.WORKBENCH_CRAFTING, i);
    }

    public WorkbenchCraftingMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(1), new SimpleContainerData(2), ContainerLevelAccess.NULL);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }
}
