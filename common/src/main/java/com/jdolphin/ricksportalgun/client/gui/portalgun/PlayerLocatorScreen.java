package com.jdolphin.ricksportalgun.client.gui.portalgun;

import com.jdolphin.ricksportalgun.client.gui.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.common.packet.SBLocatePlayerPacket;
import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class PlayerLocatorScreen extends AbstractBaseScreen {
    private EditBox playerInput;

    protected PlayerLocatorScreen() {
        super(Component.translatable("menu.ricksportalgun.player_locator"));
    }

    @Override
    protected void init() {
        this.playerInput = new EditBox(this.font,
                this.width / 2 - 64, this.height / 2 - 64, 128, 24,
                Component.translatable("chat.editBox"));
        this.addRenderableWidget(Button.builder(Component.translatable("ricksportalgun.button.player_locator.select"), (button) -> {
            this.setCoords();
            this.onClose();

        }).pos(this.width / 2 - 136, this.height / 2 + 32).size(128, 20).build());
        this.addRenderableWidget(Button.builder(Component.translatable("ricksportalgun.button.cancel"), (button) -> {
            this.onClose();
        }).size(128, 20).pos(this.width / 2 + 8, this.height / 2 + 32).build());

        this.playerInput.setMaxLength(256);
        this.playerInput.setBordered(true);

        this.addWidget(this.playerInput);
    }


    @Override
    public void render(GuiGraphics pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        pPoseStack.drawCenteredString(this.font, Component.translatable("menu.ricksportalgun.player_locator"),
                this.width / 2, 30, Color.WHITE.getRGB());
        this.playerInput.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        Style style = this.minecraft.gui.getChat().getClickedComponentStyleAt(pMouseX, pMouseY);
        if (style != null && style.getHoverEvent() != null) {
            this.renderWithTooltip(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    public void setCoords() {
        SBLocatePlayerPacket packet = new SBLocatePlayerPacket(playerInput.getValue());
        Helper.sendPacketToServer(packet);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        switch (pKeyCode) {
            case GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER:
                if(this.getFocused() instanceof Button)
                    return super.keyPressed(pKeyCode, pScanCode, pModifiers);
                this.setCoords();
                break;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
}
