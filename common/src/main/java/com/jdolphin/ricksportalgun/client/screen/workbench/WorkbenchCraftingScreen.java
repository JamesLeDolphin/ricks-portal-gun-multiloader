package com.jdolphin.ricksportalgun.client.screen.workbench;

import com.jdolphin.ricksportalgun.common.menu.workbench.WorkbenchCraftingMenu;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBSetWorkbenchTypePacket;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class WorkbenchCraftingScreen extends AbstractContainerScreen<WorkbenchCraftingMenu> {
    public static final ResourceLocation CRAFT_BG = PGHelper.createLocation("textures/gui/workbench/crafting.png");

    public WorkbenchCraftingScreen(WorkbenchCraftingMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 211;
        this.imageWidth = 212;
        this.inventoryLabelX = this.imageWidth - 188;
        this.inventoryLabelY = this.imageHeight - 93;
        this.titleLabelX = this.imageWidth - 194;
        this.titleLabelY = this.imageHeight - 204;
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableWidget(Button.builder(Component.literal("TEst"), button -> {
            SBSetWorkbenchTypePacket packet = new SBSetWorkbenchTypePacket(1);
            PGHelper.sendPacketToServer(packet);
        }).bounds(this.imageWidth / 2, this.imageHeight / 2, 16, 16).build());
    }

    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit(RenderType::guiTextured, CRAFT_BG, x + 12, y, 0f, 0f, imageWidth, imageHeight, 256, 256);
        renderProgressArrow(pGuiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            if (menu.getScaledProgress() < 66) {
               guiGraphics.blit(RenderType::guiTextured, CRAFT_BG, x + 16, y + 49, 0, 223, 66, menu.getScaledProgress(), 256 ,256);
               guiGraphics.blit(RenderType::guiTextured, CRAFT_BG, x + 16, y + 49, 0, 223, 66, menu.getScaledProgress(), 256 ,256);
            }
        }
    }
}