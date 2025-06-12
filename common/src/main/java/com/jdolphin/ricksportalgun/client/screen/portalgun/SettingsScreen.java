package com.jdolphin.ricksportalgun.client.screen.portalgun;

import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.portalgun.settings.CustomizationSettingsScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.PGImageButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGTextButton;
import com.jdolphin.ricksportalgun.common.init.PortalGunTypeRegistry;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBChangePortalGunTypePacket;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBOpenCoordGuiPacket;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBOpenSecuritySettingsPacket;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.NotNull;

public class SettingsScreen extends AbstractBaseScreen {
    private PGTextButton security, customization;
    private PGImageButton backButton;
    public SettingsScreen() {
        super("menu.ricksportalgun.settings");
    }

    @Override
    public void init() {
        super.init();

        assert this.minecraft != null;

        this.security = this.addRenderableWidget(new PGTextButton(this.width / 2 - 64, this.height / 2 - 64, 128, 20,
                Component.translatable("ricksportalgun.button.settings.security"), button -> {
            SBOpenSecuritySettingsPacket packet = new SBOpenSecuritySettingsPacket();
            PGHelper.sendPacketToServer(packet);
        }, this.font));

        this.customization = this.addRenderableWidget(new PGTextButton(this.width / 2 - 64, this.height / 2 - 36, 128, 20,
                Component.translatable("ricksportalgun.button.settings.customization"), button -> this.minecraft.setScreen(new CustomizationSettingsScreen()), this.font));

        this.backButton = this.addRenderableWidget(new PGImageButton(this.width / 2 - 140, 26, 20, 20, Component.translatable("ricksportalgun.button.back"),
                (button) -> {
                    SBOpenCoordGuiPacket packet = new SBOpenCoordGuiPacket();
                    PGHelper.sendPacketToServer(packet);
                }, 20, 20, BACK_BUTTON_TEXTURE));

        PortalGunStyle style = getStyle();
        this.security.setTextColour(style.textColor());
        this.customization.setTextColour(style.textColor());
        this.backButton.setColor(style.highlightColor());
        this.backButton.setRenderBackground(false);
        GuiHelper.setTooltip(backButton, Component.translatable("ricksportalgun.button.back"));
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        PortalGunStyle style = getStyle();

        graphics.fill(this.width / 2 - 154, this.height / 2 - 110, this.width / 2 + 165, this.height / 2 + 100, style.bgColor());
        graphics.drawCenteredString(this.font, Component.translatable("menu.ricksportalgun.settings"), this.width / 2, this.height / 4 - 32, style.textColor());

        GuiHelper.renderOutline(graphics, security, style.highlightColor());
        GuiHelper.renderOutline(graphics, customization, style.highlightColor());
        GuiHelper.renderOutline(graphics, backButton, style.highlightColor());

        graphics.blit(RenderType::guiTextured, BG_LOCATION, this.width / 2 - 158, this.height / 2 - 115, 0, 0, 330, 224, 330, 224);

        Style guiStyle = GuiHelper.getStyle(pMouseX, pMouseY);
        if (guiStyle != null && guiStyle.getHoverEvent() != null) {
            this.renderWithTooltip(graphics, pMouseX, pMouseY, pPartialTick);
        }
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
    }
}