package com.jdolphin.ricksportalgun.client.screen.portalgun;

import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.PGImageButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGTextButton;
import com.jdolphin.ricksportalgun.common.customization.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBManageWaypointsPacket;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.lwjgl.glfw.GLFW;

public class CreateWaypointScreen extends AbstractBaseScreen {
    private EditBox waypointName;
    private PGTextButton select, cancel;
    private PGImageButton backButton;

    public CreateWaypointScreen() {
        super("menu.ricksportalgun.waypoints.new");
    }

    @Override
    protected void init() {
        super.init();
        assert  minecraft != null;

        this.waypointName = this.addWidget(new EditBox(this.font,
                this.width / 2 - 64, this.height / 2 - 60, 112, 16,
                Component.translatable("chat.editBox")));

        this.select = this.addRenderableWidget(new PGTextButton(this.width / 2 - 136, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.waypoint.new"), button -> createWaypoint(), this.font));

        this.cancel = this.addRenderableWidget(new PGTextButton(this.width / 2 + 8, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.cancel"), (button) -> this.onClose(), this.font));

        this.backButton = this.addRenderableWidget(new PGImageButton(this.width / 2 - 140, this.height / 2 - 96, 20, 20, Component.translatable("ricksportalgun.button.waypoint.new"),
                (button) -> minecraft.setScreen(new WaypointScreen()), 20, 20, BACK_BUTTON_TEXTURE));

        PortalGunStyle style = getStyle();
        this.backButton.setColor(style.highlightColor());
        this.backButton.setRenderBackground(false);
        this.waypointName.setMaxLength(128);
        this.waypointName.setBordered(true);
        GuiHelper.setTooltip(backButton, Component.translatable("ricksportalgun.button.back"));
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        PortalGunStyle style = getStyle();
        assert minecraft != null && minecraft.player != null;
        LocalPlayer player = minecraft.player;

        graphics.fill(this.width / 2 - 154, this.height / 2 - 110, this.width / 2 + 165, this.height / 2 + 100, style.bgColor());

        graphics.drawCenteredString(this.font, Component.translatable("menu.ricksportalgun.waypoints.new"), this.width / 2, this.height / 2 - 92, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.name", ""), this.width / 2 - 96, this.height / 2 - 56, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.x", player.getBlockX()), this.width / 2 - 96, this.height / 2 - 36, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.y", player.getBlockY()), this.width / 2 - 96, this.height / 2 - 16, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.z", player.getBlockZ()), this.width / 2 - 96, this.height / 2, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.dimension", LevelHelper.getPlayerDimensionLocation(player).toString()), this.width / 2 - 96,
                this.height / 2 + 16, style.textColor());

        GuiHelper.renderOutline(graphics, waypointName, style.highlightColor());
        GuiHelper.renderOutline(graphics, backButton, style.highlightColor());
        GuiHelper.renderOutline(graphics, select, style.highlightColor());
        GuiHelper.renderOutline(graphics, cancel, style.highlightColor());

        this.waypointName.render(graphics, pMouseX, pMouseY, pPartialTick);
        RenderSystem.enableBlend();
        graphics.blit(BG_LOCATION, this.width / 2 - 158, this.height / 2 - 115, 0, 0, 330, 224, 330, 224);
        RenderSystem.disableBlend();
        Style guiStyle = GuiHelper.getStyle(pMouseX, pMouseY);
        if (guiStyle != null && guiStyle.getHoverEvent() != null) {
            this.renderWithTooltip(graphics, pMouseX, pMouseY, pPartialTick);
        }

        super.render(graphics, pMouseX, pMouseY, pPartialTick);
    }

    public void createWaypoint() {
        assert minecraft != null && minecraft.player != null;
        LocalPlayer player = minecraft.player;

        BlockPos pos = player.blockPosition();
        if (waypointName.getValue().isEmpty()) return;
        Waypoint waypoint = new Waypoint(pos, player.getYRot(), LevelHelper.getPlayerDimensionLocation(player).toString(), waypointName.getValue());
        SBManageWaypointsPacket packet = new SBManageWaypointsPacket(waypoint.getWaypointString(), false);
        PGHelper.sendPacketToServer(packet);
        this.minecraft.setScreen(new WaypointScreen());
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == GLFW.GLFW_KEY_ENTER || pKeyCode == GLFW.GLFW_KEY_KP_ENTER) {
            createWaypoint();
            return true;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
}
