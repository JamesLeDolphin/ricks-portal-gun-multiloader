package com.jdolphin.ricksportalgun.client.screen.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class PGTooltipText extends AbstractWidget {
    private int color = 16777215 | Mth.ceil(this.alpha * 255.0F) << 24;
    private final Font font;

    public PGTooltipText(int x, int y, Component message, Font font, int color, Component tooltip) {
        super(x, y, font.width(message), font.lineHeight, message);
        this.font = font;
        setColor(color);
        this.setTooltip(Tooltip.create(tooltip));
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        Component component = this.getMessage();
        Font font = this.font;
        int i = this.getX();
        int j = this.getY() + (this.getHeight() - 9) / 2;
        guiGraphics.drawString(font, component, i, j, this.getColor());
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
    }
}