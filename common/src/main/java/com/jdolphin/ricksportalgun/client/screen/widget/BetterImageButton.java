package com.jdolphin.ricksportalgun.client.screen.widget;


import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class BetterImageButton extends Button {
    protected final ResourceLocation texture;
    protected final int textureWidth;
    protected final int textureHeight;

    public BetterImageButton(int x, int y, int width, int height, Component message, OnPress onPress, int textureWidth, int textureHeight, ResourceLocation texture) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.texture = texture;
    }

    public void renderString(GuiGraphics context, Font textRenderer, int color) {}

    public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
        int i = this.getX() + this.getWidth() / 2 - this.textureWidth / 2;
        int j = this.getY() + this.getHeight() / 2 - this.textureHeight / 2;
        context.blitSprite(RenderType::guiTextured, this.texture, i, j, this.textureWidth, this.textureHeight);
    }
}