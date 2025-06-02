package com.jdolphin.ricksportalgun.client.screen.widget;

import com.jdolphin.ricksportalgun.client.screen.portalgun.WaypointInfoScreen;
import com.jdolphin.ricksportalgun.common.item.IWaypointStorage;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBSetDestinationPacket;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WaypointListWidget extends ScrollableList<WaypointListWidget.WaypointEntry> {
    public static ResourceLocation WAYPOINT_INFO_TEXTURES = PGHelper.createLocation("icon/waypoint_info");

    public final ItemStack stack;
    public final PortalGunItem item;
    public final Screen screen;
    public boolean showInfoButton;
    public int rowWidth = 188;
    private final int buttonWidth;
    private final int buttonHeight;

    public WaypointListWidget(Screen screen, int width, int height, int x, int y, int itemHeight, ItemStack stack, boolean showInfo, int buttonWidth, int buttonHeight) {
        super(Minecraft.getInstance(), width, height, x, y, itemHeight);

        this.stack = stack;
        this.screen = screen;
        this.item = (PortalGunItem) stack.getItem();
        this.showInfoButton = showInfo;
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;
        this.refreshEntries(stack);

    }

    public void refreshEntries(ItemStack stack) {
        this.children().clear();
        List<Waypoint> waypoints = IWaypointStorage.getWaypoints(stack);

        for (Waypoint waypoint : waypoints) {
            if (waypoint != null) {
                this.addEntry(new WaypointEntry(waypoint, this, this.showInfoButton));
            } else LogManager.getLogger().warn("Failed to get Waypoint: {}", waypoint);
        }
    }

    public int getRowWidth() {
        return rowWidth;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.USAGE, Component.empty());
    }

    public static class WaypointEntry extends Entry<WaypointListWidget.WaypointEntry> {

        private final Waypoint waypoint;
        private final Button button, infoButton;
        protected WaypointListWidget list;
        private final boolean showInfo;

        WaypointEntry(Waypoint waypoint, WaypointListWidget list, boolean showInfoButton) {
            this.waypoint = waypoint;
            this.list = list;
            this.showInfo = showInfoButton;

            this.button = Button.builder(Component.literal(waypoint.getName()),
                    (pButton -> {
                        SBSetDestinationPacket packet = new SBSetDestinationPacket(waypoint.getBlockPos(), waypoint.getDim());
                        PGHelper.sendPacketToServer(packet);
                        Minecraft.getInstance().setScreen(null);
                    })).pos(16, 0).size(list.buttonWidth, list.buttonHeight).build();
                this.infoButton = new BetterImageButton(0, 0, 20, 20, Component.translatable("ricksportalgun.button.waypoint.info"),
                        (button) ->
                                Minecraft.getInstance().setScreen(new WaypointInfoScreen(waypoint)), 20, 20, WAYPOINT_INFO_TEXTURES);
        }

        @Override
        public void render(@NotNull GuiGraphics pPoseStack, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pIsMouseOver, float pPartialTick) {
            WaypointListWidget wpList = this.list;
            if (pTop > wpList.headerHeight) {

                this.button.setX(wpList.getWidth() / 2 - 64);
                this.button.setY(pTop);
                this.button.setMessage(Component.literal(this.waypoint.getName()));
                this.button.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
                if (this.showInfo) {
                    this.infoButton.setX(wpList.getWidth() / 2 + 68);
                    this.infoButton.setY(pTop);
                    this.infoButton.setTooltip(Tooltip.create(Component.translatable("ricksportalgun.button.waypoint.info")));
                    this.infoButton.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
                }
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
