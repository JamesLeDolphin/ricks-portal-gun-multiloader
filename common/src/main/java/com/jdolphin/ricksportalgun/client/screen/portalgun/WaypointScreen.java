package com.jdolphin.ricksportalgun.client.screen.portalgun;

import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.BetterImageButton;
import com.jdolphin.ricksportalgun.client.screen.widget.WaypointListWidget;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class WaypointScreen extends AbstractBaseScreen {

    public WaypointListWidget waypointList;
    public Button addWaypoint;

    public static ResourceLocation NEW_WAYPOINT_TEXTURES = PGHelper.createLocation("icon/new_waypoint");

    public WaypointScreen() {
        super("menu.ricksportalgun.waypoints");
    }

    @Override
    protected void init() {
        super.init();

        assert minecraft != null && minecraft.screen != null && minecraft.player != null;

        LocalPlayer player = minecraft.player;
        this.addWaypoint = this.addRenderableWidget(new BetterImageButton(this.width / 2 + 68, 26, 20, 20, Component.translatable("ricksportalgun.button.waypoint.new"),
                (button) -> this.minecraft.setScreen(new CreateWaypointScreen()), 20, 20, NEW_WAYPOINT_TEXTURES ));
        ItemStack stack = player.getMainHandItem();
        if (stack.is(PGTags.Items.PORTAL_GUNS))
            this.waypointList = this.addWidget(new WaypointListWidget(this, this.width, (this.height / 3) * 2, 0,this.height / 3 - 30,
                    21, stack, true, 128, 20));

    }

    @Override
    public void tick() {
        super.tick();

        ItemStack heldItem = minecraft.player.getMainHandItem();
        if (heldItem.is(PGTags.Items.PORTAL_GUNS)) {
            if (this.waypointList != null) this.waypointList.refreshEntries(heldItem);
        } else this.onClose();
    }

    @Override
    public void render(@NotNull GuiGraphics stack, int pMouseX, int pMouseY, float pPartialTick) {
        this.addWaypoint.setTooltip(Tooltip.create(Component.translatable("ricksportalgun.button.waypoint.new")));
        if (waypointList != null)
            this.waypointList.render(stack, pMouseX, pMouseY, pPartialTick);

        GuiHelper.drawWhiteCenteredString(stack, Component.translatable("ricksportalgun.button.waypoint.saved"), this.width / 2, 30);

        assert this.minecraft != null;
        Style style = GuiHelper.getStyle(pMouseX, pMouseY);
        if (style != null && style.getHoverEvent() != null) {
            this.renderWithTooltip(stack, pMouseX, pMouseY, pPartialTick);
        }
        super.render(stack, pMouseX, pMouseY, pPartialTick);
    }
}