package com.jdolphin.ricksportalgun.common.menu;

import com.jdolphin.ricksportalgun.common.init.PGMenuTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class PortalDispenserMenu extends AbstractContainerMenu {

    public PortalDispenserMenu(int containerId) {
        super(PGMenuTypes.PORTAL_DISPENSER, containerId);
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
