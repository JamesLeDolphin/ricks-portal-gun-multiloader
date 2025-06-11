package com.jdolphin.ricksportalgun.client.screen.portalgun.settings;

import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.PGImageButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGSlider;
import com.jdolphin.ricksportalgun.client.screen.widget.PGTextButton;
import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CustomizationSettingsScreen extends AbstractBaseScreen {
    private static final ResourceLocation PAGE_FORWARD_SPRITE = ResourceLocation.withDefaultNamespace("widget/page_forward");
    private PGSlider portalSize, portalAge;
    private PGTextButton portalColor, menuTheme, select, cancel;
    private PGImageButton resetSize, resetAge;

    public CustomizationSettingsScreen() {
        super("menu.ricksportalgun.settings.customization");
    }

    public void init() {
        super.init();

        LocalPlayer player = minecraft.player;
        ItemStack stack = player.getMainHandItem();

        float size = stack.getOrDefault(PGDataComponents.PORTAL_SIZE, 1.0f);
        int age = stack.getOrDefault(PGDataComponents.PORTAL_LIFETIME, 10);

        this.portalSize = this.addWidget(new PGSlider(this.width / 2 + 62, this.height / 2 - 64, 42, 18,
                Component.empty(), size, 1.0f, 3.0f, true));

        this.resetSize = this.addRenderableWidget(new PGImageButton(portalSize.getX() + portalSize.getWidth() + 10, portalSize.getY() - 1, 21, 21,
                Component.translatable("ricksportalgun.button.portal_size.reset"), button -> this.portalSize.setValue(1), 20, 13, PAGE_FORWARD_SPRITE));

        this.portalAge = this.addWidget(new PGSlider(this.width / 2 + 62, this.height / 2 - 40, 42, 18,
                Component.empty(), age, 5, 45, true));

        this.resetAge = this.addRenderableWidget(new PGImageButton(portalAge.getX() + portalAge.getWidth() + 10, portalAge.getY() - 1, 21, 21,
                Component.translatable("ricksportalgun.button.portal_age.reset"), button -> this.portalAge.setValue(10), 20, 13, PAGE_FORWARD_SPRITE));

        this.portalColor = this.addRenderableWidget(new PGTextButton(this.width / 2 - 128, this.height / 2 - 16, 256, 18,
                Component.translatable("ricksportalgun.button.settings.customization.color"), button -> this.minecraft.setScreen(new ColourPickingScreen()), this.font));

        this.menuTheme = this.addRenderableWidget(new PGTextButton(this.width / 2 - 128, this.height / 2 + 8, 256, 18,
                Component.translatable("ricksportalgun.button.settings.customization.theme"), button -> minecraft.setScreen(new ThemeEditScreen()), this.font));

        this.select = this.addRenderableWidget(new PGTextButton(this.width / 2 - 136, this.height / 2 + 64,128, 20,
                Component.translatable("ricksportalgun.button.select"), (button) -> {
                    //  SBSettingsPacket packet = new SBSettingsPacket(this.portalSize.getValue(), this.portalAge.getValueInt());
                    //  PGHelper.sendPacketToServer(packet);
                    this.onClose();

                }, this.font));
        this.cancel = this.addRenderableWidget(new PGTextButton( this.width / 2 + 8, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.cancel"), (button) -> this.onClose(), this.font));

        PortalGunStyle style = getStyle();
        this.resetAge.setRenderBackground(false);
        this.resetSize.setRenderBackground(false);
        this.select.setTextColour(style.textColor());
        this.cancel.setTextColour(style.textColor());
        this.portalColor.setTextColour(style.textColor());
        this.menuTheme.setTextColour(style.textColor());
        this.portalSize.setRenderBG(false);
        this.portalSize.setStyle(style);
        this.portalAge.setStyle(style);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        PortalGunStyle style = getStyle();

        graphics.fill(this.width / 2 - 154, this.height / 2 - 110, this.width / 2 + 165, this.height / 2 + 100, style.bgColor());

        graphics.drawCenteredString(this.font, Component.translatable("menu.ricksportalgun.settings.customization"), this.width / 2, 30, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.button.portal_size"), this.width / 4 - 16, this.portalSize.getY() + 4, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.button.portal_age"), this.width / 4 - 16, this.portalAge.getY() + 4, style.textColor());

        GuiHelper.renderOutline(graphics, this.portalColor, style.highlightColor());
        GuiHelper.renderOutline(graphics, this.menuTheme, style.highlightColor());
        GuiHelper.renderOutline(graphics, this.resetSize, style.highlightColor());
        GuiHelper.renderOutline(graphics, this.resetAge, style.highlightColor());
        GuiHelper.renderOutline(graphics, this.select, style.highlightColor());
        GuiHelper.renderOutline(graphics, this.cancel, style.highlightColor());

        GuiHelper.renderWidgets(graphics, mouseX, mouseY, delta, portalSize, portalAge);

        GuiHelper.renderTooltip(graphics, Component.translatable("ricksportalgun.button.portal_size.reset"), resetSize);
        GuiHelper.renderTooltip(graphics, Component.translatable("ricksportalgun.button.portal_age.reset"), resetAge);

        Style guiStyle = GuiHelper.getStyle(mouseX, mouseY);
        if (guiStyle != null && guiStyle.getHoverEvent() != null) {
            this.renderWithTooltip(graphics, mouseX, mouseY, delta);
        }
        graphics.blit(RenderType::guiTextured, BG_LOCATION, this.width / 2 - 158, this.height / 2 - 115, 0, 0, 330, 224, 330, 224);
        super.render(graphics, mouseX, mouseY, delta);
    }
}
