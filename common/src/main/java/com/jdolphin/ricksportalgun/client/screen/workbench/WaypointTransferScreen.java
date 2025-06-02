package com.jdolphin.ricksportalgun.client.screen.workbench;

import com.jdolphin.ricksportalgun.client.screen.widget.WaypointListWidget;
import com.jdolphin.ricksportalgun.common.menu.workbench.WaypointTransferMenu;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class WaypointTransferScreen extends AbstractContainerScreen<WaypointTransferMenu> {
    public static final ResourceLocation BG = PGHelper.createLocation("textures/gui/workbench/waypoint_transfer.png");
    private WaypointListWidget leftWaypointList, rightWaypointList;

    public WaypointTransferScreen(WaypointTransferMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 211;
        this.imageWidth = 212;
        this.inventoryLabelX = this.imageWidth - 200;
        this.inventoryLabelY = this.imageHeight - 93;
        this.titleLabelX = this.imageWidth - 205;
        this.titleLabelY = this.imageHeight - 207;
    }

    public void init() {
        super.init();

        LocalPlayer player = minecraft.player;
        ItemStack stack = player.getMainHandItem();

        this.leftWaypointList = this.addRenderableWidget(new WaypointListWidget(this, this.imageWidth / 3, this.imageHeight / 2, this.imageWidth / 3,
                50, 16, stack, false, 60, 16));
    }

    public void containerTick() {
        super.containerTick();

    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        if (this.leftWaypointList != null) this.leftWaypointList.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit(RenderType::guiTextured, BG, x + 12, y, 0f, 0f, imageWidth, imageHeight, 256, 256);
    }
}
