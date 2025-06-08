package com.jdolphin.ricksportalgun.client.screen.widget;


import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;

import java.util.function.Consumer;

public class PGImageButton extends AbstractButton {
    private static final WidgetSprites SPRITES = new WidgetSprites(PGHelper.createLocation("widget/button"),
            PGHelper.createLocation("widget/button"), PGHelper.createLocation("widget/button_hovered"));

    protected final ResourceLocation texture;
    protected final int textureWidth;
    protected final int textureHeight;
    protected final Consumer<AbstractButton> onPress;


    public PGImageButton(int x, int y, int width, int height, Component message, Consumer<AbstractButton> onPress, int textureWidth, int textureHeight, ResourceLocation texture) {
        super(x, y, width, height, message);
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.texture = texture;
        this.onPress = onPress;
    }

    public void renderString(GuiGraphics graphics, Font textRenderer, int color) {}

    @Override
    public void onPress() {
        this.onPress.accept(this);
    }

    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        graphics.blitSprite(RenderType::guiTextured, SPRITES.get(true, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight(), ARGB.white(this.alpha));

        int i = this.getX() + this.getWidth() / 2 - this.textureWidth / 2;
        int j = this.getY() + this.getHeight() / 2 - this.textureHeight / 2;
        graphics.blitSprite(RenderType::guiTextured, this.texture, i, j, this.textureWidth, this.textureHeight);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}