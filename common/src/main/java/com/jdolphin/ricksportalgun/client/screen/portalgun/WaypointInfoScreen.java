package com.jdolphin.ricksportalgun.client.screen.portalgun;

import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.packet.SBManageWaypointsPacket;
import com.jdolphin.ricksportalgun.common.packet.SBSetDestinationPacket;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class WaypointInfoScreen extends AbstractBaseScreen {
    private Waypoint wp;

    protected WaypointInfoScreen(Waypoint waypoint) {
        super(Component.translatable("menu.ricksportalgun.waypoints.info"));
        this.wp = waypoint;
    }

    @Override
    public void render(GuiGraphics pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        GuiHelper.drawWhiteCenteredString(pPoseStack, Component.translatable("menu.ricksportalgun.waypoints.info"), this.width / 2, 30);
        GuiHelper.drawWhiteString(pPoseStack, Component.translatable("menu.ricksportalgun.waypoints.info.name", wp.getName()), this.width / 2 - 128, this.height / 2 - 48);
        GuiHelper.drawWhiteString(pPoseStack, Component.translatable("menu.ricksportalgun.waypoints.info.x",  wp.getX()), this.width / 2 - 128, this.height / 2 - 32);
        GuiHelper.drawWhiteString(pPoseStack, Component.translatable("menu.ricksportalgun.waypoints.info.y",  wp.getY()), this.width / 2 - 128, this.height / 2 - 16);
        GuiHelper.drawWhiteString(pPoseStack, Component.translatable("menu.ricksportalgun.waypoints.info.z",  wp.getZ()), this.width / 2 - 128, this.height / 2);
        GuiHelper.drawWhiteString(pPoseStack, Component.translatable("menu.ricksportalgun.waypoints.info.dimension",  wp.getDim()), this.width / 2 - 128, this.height / 2 + 16);
        Style style = GuiHelper.getStyle(pMouseX, pMouseY);
        if (style != null && style.getHoverEvent() != null) {
            this.renderWithTooltip(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    protected void init() {
        this.addRenderableWidget(Button.builder(Component.translatable("ricksportalgun.button.waypoint.select"), (button) -> {
            Player player = minecraft.player;
            ItemStack itemStack = player.getMainHandItem();
            if (itemStack.is(PGTags.Items.PORTAL_GUNS)) {
                SBSetDestinationPacket packet = new SBSetDestinationPacket(wp.getBlockPos(), wp.getDim());
                PGHelper.sendPacketToServer(packet);
                this.onClose();
            }
        }).pos(this.width / 2 - 136, this.height / 2 + 32).size(128, 20).build());
        this.addRenderableWidget(Button.builder(Component.translatable("ricksportalgun.button.waypoint.delete"), (button) -> {
            Player player = minecraft.player;
            ItemStack itemStack = player.getMainHandItem();
            if (itemStack.is(PGTags.Items.PORTAL_GUNS)) {
                SBManageWaypointsPacket packet = new SBManageWaypointsPacket(wp.getWaypointString(), true);
                PGHelper.sendPacketToServer(packet);
                minecraft.setScreen(new WaypointScreen());
                player.displayClientMessage(Component.translatable("notice.ricksportalgun.waypoint.deleted", wp.getName()).withStyle(ChatFormatting.GREEN), false);
            }
        }).size(128, 20).pos(this.width / 2 + 8, this.height / 2 + 32).build());
        super.init();
    }
}