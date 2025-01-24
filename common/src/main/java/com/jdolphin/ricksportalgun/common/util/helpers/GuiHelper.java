package com.jdolphin.ricksportalgun.common.util.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.awt.*;

public class GuiHelper {

    public static void renderWidgets(GuiGraphics stack, int pMouseX, int pMouseY, float pPartialTick, AbstractWidget... widgets) {
        for (AbstractWidget widget : widgets) {
            widget.render(stack, pMouseX, pMouseY, pPartialTick);
        }
    }

    public static void drawWhiteString(GuiGraphics stack, String text, int x, int y) {
        stack.drawString(Minecraft.getInstance().font, Component.literal(text), x, y, Color.WHITE.getRGB());
    }

    public static void drawWhiteString(GuiGraphics stack, Component text, int x, int y) {
        stack.drawString(Minecraft.getInstance().font, text, x, y, Color.WHITE.getRGB());
    }

    public static void drawWhiteCenteredString(GuiGraphics stack, Component text, int x, int y) {
        drawWhiteCenteredString(stack, text.getString(), x, y);
    }

    public static void drawWhiteCenteredString(GuiGraphics stack, String text, int x, int y) {
        stack.drawCenteredString(Minecraft.getInstance().font, text, x, y, Color.WHITE.getRGB());
    }

    public static void renderTooltip(GuiGraphics stack, Component component, AbstractWidget widget) {
        if (widget.isHovered()) stack.renderTooltip(Minecraft.getInstance().font, component, widget.getX(), widget.getY());
    }

    public static Style getStyle(int x, int y) {
        return Minecraft.getInstance().gui.getChat().getClickedComponentStyleAt(x, y);
    }
}
