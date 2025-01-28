package com.jdolphin.ricksportalgun.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class AbstractBaseScreen extends Screen {

    protected AbstractBaseScreen(Component title) {
        super(title);
    }

    public boolean isPauseScreen() {
        return false;
    }

    public void renderBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.renderMenuBackground(context);
    }

    public  <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget) {
        return super.addRenderableWidget(widget);
    }
}
