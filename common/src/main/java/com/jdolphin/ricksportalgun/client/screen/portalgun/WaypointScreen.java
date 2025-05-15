package com.jdolphin.ricksportalgun.client.screen.portalgun;

import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.BetterImageButton;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.item.IWaypointStorage;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBSetDestinationPacket;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WaypointScreen extends AbstractBaseScreen {

    public WaypointList waypointList;
    public Button addWaypoint;

    public static ResourceLocation NEW_WAYPOINT_TEXTURES = PGHelper.createLocation("icon/new_waypoint");
    public static ResourceLocation WAYPOINT_INFO_TEXTURES = PGHelper.createLocation("icon/waypoint_info");

    protected List<Waypoint> waypointCache;

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
            this.waypointList = this.addWidget(new WaypointList(this, minecraft, stack));

    }

    @Override
    public void tick() {
        super.tick();

        ItemStack heldItem = minecraft.player.getMainHandItem();
        if (heldItem.is(PGTags.Items.PORTAL_GUNS)) {
            if (waypointCache == null || !waypointCache.equals(IWaypointStorage.getWaypoints(heldItem))) {
                if (this.waypointList != null) this.waypointList.refreshEntries(heldItem);
            }
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

    public static class WaypointList extends AbstractSelectionList<WaypointList.WaypointEntry> {

        public final ItemStack stack;
        public final PortalGunItem item;
        public final WaypointScreen screen;

        public WaypointList(WaypointScreen waypointScreen, Minecraft minecraft, ItemStack stack) {
            super(minecraft, waypointScreen.width, (waypointScreen.height / 3) * 2, waypointScreen.height / 3 - 30, 21);


            this.stack = stack;
            this.screen = waypointScreen;
            this.item = (PortalGunItem) stack.getItem();

            this.refreshEntries(stack);

        }

        public void refreshEntries(ItemStack stack) {
            this.children().clear();
            List<Waypoint> waypoints = IWaypointStorage.getWaypoints(stack);
            screen.waypointCache = waypoints;

            for (Waypoint waypoint : waypoints) {
                if (waypoint != null) {
                    this.addEntry(new WaypointEntry(waypoint, this));
                } else LogManager.getLogger().warn("Failed to get Waypoint: {}", waypoint);
            }
        }

        public int getRowWidth() {
            return super.getRowWidth() - 32;
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            narrationElementOutput.add(NarratedElementType.USAGE, Component.empty());
        }

        public static class WaypointEntry extends Entry<WaypointEntry> {

            private final Waypoint waypoint;
            private final Button button, infoButton;
            protected WaypointList list;

            WaypointEntry(Waypoint waypoint, WaypointList list) {
                this.waypoint = waypoint;
                this.list = list;

                this.button = Button.builder(Component.literal(waypoint.getName()),
                        (pButton -> {
                            SBSetDestinationPacket packet = new SBSetDestinationPacket(waypoint.getBlockPos(), waypoint.getDim());
                            PGHelper.sendPacketToServer(packet);
                            list.minecraft.setScreen(null);
                        })).pos(16, 0).size(128, 20).build();

                this.infoButton = new BetterImageButton(0, 0, 20, 20, Component.translatable("ricksportalgun.button.waypoint.info"),
                        (button) ->
                                list.minecraft.setScreen(new WaypointInfoScreen(waypoint)), 20, 20, WAYPOINT_INFO_TEXTURES);

            }

            @Override
            public void render(@NotNull GuiGraphics pPoseStack, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pIsMouseOver, float pPartialTick) {
                WaypointList wpList = this.list;
                if (pTop > wpList.headerHeight ) {
                    this.infoButton.setTooltip(Tooltip.create(Component.translatable("ricksportalgun.button.waypoint.info")));
                    this.button.setX(wpList.getWidth() / 2 - 64);
                    this.button.setY(pTop);
                    this.button.setMessage(Component.literal(this.waypoint.getName()));
                    this.button.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
                    this.infoButton.setX(wpList.getWidth() / 2 + 68);
                    this.infoButton.setY(pTop);
                    this.infoButton.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

                }
            }

            public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
                if (this.button.mouseClicked(pMouseX, pMouseY, pButton)) {
                    return true;
                } else {
                    return this.infoButton.mouseClicked(pMouseX, pMouseY, pButton);
                }
            }

            public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
                return this.button.mouseReleased(pMouseX, pMouseY, pButton) || this.infoButton.mouseReleased(pMouseX, pMouseY, pButton);
            }
        }
    }
}