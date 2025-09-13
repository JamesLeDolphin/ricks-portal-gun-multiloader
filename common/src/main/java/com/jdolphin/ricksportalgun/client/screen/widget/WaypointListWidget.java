package com.jdolphin.ricksportalgun.client.screen.widget;

import com.jdolphin.ricksportalgun.client.screen.portalgun.WaypointInfoScreen;
import com.jdolphin.ricksportalgun.common.item.IWaypointStorage;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBSetDestinationPacket;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WaypointListWidget extends PGScrollableWidget<WaypointListWidget.WaypointEntry> {
    public static ResourceLocation WAYPOINT_INFO_TEXTURES = PGHelper.id("textures/gui/sprites/icon/waypoint_info.png");

    public boolean showInfoButton;
    public int rowWidth = 188;
    private final int buttonWidth;
    private final int buttonHeight;
    private boolean renderButtonBg = true;
    protected Button.OnPress onPress;
    private PortalGunStyle style = PortalGunStyle.DEFAULT;

    public WaypointListWidget(int width, int height, int x, int y, int itemHeight, ItemStack stack, boolean showInfo, int buttonWidth, int buttonHeight) {
        super(Minecraft.getInstance(), width, height, x, y, itemHeight);
        this.showInfoButton = showInfo;
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;
        this.refreshEntries(stack);
    }

    public void setStyle(PortalGunStyle style) {
        this.style = style;
    }

    protected void renderListBackground(GuiGraphics guiGraphics) {}

    public void refreshEntries(ItemStack stack) {
        this.children().clear();
        List<Waypoint> waypoints = IWaypointStorage.getWaypoints(stack);

        for (Waypoint waypoint : waypoints) {
            if (waypoint != null) {
                this.addEntry(new WaypointEntry(waypoint, this, this.showInfoButton, this.renderButtonBg, this.style.highlightColor()));
            } else LogManager.getLogger().warn("Failed to get Waypoint");
        }
    }

    public int getRowWidth() {
        return rowWidth;
    }

    public void setOnPress(Button.OnPress onPress) {
        this.onPress = onPress;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.USAGE, Component.empty());
    }

    public void setRenderButtonBackground(boolean renderButtonBg) {
        this.renderButtonBg = renderButtonBg;
    }

    public static class WaypointEntry extends Entry<WaypointListWidget.WaypointEntry> {

        private final Button button;
        private final PGImageButton infoButton;
        protected WaypointListWidget list;
        private final boolean showInfo;
        private final int color;
        private final boolean renderBG;
        public final Waypoint waypoint;

        WaypointEntry(Waypoint waypoint, WaypointListWidget list, boolean showInfoButton, boolean renderButtonBG, int borderColor) {
            this.waypoint = waypoint;
            this.list = list;
            this.showInfo = showInfoButton;
            this.color = borderColor;
            this.renderBG = renderButtonBG;

            Button.OnPress press = this.list.onPress;
            if (this.list.onPress == null) {
                press = (pButton) -> {
                    SBSetDestinationPacket packet = new SBSetDestinationPacket(waypoint.getBlockPos(), waypoint.getDimension());
                    PGHelper.sendPacketToServer(packet);
                    Minecraft.getInstance().setScreen(null);
                };
            }

            if (renderButtonBG) {
                this.button = Button.builder(Component.literal(waypoint.getName()), press)
                        .pos(16, 0).size(list.buttonWidth, list.buttonHeight).build();
            } else {
                this.button = new PGTextButton(16, 0, list.buttonWidth, list.buttonHeight, Component.literal(waypoint.getName()), press, Minecraft.getInstance().font);
            }
                this.infoButton = new PGImageButton(0, 0, 20, 20, Component.translatable("ricksportalgun.button.waypoint.info"),
                        (button) ->
                                Minecraft.getInstance().setScreen(new WaypointInfoScreen(waypoint)), 20, 20, WAYPOINT_INFO_TEXTURES);
            this.infoButton.setRenderBackground(renderButtonBG);
            this.infoButton.setColor(this.list.style.textColor());
            this.infoButton.setTooltip(Tooltip.create(Component.translatable("ricksportalgun.button.waypoint.info")));
            if (this.button instanceof PGTextButton textButton) {
                textButton.setTextColour(this.list.style.textColor());
            }
        }

        @Override
        public void render(@NotNull GuiGraphics graphics, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pIsMouseOver, float pPartialTick) {
            WaypointListWidget wpList = this.list;
            if (pTop > wpList.headerHeight) {

                this.button.setX(pLeft + (showInfo ? 18 : 56));
                this.button.setY(pTop);
                this.button.render(graphics, pMouseX, pMouseY, pPartialTick);
                if (!renderBG) GuiHelper.renderOutline(graphics, button, this.color);

                if (this.showInfo) {
                    this.infoButton.setX(pLeft + button.getWidth() + 20);
                    this.infoButton.setY(pTop);
                    if (!renderBG) GuiHelper.renderOutline(graphics, infoButton, this.color);
                    this.infoButton.render(graphics, pMouseX, pMouseY, pPartialTick);
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
