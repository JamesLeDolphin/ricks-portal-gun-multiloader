package com.jdolphin.ricksportalgun.client.screen.portalgun.settings;

import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.PGImageButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGSlider;
import com.jdolphin.ricksportalgun.client.screen.widget.PGTextButton;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBSetPortalGunStylePacket;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;

import java.awt.*;
import java.util.function.Consumer;

public class ThemeEditScreen extends AbstractBaseScreen {
    private PGTextButton select, cancel;
    private PGSlider rText, gText,bText, rH, gH, bH, rBG, gBG, bBG;
    private PGImageButton backButton;

    public ThemeEditScreen() {
        super(Component.translatable("menu.ricksportalgun.settings.customization.theme"));
    }

    public void init() {
        super.init();
        assert minecraft != null;
        PortalGunStyle style = getStyle();

        rH = this.addRenderableWidget(new PGSlider(this.width / 2 - 90,this.height / 2 + 8, 36, 12, Component.translatable("chat.editBox"),
                FastColor.ARGB32.red(style.highlightColor()), 0, 255, false));

        gH = this.addRenderableWidget(new PGSlider(this.width / 2 - 90, this.height / 2 + 28, 36, 12, Component.translatable("chat.editBox"),
                FastColor.ARGB32.green(style.highlightColor()), 0, 255, false));

        bH = this.addRenderableWidget(new PGSlider(this.width / 2 - 90, this.height / 2 + 48, 36, 12, Component.translatable("chat.editBox"),
                FastColor.ARGB32.blue(style.highlightColor()), 0, 255, false));


        rText = this.addRenderableWidget(new PGSlider(this.width / 2 - 18, this.height / 2 + 8, 36, 12, Component.translatable("chat.editBox"),
                FastColor.ARGB32.red(style.textColor()), 0, 255, false));

        gText = this.addRenderableWidget(new PGSlider(this.width / 2 - 18, this.height / 2 + 28, 36, 12, Component.translatable("chat.editBox"),
                FastColor.ARGB32.green(style.textColor()), 0, 255, false));

        bText = this.addRenderableWidget(new PGSlider(this.width / 2 - 18, this.height / 2 + 48, 36, 12, Component.translatable("chat.editBox"),
                FastColor.ARGB32.blue(style.textColor()), 0, 255, false));

        rBG = this.addRenderableWidget(new PGSlider(this.width / 2 + 54, this.height / 2 + 8, 36, 12, Component.translatable("chat.editBox"),
                FastColor.ARGB32.red(style.bgColor()), 0, 255, false));

        gBG = this.addRenderableWidget(new PGSlider(this.width / 2 + 54, this.height / 2 + 28, 36, 12, Component.translatable("chat.editBox"),
                FastColor.ARGB32.green(style.bgColor()), 0, 255, false));

        bBG = this.addRenderableWidget(new PGSlider(this.width / 2 + 54, this.height / 2 + 48, 36, 12, Component.translatable("chat.editBox"),
                FastColor.ARGB32.blue(style.bgColor()), 0, 255, false));

        this.select = this.addRenderableWidget(new PGTextButton(this.width / 2 - 136, this.height / 2 + 64,128, 20,
                Component.translatable("ricksportalgun.button.select"), (button) -> {
            SBSetPortalGunStylePacket packet = new SBSetPortalGunStylePacket(new PortalGunStyle(getHighlightColor(), getBackgroundColor(), getTextColor()));
            PGHelper.sendPacketToServer(packet);
            this.onClose();

        }, this.font));
        this.cancel = this.addRenderableWidget(new PGTextButton( this.width / 2 + 8, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.cancel"), (button) -> this.onClose(), this.font));

        this.backButton = this.addRenderableWidget(new PGImageButton(this.width / 2 - 140, this.height / 2 - 96, 20, 20, Component.translatable("ricksportalgun.button.back"),
                (button) -> minecraft.setScreen(new CustomizationSettingsScreen()), 20, 20, BACK_BUTTON_TEXTURE));

        this.select.setTextColour(style.textColor());
        this.cancel.setTextColour(style.textColor());
        this.backButton.setColor(style.highlightColor());
        this.backButton.setRenderBackground(false);
        doAll(pgSlider -> pgSlider.setStyle(style));
        doAll(pgSlider -> pgSlider.setRenderBG(false));
        GuiHelper.setTooltip(backButton, Component.translatable("ricksportalgun.button.back"));
    }

    private void doAll(Consumer<PGSlider> function) {
        function.accept(rH);
        function.accept(gH);
        function.accept(bH);
        function.accept(rBG);
        function.accept(gBG);
        function.accept(bBG);
        function.accept(rText);
        function.accept(gText);
        function.accept(bText);
    }

    private int getTextColor() {
        try {
            return FastColor.ARGB32.color(255, (int) rText.getValue(), (int) gText.getValue(), (int) bText.getValue());
        } catch (Exception e) {
            PGConstants.LOGGER.warn(e.getMessage());
        }
        return 0x000000;
    }

    private int getHighlightColor() {
        try {
            return FastColor.ARGB32.color(255, (int) rH.getValue(), (int) gH.getValue(), (int) bH.getValue());
        } catch (Exception e) {
            PGConstants.LOGGER.warn(e.getMessage());
        }
        return 0x000000;
    }

    private int getBackgroundColor() {
        try {
            return FastColor.ARGB32.color(255, (int) rBG.getValue(), (int) gBG.getValue(), (int) bBG.getValue());
        } catch (Exception e) {
            PGConstants.LOGGER.warn(e.getMessage());
        }
        return 0x000000;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        PortalGunStyle style = getStyle();

        graphics.fill(this.width / 2 - 154, this.height / 2 - 110, this.width / 2 + 165, this.height / 2 + 100, style.bgColor());
        graphics.drawCenteredString(this.font, Component.translatable("menu.ricksportalgun.settings.customization.theme"), this.width / 2, this.height / 2 - 92, style.textColor());

        GuiHelper.renderOutline(graphics, select, style.highlightColor());
        GuiHelper.renderOutline(graphics, cancel, style.highlightColor());
        GuiHelper.renderOutline(graphics, backButton, style.highlightColor());

        graphics.drawString(this.font, Component.translatable("ricksportalgun.r"), rH.getX() - 16, rH.getY() + 5, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.g"), gH.getX() - 16, gH.getY() + 5, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.b"), bH.getX() - 16, bH.getY() + 5, style.textColor());

        graphics.drawString(this.font, Component.translatable("ricksportalgun.r"), rText.getX() - 16, rText.getY() + 5, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.g"), gText.getX() - 16, gText.getY() + 5, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.b"), bText.getX() - 16, bText.getY() + 5, style.textColor());

        graphics.drawString(this.font, Component.translatable("ricksportalgun.r"), rBG.getX() - 16, rBG.getY() + 5, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.g"), gBG.getX() - 16, gBG.getY() + 5, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.b"), bBG.getX() - 16, bBG.getY() + 5, style.textColor());

