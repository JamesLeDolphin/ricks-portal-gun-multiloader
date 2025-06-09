package com.jdolphin.ricksportalgun.client.screen.portalgun;

import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.PGTextButton;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBLocatePlayerPacket;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.StringRepresentable;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.List;

public class LocatorScreen extends AbstractBaseScreen {
    private EditBox input;
    private PGTextButton select, cancel;
    private CycleButton<LocatorType> locatorType;
    private List<String> playerList, biomeList, structureList;

    public LocatorScreen(List<String> playerList, List<String> biomeList, List<String> structureList) {
        super("menu.ricksportalgun.player_locator");
        this.playerList = playerList;
        this.biomeList = biomeList;
        this.structureList = structureList;
    }

    @Override
    protected void init() {

        this.input = this.addWidget(new EditBox(this.font, this.width / 2 - 64, this.height / 2 - 64, 128, 24,
                Component.translatable("chat.editBox")));

        this.select = this.addRenderableWidget(new PGTextButton(this.width / 2 - 136, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.select"), (button) -> {
            this.setCoords();
            this.onClose();
        }, this.font));

        this.locatorType = this.addRenderableWidget(CycleButton.builder(LocatorType::getDisplayName)
                .withInitialValue(LocatorType.PLAYER).withValues(LocatorType.values()).create(this.width / 2, this.height / 2, 64, 20, Component.translatable("ricksportalgun.button.locator"),
                        (button, type) -> System.out.println(type.key)));

        this.cancel = this.addRenderableWidget(new PGTextButton(this.width / 2 + 8, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.cancel"), (button) -> this.onClose(), this.font));

        this.input.setMaxLength(256);
        this.input.setBordered(true);

    }


    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        PortalGunStyle style = getStyle();

        graphics.fill(this.width / 2 - 154, this.height / 2 - 110, this.width / 2 + 165, this.height / 2 + 100, style.bgColor());
        graphics.drawCenteredString(this.font, Component.translatable("menu.ricksportalgun.locator"), this.width / 2, 30, Color.WHITE.getRGB());
        this.input.render(graphics, pMouseX, pMouseY, pPartialTick);

        GuiHelper.renderOutline(graphics, select, style.highlightColor());
        GuiHelper.renderOutline(graphics, cancel, style.highlightColor());
        GuiHelper.renderOutline(graphics, locatorType, style.highlightColor());

        Style guiStyle = GuiHelper.getStyle(pMouseX, pMouseY);
        if (guiStyle != null && guiStyle.getHoverEvent() != null) {
            this.renderWithTooltip(graphics, pMouseX, pMouseY, pPartialTick);
        }
        graphics.blit(RenderType::guiTextured, BG_LOCATION, this.width / 2 - 158, this.height / 2 - 115, 0, 0, 330, 224, 330, 224);
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
    }

    public void setCoords() {
        SBLocatePlayerPacket packet = new SBLocatePlayerPacket(input.getValue());
        PGHelper.sendPacketToServer(packet);
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

    public enum LocatorType {
        BIOME("biome"),
        PLAYER("player"),
        STRUCTURE("structure")
        ;
        String key;
        LocatorType(String key) {
            this.key = key;
        }

        public Component getDisplayName() {
            return Component.translatable("ricksportalgun.button.locator." + this.key);
        }
    }
}
