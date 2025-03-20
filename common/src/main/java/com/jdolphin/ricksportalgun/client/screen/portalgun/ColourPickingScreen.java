package com.jdolphin.ricksportalgun.client.screen.portalgun;

import com.jdolphin.ricksportalgun.client.entity.render.PortalEntityRenderer;
import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.Slider;
import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.packet.SBColourPacket;
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
    private Slider r, g, b;

    protected ColourPickingScreen() {
        super(Component.translatable("menu.ricksportalgun.colour_select"));
    }

    @Override
    protected void init() {
        LocalPlayer player = minecraft.player;
        ItemStack stack = player.getMainHandItem();
        Color color = new Color(stack.getOrDefault(PGDataComponents.PORTAL_COLOUR, Color.GREEN.getRGB()));
        this.r = this.addRenderableWidget(new Slider(this.width / 2 - 44, this.height / 2 - 60, 36, 20,
                Component.empty(), (double) color.getRed() / 255, 0, 1, false));

        this.g = this.addRenderableWidget(new Slider(this.width / 2 - 44, this.height / 2 - 36, 36, 20,
                Component.empty(), (double) color.getGreen() / 255, 0, 1, false));

        this.b = this.addRenderableWidget(new Slider(this.width / 2 - 44, this.height / 2 - 12, 36, 20,
                Component.empty(), (double) color.getBlue() / 255, 0, 1, false));

        this.addRenderableWidget(Button.builder(Component.translatable("ricksportalgun.button.select"), (button) -> {
            if (stack.is(PGTags.Items.PORTAL_GUNS)) {
                SBColourPacket packet = new SBColourPacket(getColor());
                PGHelper.sendPacketToServer(packet);
                this.onClose();
            }
        }).size(128, 20).pos(this.width / 2 - 136, this.height / 2 + 32).build());
        this.addRenderableWidget(Button.builder(
                Component.translatable("ricksportalgun.button.cancel"), (button) -> this.onClose()).size(128, 20).pos(this.width / 2 + 8, this.height / 2 + 32).build());

        this.addRenderableWidget(Button.builder(
                Component.translatable("ricksportalgun.button.colour.reset"), (button) -> {
                    Color colour = new Color(stack.getOrDefault(PGDataComponents.DEFAULT_COLOUR, Color.GREEN.getRGB()));
                    int rgb = colour.getRGB();
                    try {
                        this.r.setValue(ARGB.redFloat(rgb));
                        this.g.setValue(ARGB.greenFloat(rgb));
                        this.b.setValue(ARGB.blueFloat(rgb));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).pos(this.width / 2 + 72, this.height / 2 + 8).size(64, 20).build());
    }

    public int getColor() {
        try {
            return new Color((float) this.r.getValue(), (float)this.g.getValue(), (float)this.b.getValue()).getRGB();
        } catch (NumberFormatException e) {
            GuiHelper.drawWhiteCenteredString(new GuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource()),
                    Component.translatable("error.ricksportalgun.color", e.getMessage().toLowerCase()),
                    this.width / 2, 55);
        }
        return 0x000000;
    }

    @Override
    public void render(GuiGraphics pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        GuiHelper.drawWhiteString(pPoseStack, Component.translatable("menu.ricksportalgun.colour_select.red", ""), this.width / 2 - 80, this.height / 2 - 48);
        GuiHelper.drawWhiteString(pPoseStack, Component.translatable("menu.ricksportalgun.colour_select.green", ""), this.width / 2 - 80, this.height / 2 - 32);
        GuiHelper.drawWhiteString(pPoseStack, Component.translatable("menu.ricksportalgun.colour_select.blue", ""), this.width / 2 - 80, this.height / 2 - 14);

        GuiHelper.drawWhiteCenteredString(pPoseStack, Component.translatable("menu.ricksportalgun.colour_select"),
                this.width / 2, 30);

        GuiHelper.renderWidgets(pPoseStack, pMouseX, pMouseY, pPartialTick, r, g, b);

        int x = 64, y = 82;
        pPoseStack.blit(RenderType::guiTextured, PortalEntityRenderer.PORTAL_TEXTURE, this.width / 2 + 10, this.height / 2 - 64, x, y, x, y, x, y, this.getColor());

        Style style = GuiHelper.getStyle(pMouseX, pMouseY);
        if (style != null && style.getHoverEvent() != null) {
            this.renderWithTooltip(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
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