        graphics.drawCenteredString(this.font, Component.translatable("ricksportalgun.button.customization.highlight"), rH.getX() + rH.getWidth() / 2, rH.getY() - 16, style.textColor());
        graphics.drawCenteredString(this.font, Component.translatable("ricksportalgun.button.customization.text"), rText.getX() + rText.getWidth() / 2, rText.getY() - 16, style.textColor());
        graphics.drawCenteredString(this.font, Component.translatable("ricksportalgun.button.customization.background"), rBG.getX() + rBG.getWidth() / 2, rBG.getY() - 16, style.textColor());


        Style guiStyle = GuiHelper.getStyle(mouseX, mouseY);
        if (guiStyle != null && guiStyle.getHoverEvent() != null) {
            this.renderWithTooltip(graphics, mouseX, mouseY, delta);
        }
        RenderSystem.enableBlend();
        graphics.blit(BG_LOCATION, this.width / 2 - 158, this.height / 2 - 115, 0, 0, 330, 224, 330, 224);
        RenderSystem.disableBlend();

        graphics.fill(rH.getX(), rH.getY() - 22, rText.getX() + rText.getWidth() / 2, rH.getY() - 76, this.getHighlightColor());
        graphics.fill(rText.getX() + rText.getWidth() / 2, rText.getY() - 22, rBG.getX() + rBG.getWidth(), rBG.getY() - 76, this.getBackgroundColor());
        graphics.drawCenteredString(this.font, Component.translatable("menu.ricksportalgun.settings.customization.theme.example"), this.width / 2, this.height / 2 - 48, getTextColor());

        int width = (rBG.getX() + rBG.getWidth()) - rH.getX();
        int height = (rH.getY() - 76) - rH.getY() + 22;
        graphics.renderOutline(rH.getX(), rH.getY() - 22, width, height, Color.BLACK.getRGB());

        super.render(graphics, mouseX, mouseY, delta);
    }


}
