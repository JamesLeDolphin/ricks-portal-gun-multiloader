package com.jdolphin.ricksportalgun.client.gui.workbench;

import com.jdolphin.ricksportalgun.common.menu.WaypointTransferMenu;
import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class WaypointTransferScreen extends AbstractContainerScreen<WaypointTransferMenu> {
    public static final ResourceLocation WAYPOINT_BG = Helper.createLocation("textures/gui/workbench/waypoint_transfer.png");

    private WaypointListWidget leftWaypointList;

    public WaypointTransferScreen(WaypointTransferMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 211;
        this.imageWidth = 212;
        this.inventoryLabelX = this.imageWidth - 200;
        this.inventoryLabelY = this.imageHeight - 93;
        this.titleLabelX = this.imageWidth - 205;
        this.titleLabelY = this.imageHeight - 207;
    }

    public void containerTick() {
        super.containerTick();

        ItemStack oldStack = leftWaypointList.getStack();
        ItemStack newStack = this.menu.getStackInSlot(36);
        if (!ItemStack.isSameItem(oldStack, newStack)) {
            leftWaypointList.setStack(newStack);
            leftWaypointList.refreshEntries();
        }
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableWidget(new ImageButton(this.width / 2 + 80, this.height / 2 - 55,
                24, 24, 0, 77, GuiHelper.BUTTONS_LOCATION, (button) -> {
            SBOpenMenuPacket packet = new SBOpenMenuPacket(this.menu.getBlockEntity().getBlockPos(), WorkbenchBlockEntity.MenuType.CRAFTING);
            PGPackets.INSTANCE.sendToServer(packet);
        }));

        this.addRenderableWidget(new ImageButton(this.width / 2 + 80, this.height / 2 - 80,
                24, 24, 24, 77, GuiHelper.BUTTONS_LOCATION, button -> {}));

        this.addRenderableWidget(new ImageButton(this.width / 2 + 80, this.height / 2 - 104,
                24, 24, 48, 77, GuiHelper.BUTTONS_LOCATION, (button) -> {
            SBOpenMenuPacket packet = new SBOpenMenuPacket(this.menu.getBlockEntity().getBlockPos(), WorkbenchBlockEntity.MenuType.SKIN_SELECTOR);
            PGPackets.INSTANCE.sendToServer(packet);
        }));

        leftWaypointList = this.addWidget(new WaypointListWidget(this.menu.getStackInSlot(36),
                this.width / 2, this.height, this.height - 218, this.height - 112, 20));
        leftWaypointList.setScrollBarOffset(36);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {

        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.leftWaypointList.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        pGuiGraphics.blit(WAYPOINT_BG, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }
}