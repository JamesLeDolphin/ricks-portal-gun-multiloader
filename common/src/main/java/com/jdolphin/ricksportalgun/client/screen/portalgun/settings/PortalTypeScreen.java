package com.jdolphin.ricksportalgun.client.screen.portalgun.settings;

import com.jdolphin.ricksportalgun.client.init.PGPortalTypeRenderers;
import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.PGCycleButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGImageButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGTextButton;
import com.jdolphin.ricksportalgun.common.customization.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.customization.PortalType;
import com.jdolphin.ricksportalgun.common.init.PGPortalTypes;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBSetPortalTypePacket;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;

public class PortalTypeScreen extends AbstractBaseScreen {
    private PGCycleButton<PortalType> typeButton;
    private PGCycleButton<PortalType.PortalShape> shapeButton;
    private PGTextButton select, cancel;
    private PortalType currentType;
    private PortalType.PortalShape shape;
    private PGImageButton backButton;

    public PortalTypeScreen() {
        super(Component.literal("Placeholder"));
    }

    @Override
    protected void init() {
        super.init();

        ItemStack stack = getItemStack();
        PortalType defaultType = PortalGunItem.getPortalType(stack);
        PortalType.PortalShape defShape = PortalGunItem.getPortalShape(stack);
        this.typeButton = this.addWidget(PGCycleButton.builder(PortalType::getName).withInitialValue(defaultType)
                .withValues(PGPortalTypes.TYPES.values()).create(this.width / 2 - 64, this.height / 2 - 64, 128, 20, Component.translatable("ricksportalgun.button.portal_type"),
                        (button, type) -> {}));

        this.shapeButton = this.addWidget(PGCycleButton.builder(PortalType.PortalShape::getTranslated).withInitialValue(defShape)
                .withValues(PortalType.PortalShape.values()).create(this.width / 2 - 64, this.height / 2 - 34, 128, 20, Component.translatable("ricksportalgun.button.portal_type"),
                        (button, type) -> {}));

        this.backButton = this.addRenderableWidget(new PGImageButton(this.width / 2 - 140, this.height / 2 - 96, 20, 20, Component.translatable("ricksportalgun.button.back"),
                (button) -> minecraft.setScreen(new CustomizationSettingsScreen()), 20, 20, BACK_BUTTON_TEXTURE));

        this.select = this.addRenderableWidget(new PGTextButton(this.width / 2 - 136, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.select"), (button) -> {
            if (getItemStack().is(PGTags.Items.PORTAL_GUNS)) {
                SBSetPortalTypePacket packet = new SBSetPortalTypePacket(typeButton.getValue(), shapeButton.getValue());
                PGHelper.sendPacketToServer(packet);
                this.onClose();
            }
        }, this.font));

        this.cancel = this.addRenderableWidget(new PGTextButton(this.width / 2 + 8, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.cancel"), (button) -> this.onClose(), this.font));

        PortalGunStyle style = getStyle();
        this.select.setTextColour(style.textColor());
        this.cancel.setTextColour(style.textColor());
        this.backButton.setColor(style.highlightColor());
        this.backButton.setRenderBackground(false);
        GuiHelper.setTooltip(backButton, Component.translatable("ricksportalgun.button.back"));
    }


    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        PortalGunStyle style = getStyle();
        graphics.fill(this.width / 2 - 154, this.height / 2 - 110, this.width / 2 + 165, this.height / 2 + 100, style.bgColor());

        GuiHelper.renderWidgets(graphics, mouseX, mouseY, partialTick, typeButton);
        GuiHelper.renderOutline(graphics, typeButton, style.highlightColor());
        GuiHelper.renderWidgets(graphics, mouseX, mouseY, partialTick, shapeButton);
        GuiHelper.renderOutline(graphics, shapeButton, style.highlightColor());
        GuiHelper.renderOutline(graphics, backButton, style.highlightColor());
        GuiHelper.renderOutline(graphics, select, style.highlightColor());
        GuiHelper.renderOutline(graphics, cancel, style.highlightColor());

        ItemStack  stack = getItemStack();
        if (typeButton.getValue() != null) {
            int color = PortalGunItem.getColor(stack);
            PGPortalTypeRenderers.getRenderer(typeButton.getValue()).renderInGui(this.width / 2, this.height / 2, 64, 82,
                    stack, graphics, mouseX, mouseY, partialTick, FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color));
        }

        Style guiStyle = GuiHelper.getStyle(mouseX, mouseY);
        if (guiStyle != null && guiStyle.getHoverEvent() != null) {
            this.renderWithTooltip(graphics, mouseX, mouseY, partialTick);
        }
        RenderSystem.enableBlend();
        graphics.blit(BG_LOCATION, this.width / 2 - 158, this.height / 2 - 115, 0, 0, 330, 224, 330, 224);
        RenderSystem.disableBlend();
        super.render(graphics, mouseX, mouseY, partialTick);
    }
}
