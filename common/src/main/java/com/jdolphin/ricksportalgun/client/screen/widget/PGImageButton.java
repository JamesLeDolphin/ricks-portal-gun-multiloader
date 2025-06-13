package com.jdolphin.ricksportalgun.client.screen.widget;


import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.function.Consumer;

public class PGImageButton extends AbstractButton {
    protected final ResourceLocation texture;
    protected final int textureWidth;
    protected final int textureHeight;
    protected final Consumer<AbstractButton> onPress;
    protected boolean renderBg = true;
    protected int color = (this.active ? 16777215 : 10526880) | Mth.ceil(this.alpha * 255.0F) << 24;

    public PGImageButton(int x, int y, int width, int height, Component message, Consumer<AbstractButton> onPress, int textureWidth, int textureHeight, ResourceLocation texture) {
        super(x, y, width, height, message);
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.texture = texture;
        this.onPress = onPress;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setRenderBackground(boolean renderBg) {
        this.renderBg = renderBg;
    }

    public void renderString(GuiGraphics graphics, Font textRenderer, int color) {}

    @Override
    public void onPress() {
        if (this.active) this.onPress.accept(this);
    }

    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (this.active) {
            if (this.renderBg) {
                super.renderWidget(graphics, mouseX, mouseY, delta);
            }
            int i = this.getX() + (this.getWidth() / 2 - this.textureWidth / 2);
            int j = this.getY() + (this.getHeight() / 2 - this.textureHeight / 2);
            graphics.blit(RenderType::guiTextured, this.texture, i, j, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight, this.color);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}