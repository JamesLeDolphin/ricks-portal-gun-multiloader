package com.jdolphin.ricksportalgun.client.screen.portalgun;

import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.PGImageButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGTextButton;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBManageWaypointsPacket;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBSetDestinationPacket;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class WaypointInfoScreen extends AbstractBaseScreen {
    private final Waypoint wp;
    private PGTextButton select, delete;
    private PGImageButton backButton;

    public WaypointInfoScreen(Waypoint waypoint) {
        super("menu.ricksportalgun.waypoints.info");
        this.wp = waypoint;
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        PortalGunStyle style = getStyle();
        fillBackgroundColor(graphics);

        graphics.drawCenteredString(this.font, Component.translatable("menu.ricksportalgun.waypoints.info"), this.width / 2, this.height / 2 - 92, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.name", wp.getName()), this.width / 2 - 64, this.height / 2 - 48, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.x",  wp.getX()), this.width / 2 - 64, this.height / 2 - 32, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.y",  wp.getY()), this.width / 2 - 64, this.height / 2 - 16, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.z",  wp.getZ()), this.width / 2 - 64, this.height / 2, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.dimension",  wp.getDimension()), this.width / 2 - 64, this.height / 2 + 16, style.textColor());

        GuiHelper.renderOutline(graphics, select, style.highlightColor());
        GuiHelper.renderOutline(graphics, delete, style.highlightColor());
        GuiHelper.renderOutline(graphics, backButton, style.highlightColor());

        drawOverlay(graphics);

        Style guiStyle = GuiHelper.getStyle(pMouseX, pMouseY);
        if (guiStyle != null && guiStyle.getHoverEvent() != null) {
            this.renderWithTooltip(graphics, pMouseX, pMouseY, pPartialTick);
        }
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    protected void init() {
        super.init();
        assert  minecraft != null && minecraft.player != null;

        this.select = this.addRenderableWidget(new PGTextButton(this.width / 2 - 136, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.select"), (button) -> {
            SBSetDestinationPacket packet = new SBSetDestinationPacket(wp.getBlockPos(), wp.getDimension());
            PGHelper.sendPacketToServer(packet);
            this.onClose();
        }, this.font));

        this.delete = this.addRenderableWidget(new PGTextButton(this.width / 2 + 8, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.delete"), (button) -> {
            SBManageWaypointsPacket packet = new SBManageWaypointsPacket(wp.getWaypointString(), true);
            PGHelper.sendPacketToServer(packet);
            minecraft.setScreen(new WaypointScreen());
        }, this.font));

        this.backButton = this.addRenderableWidget(new PGImageButton(this.width / 2 - 140, this.height / 2 - 96, 20, 20, Component.translatable("ricksportalgun.button.waypoint.new"),
                (button) -> minecraft.setScreen(new WaypointScreen()), 20, 20, BACK_BUTTON_TEXTURE));

        PortalGunStyle style = getStyle();
        this.backButton.setColor(style.highlightColor());
        this.backButton.setRenderBackground(false);
        GuiHelper.setTooltip(backButton, Component.translatable("ricksportalgun.button.back"));
    }
}