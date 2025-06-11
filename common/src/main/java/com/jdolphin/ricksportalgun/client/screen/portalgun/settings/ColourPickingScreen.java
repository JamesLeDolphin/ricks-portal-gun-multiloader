package com.jdolphin.ricksportalgun.client.screen.portalgun.settings;

import com.jdolphin.ricksportalgun.client.entity.render.PortalEntityRenderer;
import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.PGSlider;
import com.jdolphin.ricksportalgun.client.screen.widget.PGTextButton;
import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBColourPacket;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class ColourPickingScreen extends AbstractBaseScreen {
    private PGSlider r, g, b, size;
    private PGTextButton reset, select, cancel;

    public ColourPickingScreen() {
        super(Component.translatable("menu.ricksportalgun.colour_select"));
    }

    @Override
    protected void init() {
        LocalPlayer player = minecraft.player;
        ItemStack stack = player.getMainHandItem();
        int color = stack.getOrDefault(PGDataComponents.PORTAL_COLOUR, Color.GREEN.getRGB());
        this.r = this.addRenderableWidget(new PGSlider(this.width / 2 - 44, this.height / 2 - 60, 36, 20,
                Component.empty(), ARGB.redFloat(color), 0, 1, false));

        this.g = this.addRenderableWidget(new PGSlider(this.width / 2 - 44, this.height / 2 - 36, 36, 20,
                Component.empty(), ARGB.greenFloat(color), 0, 1, false));

        this.b = this.addRenderableWidget(new PGSlider(this.width / 2 - 44, this.height / 2 - 12, 36, 20,
                Component.empty(), ARGB.blueFloat(color), 0, 1, false));

        this.size = this.addRenderableWidget(new PGSlider(this.width / 2 - 44, this.height / 2 + 12, 36, 20,
                Component.empty(), 1, 1, 3, false));

        this.select = this.addRenderableWidget(new PGTextButton(this.width / 2 - 136, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.select"), (button) -> {
            if (stack.is(PGTags.Items.PORTAL_GUNS)) {
                SBColourPacket packet = new SBColourPacket(getColor());
                PGHelper.sendPacketToServer(packet);
                this.onClose();
            }
        }, this.font));

        this.cancel = this.addRenderableWidget(new PGTextButton(this.width / 2 + 8, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.cancel"),
                (button) -> this.onClose(), this.font));

        this.reset = this.addRenderableWidget(new PGTextButton(this.width / 2 + 72, this.height / 2 + 42, 64, 20,
                Component.translatable("ricksportalgun.button.colour.reset"), (button) -> {
                    int rgb = stack.getOrDefault(PGDataComponents.DEFAULT_PORTAL_COLOUR, Color.GREEN.getRGB());

                    try {
                        this.r.setValue(ARGB.redFloat(rgb));
                        this.g.setValue(ARGB.greenFloat(rgb));
                        this.b.setValue(ARGB.blueFloat(rgb));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, this.font));

        PortalGunStyle style = getStyle();
        this.select.setTextColour(style.textColor());
        this.reset.setTextColour(style.textColor());
        this.cancel.setTextColour(style.textColor());
    }

    public int getColor() {
        try {
            return ARGB.colorFromFloat(1.0f, (float) this.r.getValue(), (float) this.g.getValue(), (float) this.b.getValue());
        } catch (NumberFormatException e) {
            GuiHelper.drawWhiteCenteredString(new GuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource()),
                    Component.translatable("error.ricksportalgun.color", e.getMessage().toLowerCase()),
                    this.width / 2, 55);
        }
        return 0x000000;
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        PortalGunStyle style = getStyle();

        graphics.fill(this.width / 2 - 154, this.height / 2 - 110, this.width / 2 + 165, this.height / 2 + 100, style.bgColor());

        graphics.drawCenteredString(this.font, Component.translatable("menu.ricksportalgun.colour_select"), this.width / 2, 30, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.red", ""), this.width / 2 - 110, this.r.getY() + 4, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.green", ""), this.width / 2 - 110, this.g.getY() + 4, getStyle().textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.blue", ""), this.width / 2 - 110, this.b.getY() + 4, getStyle().textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.button.settings.customization.color.size"), this.width / 2 - 110, this.size.getY() + 4, getStyle().textColor());

        GuiHelper.renderOutline(graphics, select, style.highlightColor());
        GuiHelper.renderOutline(graphics, cancel, style.highlightColor());
        GuiHelper.renderOutline(graphics, reset, style.highlightColor());

        GuiHelper.renderWidgets(graphics, pMouseX, pMouseY, pPartialTick, r, g, b);

        graphics.blit(RenderType::guiTextured, BG_LOCATION, this.width / 2 - 158, this.height / 2 - 115, 0, 0, 330, 224, 330, 224);

        int x = 64, y = 82, multiplier = size.getValueInt();
        graphics.blit(RenderType::guiTextured, PortalEntityRenderer.PORTAL_TEXTURE, this.width / 2 + 10,
                this.height / 2 - 64, 0, 0, x * multiplier, y, x * multiplier, y, this.getColor());

        Style guiStyle = GuiHelper.getStyle(pMouseX, pMouseY);
        if (guiStyle != null && guiStyle.getHoverEvent() != null) {
            this.renderWithTooltip(graphics, pMouseX, pMouseY, pPartialTick);
        }
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        switch (pKeyCode) {
            case GLFW.GLFW_KEY_ENTER:
            case GLFW.GLFW_KEY_KP_ENTER:
                if (this.getFocused() instanceof Button)
                    return super.keyPressed(pKeyCode, pScanCode, pModifiers);
                SBColourPacket packet = new SBColourPacket(getColor());
                PGHelper.sendPacketToServer(packet);
                break;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
}