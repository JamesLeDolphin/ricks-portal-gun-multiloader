package com.jdolphin.ricksportalgun.client.screen.portalgun.settings;

import com.jdolphin.ricksportalgun.client.init.PGPortalShapeRenderers;
import com.jdolphin.ricksportalgun.client.init.PGPortalTypeRenderers;
import com.jdolphin.ricksportalgun.client.render.portal.type.AbstractPortalTypeRenderer;
import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.PGCycleButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGImageButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGSlider;
import com.jdolphin.ricksportalgun.client.screen.widget.PGTextButton;
import com.jdolphin.ricksportalgun.common.customization.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.customization.shape.PortalShape;
import com.jdolphin.ricksportalgun.common.customization.type.PortalType;
import com.jdolphin.ricksportalgun.common.init.PGPortalShapes;
import com.jdolphin.ricksportalgun.common.init.PGPortalTypes;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBSetPortalTypePacket;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class PortalEditScreen extends AbstractBaseScreen {
    private PGSlider r, g, b;
    private PGCycleButton<PortalType> typeButton;
    private PGCycleButton<PortalShape> shapeButton;
    private PGTextButton select, cancel;
    private PGImageButton backButton;

    public PortalEditScreen() {
        super(Component.translatable("ricksportalgun.button.settings.customization.portal"));
    }

    @Override
    protected void init() {
        super.init();

        ItemStack stack = getItemStack();
        PortalType defaultType = PortalGunItem.getPortalType(stack);
        PortalShape defShape = PortalGunItem.getPortalShape(stack);
        int color = PortalGunItem.getColor(getItemStack());

        this.typeButton = this.addWidget(PGCycleButton.builder(PortalType::getName).withInitialValue(defaultType)
                .withValues(PGPortalTypes.TYPES.values()).create(this.width / 2 - 136, this.height / 2 - 65, 128, 20, Component.translatable("ricksportalgun.button.portal_type"),
                        (button, type) -> {}));

        this.shapeButton = this.addWidget(PGCycleButton.builder(PortalShape::getTranslationName).withInitialValue(defShape)
                .withValues(PGPortalShapes.SHAPES.values()).create(this.width / 2 - 136, this.height / 2 - 41, 128, 20, Component.translatable("ricksportalgun.button.portal_type"),
                        (button, type) -> {}));


        this.r = this.addRenderableWidget(new PGSlider(this.width / 2 - 72, this.height / 2 - 17, 64, 20,
                Component.empty(), new Color(color).getRed(), 0, 255, true));

        this.g = this.addRenderableWidget(new PGSlider(this.width / 2 - 72, this.height / 2 + 7, 64, 20,
                Component.empty(), new Color(color).getGreen(), 0, 255, true));

        this.b = this.addRenderableWidget(new PGSlider(this.width / 2 - 72, this.height / 2 + 31, 64, 20,
                Component.empty(), new Color(color).getBlue(), 0, 255, true));



        this.backButton = this.addRenderableWidget(new PGImageButton(this.width / 2 - 140, this.height / 2 - 96, 20, 20, Component.translatable("ricksportalgun.button.back"),
                (button) -> minecraft.setScreen(new CustomizationSettingsScreen()), 20, 20, BACK_BUTTON_TEXTURE));

        this.select = this.addRenderableWidget(new PGTextButton(this.width / 2 - 136, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.select"), (button) -> {
            if (getItemStack().is(PGTags.Items.PORTAL_GUNS)) {
                SBSetPortalTypePacket packet = new SBSetPortalTypePacket(typeButton.getValue(), shapeButton.getValue(), getColor());
                PGHelper.sendPacketToServer(packet);
                this.onClose();
            }
        }, this.font));

        this.cancel = this.addRenderableWidget(new PGTextButton(this.width / 2 + 8, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.cancel"), (button) -> this.onClose(), this.font));

        PortalGunStyle style = getStyle();
        this.r.setStyle(style);
        this.g.setStyle(style);
        this.b.setStyle(style);
        typeButton.setRenderBackground(false);
        shapeButton.setRenderBackground(false);
        this.r.setRenderBG(false);
        this.g.setRenderBG(false);
        this.b.setRenderBG(false);
        this.select.setTextColour(style.textColor());
        this.cancel.setTextColour(style.textColor());
        this.backButton.setColor(style.highlightColor());
        this.backButton.setRenderBackground(false);
        GuiHelper.setTooltip(backButton, Component.translatable("ricksportalgun.button.back"));
    }

    @Override
    public void tick() {
        super.tick();

        if (typeButton != null && typeButton.getValue() != null) {
            PortalType type = typeButton.getValue();
            boolean bl = type.supportsShape();
            shapeButton.active = bl;
            if (!bl) shapeButton.updateValue(type.defaultShape());
        }
    }

    public int getColor() {
        try {
            return new Color(((int) this.r.getValue()), ((int) this.g.getValue()), ((int) this.b.getValue())).getRGB();
        } catch (NumberFormatException e) {
            GuiHelper.drawWhiteCenteredString(new GuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource()),
                    Component.translatable("error.ricksportalgun.color", e.getMessage().toLowerCase()),
                    this.width / 2, 55);
        }
        return 0x000000;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        PortalGunStyle style = getStyle();
        graphics.fill(this.width / 2 - 154, this.height / 2 - 110, this.width / 2 + 165, this.height / 2 + 100, style.bgColor());

        graphics.drawCenteredString(this.font, Component.translatable("ricksportalgun.button.settings.customization.portal"), this.width / 2, this.height / 2 - 92, style.textColor());

        graphics.drawString(this.font, Component.translatable("ricksportalgun.red", ""), this.width / 2 - 136, this.r.getY() + 4, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.green", ""), this.width / 2 - 136, this.g.getY() + 4, getStyle().textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.blue", ""), this.width / 2 - 136, this.b.getY() + 4, getStyle().textColor());

        GuiHelper.renderWidgets(graphics, mouseX, mouseY, partialTick, typeButton);
        GuiHelper.renderOutline(graphics, typeButton, style.highlightColor());
        GuiHelper.renderWidgets(graphics, mouseX, mouseY, partialTick, shapeButton);
        GuiHelper.renderOutline(graphics, shapeButton, style.highlightColor());
        GuiHelper.renderOutline(graphics, backButton, style.highlightColor());
        GuiHelper.renderOutline(graphics, select, style.highlightColor());
        GuiHelper.renderOutline(graphics, cancel, style.highlightColor());

        RenderSystem.enableBlend();
        graphics.blit(BG_LOCATION, this.width / 2 - 158, this.height / 2 - 115, 0, 0, 330, 224, 330, 224);
        RenderSystem.disableBlend();

        graphics.fill(this.width / 2 + 32, this.height / 2 - 76, this.width / 2 + 104, this.height / 2 + 60, Color.BLACK.getRGB());
        graphics.renderOutline(this.width / 2 + 32, this.height / 2 - 76, 72, 136, style.highlightColor());

        ItemStack  stack = getItemStack();
        if (typeButton.getValue() != null) {
            int color = getColor();
            PortalShape shape = shapeButton.getValue();
            AbstractPortalTypeRenderer typeRenderer = PGPortalTypeRenderers.getRenderer(typeButton.getValue());
            if (typeRenderer != null) {
                typeRenderer.renderInGui(this.width / 2 + 36, this.height / 2 - 72, 64, 128, PGPortalShapeRenderers.getRenderer(shape),
                        stack, graphics, mouseX, mouseY, partialTick, FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color));
            } else {
                graphics.drawString(this.font, Component.literal("No portal type renderer registered!"), this.width / 2 + 36, this.height / 2 - 72, style.textColor());
            }
        }

        Style guiStyle = GuiHelper.getStyle(mouseX, mouseY);
        if (guiStyle != null && guiStyle.getHoverEvent() != null) {
            this.renderWithTooltip(graphics, mouseX, mouseY, partialTick);
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }
}
