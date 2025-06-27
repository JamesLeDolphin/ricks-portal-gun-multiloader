package com.jdolphin.ricksportalgun.client.screen;

import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public abstract class AbstractBaseScreen extends Screen {
    public static ResourceLocation BG_LOCATION = PGHelper.createLocation("textures/gui/pg_background.png");
    public static ResourceLocation BACK_BUTTON_TEXTURE = PGHelper.createLocation("textures/gui/sprites/icon/arrow_back.png");

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
        ItemStack stack = getItemStack();
        return stack.getOrDefault(PGDataComponents.PORTAL_GUN_STYLE, PortalGunStyle.DEFAULT);
    }


    protected ItemStack getItemStack() {
        assert this.minecraft != null && minecraft.player != null;
        Player player = this.minecraft.player;
        InteractionHand hand = player.getUsedItemHand();
        return player.getItemInHand(hand);
    }

    public void renderBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.renderMenuBackground(context);
    }

    protected void drawOverlay(GuiGraphics graphics) {
        graphics.blit(RenderType::guiTextured, BG_LOCATION, this.width / 2 - 158, this.height / 2 - 115, 0, 0, 330, 224, 330, 224);
    }

    protected void fillBackgroundColor(GuiGraphics graphics) {
        PortalGunStyle style = getStyle();
        graphics.fill(this.width / 2 - 154, this.height / 2 - 110, this.width / 2 + 165, this.height / 2 + 100, style.bgColor());
    }
}
