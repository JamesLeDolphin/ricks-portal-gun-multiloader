package com.jdolphin.ricksportalgun.client.screen.portalgun.settings;

import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.PGSlider;
import com.jdolphin.ricksportalgun.client.screen.widget.PGTextButton;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.ARGB;

public class ThemeEditScreen extends AbstractBaseScreen {
    private PGTextButton select, cancel;
    private PGSlider rText, gText,bText, rH, gH, bH, rBG, gBG, bBG;

    public ThemeEditScreen() {
        super(Component.translatable("menu.ricksportalgun.settings.customization.theme"));
    }

    public void init() {
        super.init();
        PortalGunStyle style = getStyle();

        rH = this.addRenderableWidget(new PGSlider(this.width / 2 - 90 , this.height / 2 - 4, 36, 16, Component.translatable("chat.editBox"),
                ARGB.redFloat(style.highlightColor()), 0, 1, false));

        gH = this.addRenderableWidget(new PGSlider(this.width / 2 - 90, this.height / 2 + 20, 36, 16, Component.translatable("chat.editBox"),
                ARGB.greenFloat(style.highlightColor()), 0, 1, false));

        bH = this.addRenderableWidget(new PGSlider(this.width / 2 - 90, this.height / 2 + 44, 36, 16, Component.translatable("chat.editBox"),
                ARGB.blueFloat(style.highlightColor()), 0, 1, false));

        this.select = this.addRenderableWidget(new PGTextButton(this.width / 2 - 136, this.height / 2 + 64,128, 20,
                Component.translatable("ricksportalgun.button.select"), (button) -> {
            //  SBSettingsPacket packet = new SBSettingsPacket(this.portalSize.getValue(), this.portalAge.getValueInt());
            //  PGHelper.sendPacketToServer(packet);
            this.onClose();

        }, this.font));
        this.cancel = this.addRenderableWidget(new PGTextButton( this.width / 2 + 8, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.cancel"), (button) -> this.onClose(), this.font));


        this.select.setTextColour(style.textColor());
        this.cancel.setTextColour(style.textColor());
        rH.setStyle(style);
        gH.setStyle(style);
        bH.setStyle(style);
    }

    private int getTextColor() {
        return 0;
    }

    private int getHighlightColor() {
        try {
            return ARGB.colorFromFloat(1.0f, (float) rH.getValue(), (float) gH.getValue(), (float) bH.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getBackgroundColor() {
        return 0;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        PortalGunStyle style = getStyle();

        graphics.fill(this.width / 2 - 154, this.height / 2 - 110, this.width / 2 + 165, this.height / 2 + 100, style.bgColor());
        graphics.drawCenteredString(this.font, Component.translatable("menu.ricksportalgun.settings.customization.theme"), this.width / 2, 30, style.textColor());
        GuiHelper.renderOutline(graphics, select, style.highlightColor());
        GuiHelper.renderOutline(graphics, cancel, style.highlightColor());

        Style guiStyle = GuiHelper.getStyle(mouseX, mouseY);
        if (guiStyle != null && guiStyle.getHoverEvent() != null) {
            this.renderWithTooltip(graphics, mouseX, mouseY, delta);
        }
        graphics.blit(RenderType::guiTextured, BG_LOCATION, this.width / 2 - 158, this.height / 2 - 115, 0, 0, 330, 224, 330, 224);

        graphics.fill(this.rH.getX() - 16, this.rH.getY() - 10, this.rH.getX() + rH.getWidth() + 16, rH.getY() - 64, this.getHighlightColor());

        super.render(graphics, mouseX, mouseY, delta);
    }


}
