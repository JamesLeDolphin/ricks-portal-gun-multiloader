package com.jdolphin.ricksportalgun.client.screen.workbench;

import com.jdolphin.ricksportalgun.client.screen.widget.PGItemButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGScrollableWidget;
import com.jdolphin.ricksportalgun.client.screen.widget.WaypointListWidget;
import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.menu.workbench.WaypointTransferMenu;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBWorkbenchWaypointEditPackage;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import oshi.util.tuples.Pair;

import java.awt.*;
import java.util.Optional;

public class WaypointTransferScreen extends AbstractWorkbenchScreen<WaypointTransferMenu> {
    public static final ResourceLocation BG = PGHelper.id("textures/gui/workbench/waypoint_transfer.png");
    public static final ResourceLocation WP_INFO_BG = PGHelper.id("textures/gui/workbench/waypoint_info_bg.png");
    private WaypointListWidget leftWaypointList, rightWaypointList;
    private Pair<Waypoint, Side> selectedWaypoint;
    private Button copy, moveTo, delete;

    public WaypointTransferScreen(WaypointTransferMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 211;
        this.imageWidth = 212;
        this.inventoryLabelX = this.imageWidth - 188;
        this.inventoryLabelY = this.imageHeight - 93;
        this.titleLabelX = this.imageWidth - 193;
        this.titleLabelY = this.imageHeight - 207;
    }

