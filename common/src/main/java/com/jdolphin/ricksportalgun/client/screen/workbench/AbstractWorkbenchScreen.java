package com.jdolphin.ricksportalgun.client.screen.workbench;

import com.jdolphin.ricksportalgun.common.menu.workbench.AbstractWorkbenchMenu;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBSetWorkbenchTypePacket;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class AbstractWorkbenchScreen<T extends AbstractWorkbenchMenu> extends AbstractContainerScreen<T> {

    public AbstractWorkbenchScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    protected void setScreen(int i) {
        SBSetWorkbenchTypePacket packet = new SBSetWorkbenchTypePacket(i);
        PGHelper.sendPacketToServer(packet);
    }

    public boolean isPauseScreen() {
        return false;
    }
}
