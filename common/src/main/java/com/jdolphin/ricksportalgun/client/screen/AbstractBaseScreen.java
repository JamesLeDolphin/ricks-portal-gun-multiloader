package com.jdolphin.ricksportalgun.client.screen;

import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public abstract class AbstractBaseScreen extends Screen {
    public static ResourceLocation BG_LOCATION = PGHelper.createLocation("textures/gui/pg_background.png");
    public static ResourceLocation BACK_BUTTON_TEXTURE = PGHelper.createLocation("textures/gui/sprites/icon/arrow_back.png");
    public static int WHITE = Color.WHITE.getRGB();

    protected AbstractBaseScreen(Component title) {
        super(title);
    }

    protected AbstractBaseScreen(String title) {
        this(Component.translatable(title));
    }

    public boolean isPauseScreen() {
        return false;
    }

    protected PortalGunStyle getStyle() {
        assert this.minecraft != null;
        Player player = this.minecraft.player;
        if (player != null) {
            ItemStack stack = player.getMainHandItem();
            return stack.getOrDefault(PGDataComponents.PORTAL_GUN_STYLE, PortalGunStyle.DEFAULT);
        }
        return PortalGunStyle.DEFAULT;
    }


    public void renderBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.renderMenuBackground(context);
    }
}
