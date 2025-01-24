package com.jdolphin.ricksportalgun.client.gui.workbench;

import com.jdolphin.ricksportalgun.common.blockentity.GunWorkbenchBlockEntity;
import com.jdolphin.ricksportalgun.common.menu.WorkbenchCraftingMenu;
import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class WorkbenchCraftingScreen extends AbstractContainerScreen<WorkbenchCraftingMenu> {
    public static final ResourceLocation CRAFT_BG = Helper.createLocation("textures/gui/workbench/crafting.png");

    public WorkbenchCraftingScreen(WorkbenchCraftingMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 211;
        this.imageWidth = 212;
        this.inventoryLabelX = this.imageWidth - 200;
        this.inventoryLabelY = this.imageHeight - 93;
        this.titleLabelX = this.imageWidth - 207;
        this.titleLabelY = this.imageHeight - 204;
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new ImageButton(this.width / 2 + 80, this.height / 2 - 55,
                24, 24, 0, 77, GuiHelper.BUTTONS_LOCATION, button -> {}));

        this.addRenderableWidget(new ImageButton(this.width / 2 + 80, this.height / 2 - 80,
                24, 24, 24, 77, GuiHelper.BUTTONS_LOCATION, (button) -> {
            SBOpenMenuPacket packet = new SBOpenMenuPacket(this.menu.getBlockEntity().getBlockPos(), GunWorkbenchBlockEntity.MenuType.WAYPOINT_TRANSFER);
            PGPackets.INSTANCE.sendToServer(packet);
        }));
        this.addRenderableWidget(new ImageButton(this.width / 2 + 80
                , this.height / 2 - 104,
                24, 24, 48, 77, GuiHelper.BUTTONS_LOCATION, (button) -> {
            SBOpenMenuPacket packet = new SBOpenMenuPacket(this.menu.getBlockEntity().getBlockPos(), GunWorkbenchBlockEntity.MenuType.SKIN_SELECTOR);
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
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CRAFT_BG);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit(CRAFT_BG, x, y, 0, 0, imageWidth, imageHeight);
        renderProgressArrow(pGuiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            if (menu.getScaledProgress() < 66) {
                guiGraphics.blit(CRAFT_BG, x + 16, y + 49, 0, 223, 66, menu.getScaledProgress());
                guiGraphics.blit(CRAFT_BG, x + 16, y + 49, 0, 223, 66, menu.getScaledProgress());
            }
        }
    }
}