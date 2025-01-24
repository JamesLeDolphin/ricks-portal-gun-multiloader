package com.jdolphin.ricksportalgun.client.gui.workbench;

import com.jdolphin.ricksportalgun.common.menu.SkinSelectorMenu;
import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SkinSelectionScreen extends AbstractContainerScreen<SkinSelectorMenu> {
    public static final ResourceLocation BG = Helper.createLocation("textures/gui/workbench/skin_select.png");

    public SkinSelectionScreen(SkinSelectorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 211;
        this.imageWidth = 212;
        this.inventoryLabelX = this.imageWidth - 200;
        this.inventoryLabelY = this.imageHeight - 93;
        this.titleLabelX = this.imageWidth - 205;
        this.titleLabelY = this.imageHeight - 207;
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableWidget(new ImageButton(this.width / 2 + 80, this.height / 2 - 55,
                24, 24, 0, 77, GuiHelper.BUTTONS_LOCATION, (button) -> {
                    SBOpenMenuPacket packet = new SBOpenMenuPacket(this.menu.getBlockEntity().getBlockPos(),
                            WorkbenchBlockEntity.MenuType.CRAFTING);
                    PGPackets.INSTANCE.sendToServer(packet);
                }));

        this.addRenderableWidget(new ImageButton(this.width / 2 + 80, this.height / 2 - 80,
                24, 24, 24, 77, GuiHelper.BUTTONS_LOCATION, button -> {
                    SBOpenMenuPacket packet = new SBOpenMenuPacket(this.menu.getBlockEntity().getBlockPos(),
                            WorkbenchBlockEntity.MenuType.WAYPOINT_TRANSFER);
                    PGPackets.INSTANCE.sendToServer(packet);
                }));

        this.addRenderableWidget(new ImageButton(this.width / 2 + 80, this.height / 2 - 104,
                24, 24, 48, 77, GuiHelper.BUTTONS_LOCATION, (button) -> {
                    SBOpenMenuPacket packet = new SBOpenMenuPacket(this.menu.getBlockEntity().getBlockPos(),
                            WorkbenchBlockEntity.MenuType.SKIN_SELECTOR);
                    PGPackets.INSTANCE.sendToServer(packet);
                }));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        pGuiGraphics.blit(BG, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }
}