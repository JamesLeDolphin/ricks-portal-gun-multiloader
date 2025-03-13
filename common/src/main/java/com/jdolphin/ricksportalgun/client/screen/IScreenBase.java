package com.jdolphin.ricksportalgun.client.screen;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

public interface IScreenBase {

    <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget);
}
