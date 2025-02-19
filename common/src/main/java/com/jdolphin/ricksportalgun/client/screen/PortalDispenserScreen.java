package com.jdolphin.ricksportalgun.client.screen;

import com.jdolphin.ricksportalgun.common.menu.PortalDispenserMenu;
import com.jdolphin.ricksportalgun.common.util.helper.Helper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class PortalDispenserScreen extends AbstractContainerScreen<PortalDispenserMenu> {
    private final PortalDispenserMenu dispenserMenu;
    public static final ResourceLocation CONTAINER_LOCATION = Helper.createLocation("textures/gui/container/portal_dispenser.png");
    public PortalDispenserScreen(PortalDispenserMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.dispenserMenu = menu;
    }

    protected void init() {
        super.init();
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        this.renderTooltip(graphics, mouseX, mouseY);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        int percentage = (52 * 16) /16;
        graphics.fill(i + 8, j + 68, i + 23, j + 68 - percentage, Color.GREEN.getRGB());
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float v, int i, int i1) {
        int i2 = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderType::guiTextured, CONTAINER_LOCATION, i2, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }
}
