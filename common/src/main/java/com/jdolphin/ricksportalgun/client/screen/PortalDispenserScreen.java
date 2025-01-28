package com.jdolphin.ricksportalgun.client.screen;

import com.jdolphin.ricksportalgun.common.menu.PortalDispenserMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class PortalDispenserScreen extends AbstractContainerScreen<PortalDispenserMenu> {

    public PortalDispenserScreen(PortalDispenserMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float v, int i, int i1) {

    }
}
