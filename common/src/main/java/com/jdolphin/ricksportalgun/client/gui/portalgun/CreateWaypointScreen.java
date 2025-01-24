package com.jdolphin.ricksportalgun.client.gui.portalgun;

import com.jdolphin.ricksportalgun.client.gui.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.platform.Services;
import com.jdolphin.ricksportalgun.common.util.PGPacketType;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helpers.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helpers.LevelHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

public class CreateWaypointScreen extends AbstractBaseScreen {
    private EditBox waypointName;

    public CreateWaypointScreen() {
        super(Component.translatable("menu.ricksportalgun.waypoints.new"));
    }

    @Override
    protected void init() {
        super.init();
        this.waypointName = new EditBox(this.font,
                this.width / 2 - 64, this.height / 2 - 58, 112, 12,
                Component.translatable("chat.editBox"));
        this.addRenderableWidget(Button.builder(Component.translatable("ricksportalgun.button.waypoint.new"), button -> createWaypoint())
                .pos(this.width / 2 - 136, this.height / 2 + 32).size(128, 20).build());

        this.addRenderableWidget(Button.builder(Component.translatable("ricksportalgun.button.cancel"), (button) -> {
            this.minecraft.setScreen(new WaypointScreen());
        }).pos(this.width / 2 + 8, this.height / 2 + 32).size(128, 20).build());
        this.waypointName.setMaxLength(128);
        this.waypointName.setBordered(true);
        this.addWidget(this.waypointName);
    }

    @Override
    public void render(GuiGraphics pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        Player player = minecraft.player;
        if (player != null) {
            GuiHelper.drawWhiteString(pPoseStack, Component.translatable("menu.ricksportalgun.waypoints.new.name"), this.width / 2 - 96, this.height / 2 - 56);
            GuiHelper.drawWhiteString(pPoseStack, Component.translatable("menu.ricksportalgun.waypoints.new.x", player.getBlockX()), this.width / 2 - 96, this.height / 2 - 36);
            GuiHelper.drawWhiteString(pPoseStack, Component.translatable("menu.ricksportalgun.waypoints.new.y", player.getBlockY()), this.width / 2 - 96, this.height / 2 - 16);
            GuiHelper.drawWhiteString(pPoseStack, Component.translatable("menu.ricksportalgun.waypoints.new.z", player.getBlockZ()), this.width / 2 - 96, this.height / 2);
            GuiHelper.drawWhiteString(pPoseStack, Component.translatable("menu.ricksportalgun.waypoints.new.dimension", player.level().dimension().location().toString()), this.width / 2 - 96,
                    this.height / 2 + 16);
        }
        this.waypointName.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        Style style = this.minecraft.gui.getChat().getClickedComponentStyleAt(pMouseX, pMouseY);
        if (style != null && style.getHoverEvent() != null) {
            this.renderWithTooltip(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    public void createWaypoint() {
        assert minecraft != null && minecraft.player != null;
        LocalPlayer player = minecraft.player;
        ItemStack itemStack = player.getMainHandItem();
        if (itemStack.is(PGTags.Items.PORTAL_GUNS)) {
            BlockPos pos = player.blockPosition();
            if (waypointName.getValue().isEmpty()) return;
            Waypoint waypoint = new Waypoint(pos, LevelHelper.getPlayerDimensionLocation(player).toString(), waypointName.getValue());
            Services.PLATFORM.sendPacketToServer(new PGPacketType(PGPacketType.PacketType.MANAGE_WAYPOINTS, waypoint.getWaypointString(), false));
            this.minecraft.setScreen(new WaypointScreen());
        }
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