    public boolean mouseScrolled(double d, double d1, double d2, double d3) {
        Optional<GuiEventListener> optional = this.getChildAt(d, d1);
        if (optional.isPresent()) {
            if (optional.get() instanceof PGScrollableWidget<?> list) {
                return list.mouseScrolled(d, d1, d2, d3);
            }
        }
        return super.mouseScrolled(d, d1, d2, d3);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Optional<GuiEventListener> optional = this.getChildAt(mouseX, mouseY);
        if (optional.isEmpty()) {
            leftWaypointList.setFocused(false);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void init() {
        super.init();

        this.leftWaypointList = this.addRenderableWidget(new WaypointListWidget(73, 100, this.width / 2 - 86,
                this.height / 2 - 93, 16, getTopSlotItem(), false, 70, 16));
        leftWaypointList.setRenderScrollbar(false);

        this.rightWaypointList = this.addRenderableWidget(new WaypointListWidget(73, 100, this.width / 2 + 13,
                this.height / 2 - 93, 16, getBottomSlotItem(), false, 70, 16));
        rightWaypointList.setRenderScrollbar(false);

        Button.OnPress onPress =(button -> {
            Optional<GuiEventListener> optional = this.getChildAt(button.getX(), button.getY());
            if (optional.isPresent()) {
                GuiEventListener listener = optional.get();
                if (listener instanceof WaypointListWidget widget) {
                    WaypointListWidget.WaypointEntry entry = widget.getEntryAtPosition(button.getX(), button.getY());
                    if (entry != null) {
                        Side side = widget.equals(leftWaypointList) ? Side.LEFT : Side.RIGHT;
                        this.selectedWaypoint = new Pair<>(entry.waypoint, side);
                    }
                }
            }
        });
        leftWaypointList.setOnPress(onPress);
        rightWaypointList.setOnPress(onPress);

        this.copy = this.addWidget(Button.builder(Component.translatable("ricksportalgun.button.workbench.copy"), button -> {
            if (selectedWaypoint != null) {
                Waypoint wp = selectedWaypoint.getA();
                sendPacket(wp, true, false);
            }
        }).bounds(this.width / 2 - 175, this.height / 2 - 10, 80, 16).build());

        this.moveTo = this.addWidget(Button.builder(Component.translatable("ricksportalgun.button.workbench.move"), button -> {
            if (selectedWaypoint != null) {
                Waypoint wp = selectedWaypoint.getA();
                sendPacket(wp, false, false);
                selectedWaypoint = null;
            }
        }).bounds(this.width / 2 - 175, this.height / 2 + 8, 80, 16).build());

        this.delete = this.addWidget(Button.builder(Component.translatable("ricksportalgun.button.delete"), button -> {
            if (selectedWaypoint != null) {
                Waypoint wp = selectedWaypoint.getA();
                sendPacket(wp, false, true);
                selectedWaypoint = null;
            }
        }).bounds(this.width / 2 - 175, this.height / 2 + 26, 80, 16).build());

        PGItemButton skin = this.addRenderableWidget(new PGItemButton(this.width / 2 + 91, this.height / 2 - 105, 24, 24,
                Component.translatable("menu.ricksportalgun.workbench.skin"), button -> setScreen(1), PGItems.PORTAL_GUN.getDefaultInstance()));
        skin.setRenderBackground(false);

        PGItemButton waypoint = this.addRenderableWidget(new PGItemButton(this.width / 2 + 91, this.height / 2 - 80, 24, 24,
                Component.translatable("menu.ricksportalgun.workbench.waypoint"), button -> {}, PGItems.DATA_CARD.getDefaultInstance()));
        waypoint.setRenderBackground(false);

        PGItemButton craft = this.addRenderableWidget(new PGItemButton(this.width / 2 + 91, this.height / 2 - 55, 24, 24,
                Component.translatable("menu.ricksportalgun.workbench.craft"), button -> setScreen(2), Items.CRAFTING_TABLE.getDefaultInstance()));
        craft.setRenderBackground(false);

        GuiHelper.setTooltip(skin, Component.translatable("menu.ricksportalgun.workbench.skin"));
        GuiHelper.setTooltip(waypoint, Component.translatable("menu.ricksportalgun.workbench.waypoint"));
        GuiHelper.setTooltip(craft, Component.translatable("menu.ricksportalgun.workbench.craft"));

        GuiHelper.setTooltip(copy, Component.translatable("tooltip.ricksportalgun.button.copy"));
        GuiHelper.setTooltip(moveTo, Component.translatable("tooltip.ricksportalgun.button.move"));
        GuiHelper.setTooltip(delete, Component.translatable("tooltip.ricksportalgun.button.delete"));
    }

    private void sendPacket(Waypoint waypoint, boolean copy, boolean delete) {
        SBWorkbenchWaypointEditPackage packet = new SBWorkbenchWaypointEditPackage(waypoint, selectedWaypoint.getB() == Side.LEFT, copy, delete);
        PGHelper.sendPacketToServer(packet);
    }

    private ItemStack getTopSlotItem() {
        return this.menu.getSlot(36).getItem();
    }

    private ItemStack getBottomSlotItem() {
        return this.menu.getSlot(37).getItem();
    }

    private ItemStack getItem(Side side) {
        return side == Side.LEFT ? getTopSlotItem() : getBottomSlotItem();
    }

    public void containerTick() {
        super.containerTick();

        ItemStack leftStack = getTopSlotItem();
        ItemStack rightStack = getBottomSlotItem();
        if (leftWaypointList != null) leftWaypointList.refreshEntries(leftStack);
        if (rightWaypointList != null) rightWaypointList.refreshEntries(rightStack);

        if (selectedWaypoint != null) {
            if ((selectedWaypoint.getB() == Side.LEFT && leftStack.isEmpty()) ||
                    (selectedWaypoint.getB() == Side.RIGHT && rightStack.isEmpty())) this.selectedWaypoint = null;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        GuiHelper.renderWidgets(graphics, mouseX, mouseY, delta, leftWaypointList, rightWaypointList);

        if (selectedWaypoint != null) {
            delete.render(graphics, mouseX, mouseY, delta);
            ItemStack opposite = getItem(selectedWaypoint.getB().opposite());
            if (!opposite.isEmpty()) {
                CompoundTag tag = opposite.getOrCreateTag();
                if (!opposite.is(PGTags.Items.PORTAL_GUNS) || (opposite.is(PGTags.Items.PORTAL_GUNS) &&
                        tag.contains(PGNbtKeys.UPGRADE_WAYPOINT)) ) {

                copy.render(graphics, mouseX, mouseY, delta);
                moveTo.render(graphics, mouseX, mouseY, delta);
            }
                }
            renderWaypointInfo(graphics, mouseX, mouseY, delta);
        }
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    private void renderWaypointInfo(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        Waypoint waypoint = this.selectedWaypoint.getA();
        GuiHelper.drawWhiteString(graphics, Component.translatable("ricksportalgun.name", ""), this.width / 2 - 172, this.height / 2 - 96);
        GuiHelper.renderScrollingString(graphics, Component.literal(waypoint.getName()), this.width / 2 - 172, this.height / 2 - 84,
                this.width / 2 - 100, this.height / 2 - 74, Color.WHITE.getRGB());
        GuiHelper.drawWhiteString(graphics, Component.translatable("ricksportalgun.dimension", ""), this.width / 2 - 172, this.height / 2 - 72);
        GuiHelper.renderScrollingString(graphics, Component.literal(waypoint.getDimension()), this.width / 2 - 172, this.height / 2 - 60,
                this.width / 2 - 100, this.height / 2 - 50, Color.WHITE.getRGB());
        GuiHelper.drawWhiteString(graphics, Component.translatable("ricksportalgun.x", waypoint.getX()), this.width / 2 - 172, this.height / 2 - 48);
        GuiHelper.drawWhiteString(graphics, Component.translatable("ricksportalgun.y", waypoint.getY()), this.width / 2 - 172, this.height / 2 - 36);
        GuiHelper.drawWhiteString(graphics, Component.translatable("ricksportalgun.z", waypoint.getZ()), this.width / 2 - 172, this.height / 2 - 24);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        if (this.selectedWaypoint != null) {
            graphics.blit(WP_INFO_BG, this.width / 2 - 180, y, 0f, 0f, 128, 256, 128, 256);
        }
        graphics.blit(BG, x + 12, y, 0f, 0f, imageWidth, imageHeight, 256, 256);
    }

    public enum Side {
        LEFT,
        RIGHT;

        public Side opposite() {
            return this == LEFT ? RIGHT : LEFT;
        }
    }
}
