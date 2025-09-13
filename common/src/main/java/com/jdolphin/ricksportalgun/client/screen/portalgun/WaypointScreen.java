package com.jdolphin.ricksportalgun.client.screen.portalgun;

import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.PGImageButton;
import com.jdolphin.ricksportalgun.client.screen.widget.WaypointListWidget;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBOpenCoordGuiPacket;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class WaypointScreen extends AbstractBaseScreen {

    public WaypointListWidget waypointList;
    public PGImageButton addWaypoint, backButton;

    public static ResourceLocation NEW_WAYPOINT_TEXTURES = PGHelper.id("textures/gui/sprites/icon/new_waypoint.png");

    public WaypointScreen() {
        super("menu.ricksportalgun.waypoints");
    }

    @Override
    protected void init() {
        super.init();
        assert minecraft != null && minecraft.screen != null && minecraft.player != null;

        this.addWaypoint = this.addRenderableWidget(new PGImageButton(this.width / 2 + 68, this.height / 2 - 94, 20, 20, Component.translatable("ricksportalgun.button.waypoint.new"),
                (button) -> this.minecraft.setScreen(new CreateWaypointScreen()), 20, 20, NEW_WAYPOINT_TEXTURES));

        ItemStack stack = getItemStack();
        this.waypointList = this.addWidget(new WaypointListWidget(170, 152, this.width / 2 - 75, this.height / 2 - 70,
                24, stack, true, 128, 20));

        this.backButton = this.addRenderableWidget(new PGImageButton(this.width / 2 - 140, this.height / 2 - 96, 20, 20, Component.translatable("ricksportalgun.button.back"),
                (button) -> {
                    SBOpenCoordGuiPacket packet = new SBOpenCoordGuiPacket();
                    PGHelper.sendPacketToServer(packet);
                }, 20, 20, BACK_BUTTON_TEXTURE));

        PortalGunStyle style = getStyle();
        this.addWaypoint.setColor(style.highlightColor());
        this.addWaypoint.setRenderBackground(false);
        this.backButton.setColor(style.highlightColor());
        this.backButton.setRenderBackground(false);
        this.waypointList.setRenderButtonBackground(false);
        this.waypointList.setStyle(style);
        GuiHelper.setTooltip(backButton, Component.translatable("ricksportalgun.button.back"));
    }

    @Override
    public void tick() {
        super.tick();
        assert minecraft != null && minecraft.player != null;

        ItemStack heldItem = getItemStack();
        if (heldItem.is(PGTags.Items.PORTAL_GUNS)) {
            if (this.waypointList != null) this.waypointList.refreshEntries(heldItem);
        } else this.onClose();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        PortalGunStyle style = getStyle();

        graphics.fill(this.width / 2 - 154, this.height / 2 - 110, this.width / 2 + 165, this.height / 2 + 100, style.bgColor());

        GuiHelper.renderTooltip(graphics, Component.translatable("ricksportalgun.button.waypoint.new"), addWaypoint);
        GuiHelper.renderOutline(graphics, addWaypoint, style.highlightColor());
        GuiHelper.renderOutline(graphics, waypointList, style.highlightColor());
        GuiHelper.renderOutline(graphics, backButton, style.highlightColor());

        if (waypointList != null) {
            this.waypointList.render(graphics, pMouseX, pMouseY, pPartialTick);
            GuiHelper.renderOutline(graphics, waypointList, Color.YELLOW.getRGB());
        }
        graphics.drawCenteredString(this.font, Component.translatable("ricksportalgun.button.waypoint.saved"), this.width / 2, this.height / 2 - 92, style.textColor());


        Style guiStyle = GuiHelper.getStyle(pMouseX, pMouseY);
        if (guiStyle != null && guiStyle.getHoverEvent() != null) {
            this.renderWithTooltip(graphics, pMouseX, pMouseY, pPartialTick);
        }
        RenderSystem.enableBlend();
        graphics.blit(BG_LOCATION, this.width / 2 - 158, this.height / 2 - 115, 0, 0, 330, 224, 330, 224);
        RenderSystem.disableBlend();
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
    }
}