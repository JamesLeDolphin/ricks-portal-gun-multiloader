package com.jdolphin.ricksportalgun.client.screen.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class PGItemButton extends AbstractButton {
    protected Consumer<AbstractButton> onPress;
    protected ItemStack stack;
    protected boolean renderBG = true;

    public PGItemButton(int x, int y, int width, int height, Component message, Consumer<AbstractButton> onPress, ItemStack stack) {
        super(x, y, width, height, message);
        this.onPress = onPress;
        this.stack = stack;
    }

    public void setRenderBackground(boolean renderBG) {
        this.renderBG = renderBG;
    }

    @Override
    public void renderString(GuiGraphics guiGraphics, Font font, int color) {}

    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (renderBG) super.renderWidget(graphics, mouseX, mouseY, delta);

        int x = this.getX() + (this.getWidth() / 2 - 8);
        int y = this.getY() + (this.getHeight() / 2 - 8);
        graphics.renderFakeItem(stack, x, y);
    }

    @Override
    public void onPress() {
        this.onPress.accept(this);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }
}
