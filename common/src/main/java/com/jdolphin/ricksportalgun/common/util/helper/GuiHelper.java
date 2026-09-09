package com.jdolphin.ricksportalgun.common.util.helper;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.awt.*;

public class GuiHelper {

    public static void renderVertexes(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer consumer, float x1, float x2, float y1, float y2, float z1, float z2,
                                      float red, float green, float blue, float alpha,
                                      float u1, float v1, float u2, float v2,
                                      int overlay, int light,
                                      float normalX, float normalY, float normalZ) {

        renderVertex(consumer, matrix4f, matrix3f, x1, y1, z1, red, green, blue, alpha, u1, v2, overlay, light, normalX, normalY, normalZ);
        renderVertex(consumer, matrix4f, matrix3f, x2, y1, z2, red, green, blue, alpha, u2, v2, overlay, light, normalX, normalY, normalZ);
        renderVertex(consumer, matrix4f, matrix3f, x2, y2, z1, red, green, blue, alpha, u2, v1, overlay, light, normalX, normalY, normalZ);
        renderVertex(consumer, matrix4f, matrix3f, x1, y2, z2, red, green, blue, alpha, u1, v1, overlay, light, normalX, normalY, normalZ);
    }

    public static void renderVertex(VertexConsumer consumer, Matrix4f matrix4f, Matrix3f matrix3f,
                                    float x, float y, float z,
                                    float red, float green, float blue, float alpha,
                                    float u, float v, int overlay, int light,
                                    float normalX, float normalY, float normalZ) {
        consumer.vertex(matrix4f, x, y, z).color(red, green, blue, alpha).uv(u, v).overlayCoords(overlay).uv2(light).normal(matrix3f, normalX, normalY, normalZ).endVertex();
    }

    public static int opaqueColor(int color) {
        return color | -16777216;
    }

    public static void renderWidgets(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick, AbstractWidget... widgets) {
        for (AbstractWidget widget : widgets) {
            if (widget != null) {
                widget.render(graphics, pMouseX, pMouseY, pPartialTick);
            }
        }
    }

    public static void drawWordWrap(GuiGraphics graphics, Font font, FormattedText text, int x, int y, int lineWidth, int color) {
        for(FormattedCharSequence formattedcharsequence : font.split(text, lineWidth)) {
            graphics.drawCenteredString(font, formattedcharsequence, x, y, color);
            y += 9;
        }
    }

    public static void renderScrollingString(GuiGraphics guiGraphics, Component text, int minX, int minY, int maxX, int maxY, int color) {
        renderScrollingString(guiGraphics, text, (minX + maxX) / 2, minX, minY, maxX, maxY, color);
    }

    public static void renderScrollingString(GuiGraphics guiGraphics, Component text, int centerX, int minX, int minY, int maxX, int maxY, int color) {
        Font font = Minecraft.getInstance().font;
        int i = font.width(text);
        int j = (minY + maxY - 9) / 2 + 1;
        int k = maxX - minX;
        if (i > k) {
            int l = i - k;
            double d0 = (double) Util.getMillis() / (double)1000.0F;
            double d1 = Math.max((double)l * (double)0.5F, 3.0F);
            double d2 = Math.sin((Math.PI / 2D) * Math.cos((Math.PI * 2D) * d0 / d1)) / (double)2.0F + (double)0.5F;
            double d3 = Mth.lerp(d2, 0.0F, l);
            guiGraphics.enableScissor(minX, minY, maxX, maxY);
            guiGraphics.drawString(font, text, minX - (int)d3, j, color);
            guiGraphics.disableScissor();
        } else {
            int i1 = Mth.clamp(centerX, minX + i / 2, maxX - i / 2);
            guiGraphics.drawCenteredString(font, text, i1, j, color);
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
