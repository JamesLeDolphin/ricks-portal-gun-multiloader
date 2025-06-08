package com.jdolphin.ricksportalgun.client.screen;

import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;

public abstract class AbstractBaseScreen extends Screen {
    public static ResourceLocation BG_LOCATION = PGHelper.createLocation("textures/gui/pg_background.png");
    public static int TEXT_RED = ARGB.color(200, 0, 0);
    public static int HIGHLIGHT_RED = ARGB.color(255, 0, 0);
    public static int BG_RED = ARGB.color(100, 0, 0);

    protected AbstractBaseScreen(Component title) {
        super(title);
    }

    protected AbstractBaseScreen(String title) {
        this(Component.translatable(title));
    }

    public boolean isPauseScreen() {
        return false;
    }

    public void renderBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.renderMenuBackground(context);
    }
}
