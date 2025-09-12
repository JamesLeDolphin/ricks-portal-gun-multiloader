package com.jdolphin.ricksportalgun.client.screen.workbench;

import com.jdolphin.ricksportalgun.client.screen.widget.PGItemButton;
import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.menu.workbench.WorkbenchCraftingMenu;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;

public class WorkbenchCraftingScreen extends AbstractWorkbenchScreen<WorkbenchCraftingMenu> {
    public static final ResourceLocation CRAFT_BG = PGHelper.id("textures/gui/workbench/crafting.png");

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
        PGItemButton skin = this.addRenderableWidget(new PGItemButton(this.width / 2 + 91, this.height / 2 - 105, 24, 24,
                Component.translatable("menu.ricksportalgun.workbench.skin"), button -> setScreen(1), PGItems.PORTAL_GUN.getDefaultInstance()));
        skin.setRenderBackground(false);

        PGItemButton waypoint = this.addRenderableWidget(new PGItemButton(this.width / 2 + 91, this.height / 2 - 80, 24, 24,
                Component.translatable("menu.ricksportalgun.workbench.waypoint"), button -> setScreen(0), PGItems.DATA_CARD.getDefaultInstance()));
        waypoint.setRenderBackground(false);

        PGItemButton craft = this.addRenderableWidget(new PGItemButton(this.width / 2 + 91, this.height / 2 - 55, 24, 24,
                Component.translatable("menu.ricksportalgun.workbench.craft"), button -> {
        }, Items.CRAFTING_TABLE.getDefaultInstance()));
        craft.setRenderBackground(false);

        GuiHelper.setTooltip(skin, Component.translatable("menu.ricksportalgun.workbench.skin"));
        GuiHelper.setTooltip(waypoint, Component.translatable("menu.ricksportalgun.workbench.waypoint"));
        GuiHelper.setTooltip(craft, Component.translatable("menu.ricksportalgun.workbench.craft"));
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(graphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit(CRAFT_BG, x + 12, y, 0f, 0f, imageWidth, imageHeight, 256, 256);
        renderProgressArrow(pGuiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            if (menu.getScaledProgress() < 66) {
               guiGraphics.blit(CRAFT_BG, x + 88, y + 36, 222, 210, 34, menu.getScaledProgress(), 256 ,256);
            }
        }
    }
}