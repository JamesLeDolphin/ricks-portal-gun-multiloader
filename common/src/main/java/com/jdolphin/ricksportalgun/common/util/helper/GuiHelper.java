package com.jdolphin.ricksportalgun.common.util.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.awt.*;

public class GuiHelper {

    public static void renderWidgets(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick, AbstractWidget... widgets) {
        for (AbstractWidget widget : widgets) {
            if (widget != null) {
                widget.render(graphics, pMouseX, pMouseY, pPartialTick);
            }
        }
    }

    public static void drawWhiteString(GuiGraphics graphics, String text, int x, int y) {
        graphics.drawString(Minecraft.getInstance().font, Component.literal(text), x, y, Color.WHITE.getRGB());
    }

    public static void drawWhiteString(GuiGraphics graphics, Component text, int x, int y) {
        graphics.drawString(Minecraft.getInstance().font, text, x, y, Color.WHITE.getRGB());
    }

    public static void drawWhiteCenteredString(GuiGraphics graphics, Component text, int x, int y) {
        drawWhiteCenteredString(graphics, text.getString(), x, y);
    }

    public static void drawWhiteCenteredString(GuiGraphics graphics, String text, int x, int y) {
        graphics.drawCenteredString(Minecraft.getInstance().font, text, x, y, Color.WHITE.getRGB());
    }

    public static void renderOutline(GuiGraphics graphics, AbstractWidget widget, int color) {
        if (widget != null) {
            graphics.renderOutline(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(), color);
        }
    }

    public static void setTooltip(AbstractWidget widget, Component component) {
        if (widget != null && component != null) {
            Tooltip tooltip = Tooltip.create(component);
            widget.setTooltip(tooltip);
        }
    }

    public static void renderTooltip(GuiGraphics graphics, Component component, AbstractWidget widget) {
        if (widget.isHovered()) graphics.renderTooltip(Minecraft.getInstance().font, component, widget.getX(), widget.getY());
    }

    public static Style getStyle(int x, int y) {
        return Minecraft.getInstance().gui.getChat().getClickedComponentStyleAt(x, y);
    }
}
