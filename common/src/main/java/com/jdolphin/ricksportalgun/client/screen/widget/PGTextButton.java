package com.jdolphin.ricksportalgun.client.screen.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;

public class PGTextButton extends Button {
    private final Font font;
    private final Component message;
    private final Component underlinedMessage;
    protected int textColour = 16777215 | Mth.ceil(this.alpha * 255.0F) << 24;

    public PGTextButton(int x, int y, int width, int height, Component message, OnPress onPress, Font font) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.font = font;
        this.message = message;
        this.underlinedMessage = ComponentUtils.mergeStyles(message.copy(), Style.EMPTY.withUnderlined(true));
    }

    @Override
    public void onPress() {
        super.onPress();
    }

    public int getTextColour() {
        return textColour;
    }

    public void setTextColour(int textColour) {
        this.textColour = textColour;
    }

    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        Component component = this.isHoveredOrFocused() ? this.underlinedMessage : this.message;
        renderScrollingString(graphics, this.font, component, this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), textColour);
    }
}
