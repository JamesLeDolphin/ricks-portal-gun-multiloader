package com.jdolphin.ricksportalgun.client.screen.portalgun.settings;

import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.portalgun.SettingsScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.PGImageButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGSlider;
import com.jdolphin.ricksportalgun.client.screen.widget.PGTextButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGTooltipText;
import com.jdolphin.ricksportalgun.common.customization.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBCustomizeSettingsPacket;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CustomizationSettingsScreen extends AbstractBaseScreen {
    public static ResourceLocation RESET_LOCATION = PGHelper.id("textures/gui/sprites/icon/reset.png");
    private PGSlider portalSize, portalAge;
    private PGTextButton portalColor, menuTheme, select, cancel, portalType;
    private PGImageButton resetSize, resetAge, backButton;

    public CustomizationSettingsScreen() {
        super("menu.ricksportalgun.settings.customization");
    }

    public void init() {
        super.init();
        assert minecraft != null && minecraft.player != null;

        ItemStack stack = getItemStack();
        CompoundTag tag = stack.getOrCreateTag();

        float size = tag.contains(PGNbtKeys.TAG_SIZE) ? tag.getFloat(PGNbtKeys.TAG_SIZE) : 1.0f;
        int age = tag.contains(PGNbtKeys.TAG_AGE) ? tag.getInt(PGNbtKeys.TAG_AGE) : 10;

        this.portalSize = this.addWidget(new PGSlider(this.width / 2 + 62, this.height / 2 - 64, 42, 18,
                Component.empty(), size, 1.0f, 3.0f, true));

        this.resetSize = this.addRenderableWidget(new PGImageButton(portalSize.getX() + portalSize.getWidth() + 10, portalSize.getY() - 1, 21, 21,
                Component.translatable("ricksportalgun.button.portal_size.reset"), button -> this.portalSize.setValue(1), 16, 16, RESET_LOCATION));

        this.portalAge = this.addWidget(new PGSlider(this.width / 2 + 62, this.height / 2 - 40, 42, 18,
                Component.empty(), age, 5, 45, true));

        this.resetAge = this.addRenderableWidget(new PGImageButton(portalAge.getX() + portalAge.getWidth() + 10, portalAge.getY() - 1, 21, 21,
                Component.translatable("ricksportalgun.button.portal_age.reset"), button -> this.portalAge.setValue(10), 16, 16, RESET_LOCATION));

        this.portalColor = this.addRenderableWidget(new PGTextButton(this.width / 2 - 128, this.height / 2 - 16, 256, 18,
                Component.translatable("ricksportalgun.button.settings.customization.color"), button -> this.minecraft.setScreen(new ColourPickingScreen()), this.font));

        this.menuTheme = this.addRenderableWidget(new PGTextButton(this.width / 2 - 128, this.height / 2 + 8, 256, 18,
                Component.translatable("ricksportalgun.button.settings.customization.theme"), button -> minecraft.setScreen(new ThemeEditScreen()), this.font));

        this.portalType = this.addRenderableWidget(new PGTextButton(this.width / 2 - 128, this.height / 2 + 32, 256, 18,
                Component.literal("Placeholder"), button -> minecraft.setScreen(new PortalTypeScreen()), this.font));

        this.select = this.addRenderableWidget(new PGTextButton(this.width / 2 - 136, this.height / 2 + 64,128, 20,
                Component.translatable("ricksportalgun.button.select"), (button) -> {
                    SBCustomizeSettingsPacket packet = new SBCustomizeSettingsPacket(this.portalSize.getValue(), this.portalAge.getValueInt());
                    PGHelper.sendPacketToServer(packet);
                    this.onClose();

                }, this.font));
        this.cancel = this.addRenderableWidget(new PGTextButton( this.width / 2 + 8, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.cancel"), (button) -> this.onClose(), this.font));

        this.backButton = this.addRenderableWidget(new PGImageButton(this.width / 2 - 140, this.height / 2 - 96, 20, 20, Component.translatable("ricksportalgun.button.back"),
                (button) -> minecraft.setScreen(new SettingsScreen()), 20, 20, BACK_BUTTON_TEXTURE));

        PortalGunStyle style = getStyle();
        this.addRenderableWidget(new PGTooltipText(this.width / 2 - 128, this.portalAge.getY() + 4,
                Component.translatable("ricksportalgun.button.portal_age"), this.font, style.textColor(), Component.translatable("tooltip.ricksportalgun.button.portal_lifetime")));
        this.resetAge.setRenderBackground(false);
        this.resetSize.setRenderBackground(false);
        this.resetAge.setColor(style.highlightColor());
        this.resetSize.setColor(style.highlightColor());
        this.select.setTextColour(style.textColor());
        this.cancel.setTextColour(style.textColor());
        this.portalColor.setTextColour(style.textColor());
        this.menuTheme.setTextColour(style.textColor());
        this.portalType.setTextColour(style.textColor());
        this.portalSize.setRenderBG(false);
        this.portalAge.setRenderBG(false);
        this.portalSize.setStyle(style);
        this.portalAge.setStyle(style);
        this.backButton.setColor(style.highlightColor());
        this.backButton.setRenderBackground(false);
        GuiHelper.setTooltip(backButton, Component.translatable("ricksportalgun.button.back"));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        PortalGunStyle style = getStyle();

        graphics.fill(this.width / 2 - 154, this.height / 2 - 110, this.width / 2 + 165, this.height / 2 + 100, style.bgColor());

        graphics.drawCenteredString(this.font, Component.translatable("menu.ricksportalgun.settings.customization"), this.width / 2, this.height / 2 - 92, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.button.portal_size"), this.width / 2 - 128, this.portalSize.getY() + 4, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.button.portal_age"), this.width / 2 - 128, this.portalAge.getY() + 4, style.textColor());

        GuiHelper.renderOutline(graphics, this.portalColor, style.highlightColor());
        GuiHelper.renderOutline(graphics, this.menuTheme, style.highlightColor());
        GuiHelper.renderOutline(graphics, this.portalType, style.highlightColor());
        GuiHelper.renderOutline(graphics, this.resetSize, style.highlightColor());
        GuiHelper.renderOutline(graphics, this.resetAge, style.highlightColor());
        GuiHelper.renderOutline(graphics, this.select, style.highlightColor());
        GuiHelper.renderOutline(graphics, this.cancel, style.highlightColor());
        GuiHelper.renderOutline(graphics, this.backButton, style.highlightColor());

        GuiHelper.renderWidgets(graphics, mouseX, mouseY, delta, portalSize, portalAge);

        GuiHelper.renderTooltip(graphics, Component.translatable("ricksportalgun.button.portal_size.reset"), resetSize);
        GuiHelper.renderTooltip(graphics, Component.translatable("ricksportalgun.button.portal_age.reset"), resetAge);

        Style guiStyle = GuiHelper.getStyle(mouseX, mouseY);
        if (guiStyle != null && guiStyle.getHoverEvent() != null) {
            this.renderWithTooltip(graphics, mouseX, mouseY, delta);
        }
        RenderSystem.enableBlend();
        graphics.blit(BG_LOCATION, this.width / 2 - 158, this.height / 2 - 115, 0, 0, 330, 224, 330, 224);
        RenderSystem.disableBlend();
        super.render(graphics, mouseX, mouseY, delta);
    }
}
