package com.jdolphin.ricksportalgun.client.screen.portalgun;

import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.portalgun.settings.CustomizationSettingsScreen;
import com.jdolphin.ricksportalgun.client.screen.portalgun.settings.UpgradesInfoScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.PGImageButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGTextButton;
import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBOpenCoordGuiPacket;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBOpenSecuritySettingsPacket;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

public class SettingsScreen extends AbstractBaseScreen {
    private PGTextButton security, customization, upgrades;
    private PGImageButton backButton;
    public SettingsScreen() {
        super("menu.ricksportalgun.settings");
    }

    @Override
    public void init() {
        super.init();

        assert this.minecraft != null;

        ItemStack stack = getItemStack();
        CompoundTag tag = stack.getOrCreateTag();

        if (PGHelper.checkTagBoolean(tag, PGNbtKeys.SETTINGS)) {
            this.security = this.addRenderableWidget(new PGTextButton(this.width / 2 - 64, this.height / 2 - 38, 128, 20,
                    Component.translatable("ricksportalgun.button.settings.security"), button -> {
                SBOpenSecuritySettingsPacket packet = new SBOpenSecuritySettingsPacket();
                PGHelper.sendPacketToServer(packet);
            }, this.font));
        }

        this.upgrades = this.addRenderableWidget(new PGTextButton(this.width / 2 - 64, this.height / 2 - 10, 128, 20,
                Component.translatable("menu.ricksportalgun.settings.upgrades"), button -> this.minecraft.setScreen(new UpgradesInfoScreen()), this.font));

        if (PGHelper.checkTagBoolean(tag, PGNbtKeys.SETTINGS)) {
            this.customization = this.addRenderableWidget(new PGTextButton(this.width / 2 - 64, this.height / 2 + 18, 128, 20,
                    Component.translatable("ricksportalgun.button.settings.customization"), button -> this.minecraft.setScreen(new CustomizationSettingsScreen()), this.font));
        }
        this.backButton = this.addRenderableWidget(new PGImageButton(this.width / 2 - 140, this.height / 2 - 96, 20, 20, Component.translatable("ricksportalgun.button.back"),
                (button) -> {
                    SBOpenCoordGuiPacket packet = new SBOpenCoordGuiPacket();
                    PGHelper.sendPacketToServer(packet);
                }, 20, 20, BACK_BUTTON_TEXTURE));

        PortalGunStyle style = getStyle();
        this.upgrades.setTextColour(style.textColor());
        if (security != null) this.security.setTextColour(style.textColor());
        if (customization != null) this.customization.setTextColour(style.textColor());
        this.backButton.setColor(style.highlightColor());
        this.backButton.setRenderBackground(false);
        GuiHelper.setTooltip(backButton, Component.translatable("ricksportalgun.button.back"));
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        PortalGunStyle style = getStyle();

        graphics.fill(this.width / 2 - 154, this.height / 2 - 110, this.width / 2 + 165, this.height / 2 + 100, style.bgColor());
        graphics.drawCenteredString(this.font, Component.translatable("menu.ricksportalgun.settings"), this.width / 2, this.height / 2 - 92, style.textColor());

        GuiHelper.renderOutline(graphics, security, style.highlightColor());
        GuiHelper.renderOutline(graphics, customization, style.highlightColor());
        GuiHelper.renderOutline(graphics, upgrades, style.highlightColor());
        GuiHelper.renderOutline(graphics, backButton, style.highlightColor());

        RenderSystem.enableBlend();
        graphics.blit(BG_LOCATION, this.width / 2 - 158, this.height / 2 - 115, 0, 0, 330, 224, 330, 224);
        RenderSystem.disableBlend();

        Style guiStyle = GuiHelper.getStyle(pMouseX, pMouseY);
        if (guiStyle != null && guiStyle.getHoverEvent() != null) {
            this.renderWithTooltip(graphics, pMouseX, pMouseY, pPartialTick);
        }
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
    }
}