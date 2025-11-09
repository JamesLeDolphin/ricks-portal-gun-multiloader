package com.jdolphin.ricksportalgun.client.screen.portalgun.settings;

import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.portalgun.SettingsScreen;
import com.jdolphin.ricksportalgun.client.screen.portalgun.WaypointScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.PGImageButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGItemButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGTextButton;
import com.jdolphin.ricksportalgun.client.screen.widget.ScrollableList;
import com.jdolphin.ricksportalgun.common.customization.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.init.PGUpgradeTypes;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.item.upgrade.UpgradeItem;
import com.jdolphin.ricksportalgun.common.item.upgrade.types.PreConditionUpgrade;
import com.jdolphin.ricksportalgun.common.item.upgrade.types.UpgradeType;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBAddUpgradePacket;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.List;

public class UpgradesInfoScreen extends AbstractBaseScreen {
    private PGImageButton backButton, addUpgrade;
    private UpgradeListWidget upgradeListWidget;
    private final List<PGItemButton> itemButtons = new ArrayList<>();
    private boolean showUpgradeButtons = false;
    public UpgradesInfoScreen() {
        super(Component.translatable("menu.ricksportalgun.settings.upgrades"));
    }

    @Override
    protected void init() {
        super.init();
        assert minecraft != null;
        PortalGunStyle style = getStyle();
        upgradeListWidget = this.addWidget(new UpgradeListWidget(Minecraft.getInstance(), 170, 152, this.width / 2 - 75, this.height / 2 - 70, 24, style));

        this.addUpgrade = this.addRenderableWidget(new PGImageButton(this.width / 2 + 118, this.height / 2 - 94, 20, 20, Component.translatable("ricksportalgun.button.waypoint.new"),
                (button) -> showUpgradeButtons = !showUpgradeButtons, 20, 20, WaypointScreen.NEW_WAYPOINT_TEXTURES));

        this.backButton = this.addRenderableWidget(new PGImageButton(this.width / 2 - 140, this.height / 2 - 96, 20, 20, Component.translatable("ricksportalgun.button.back"),
                (button) -> minecraft.setScreen(new SettingsScreen()), 20, 20, BACK_BUTTON_TEXTURE));

        initUpgrade(this.width / 2 + 98, this.height / 2 - 64);

        this.addUpgrade.setColor(style.highlightColor());
        this.addUpgrade.setRenderBackground(false);
        this.backButton.setColor(style.highlightColor());
        this.backButton.setRenderBackground(false);
        GuiHelper.setTooltip(backButton, Component.translatable("ricksportalgun.button.back"));
        GuiHelper.setTooltip(addUpgrade, Component.translatable("ricksportalgun.button.upgrade_new"));
    }

    private void initUpgrade(int x, int y) {
        itemButtons.clear();

        int i = 0;
        List<String> types = PortalGunItem.getUpgrades(getItemStack());
        for (UpgradeItem upgrade : PGItems.UPGRADES) {
            int offsetX = x + (i % 3) * 20;
            int offsetY = y + (i / 3) * 20;

            Player player = minecraft.player;
            boolean hasItem = player.getInventory().hasAnyMatching(stack -> stack.getItem().equals(upgrade));
            boolean hasPrevious = (upgrade.getUpgradeType() instanceof PreConditionUpgrade pre && types.contains(pre.getConditionTag())) || !(upgrade.getUpgradeType() instanceof PreConditionUpgrade);
            boolean hasUpgradePreviously = types.contains(upgrade.getUpgradeType().getUpgradeTag());
            boolean canApply = player.isCreative() || (hasItem && !hasUpgradePreviously && hasPrevious);

            PGItemButton button = this.addRenderableWidget(new PGItemButton(offsetX, offsetY, 20, 20,  upgrade.getUpgradeType().getName(), btn -> {
                if (canApply) {
                    SBAddUpgradePacket packet = new SBAddUpgradePacket(upgrade.getUpgradeType().getId());
                    PGHelper.sendPacketToServer(packet);
                }
            }, upgrade.getDefaultInstance()));
            button.visible = showUpgradeButtons;
            button.active = canApply;
            button.setRenderBackground(false);
            itemButtons.add(button);
            MutableComponent desc = upgrade.getUpgradeType().getName().copy();
            if (!player.isCreative()) {
                if (!hasItem && !hasUpgradePreviously) desc.append("\n").append(Component.translatable("notice.ricksportalgun.upgrade.not_in_inventory"));
                if (!hasPrevious) desc.append("\n").append(Component.translatable("notice.ricksportalgun.upgrade.needs_other"));
                if (hasUpgradePreviously) desc.append("\n").append(Component.translatable("notice.ricksportalgun.upgrade.already_applied"));
            }
            GuiHelper.setTooltip(button, desc);
            i++;
        }
    }


    @Override
    public void tick() {
        super.tick();

        ItemStack stack = getItemStack();
        CompoundTag tag = stack.getOrCreateTag();
        ListTag listTag = tag.getList(PGNbtKeys.TAG_UPGRADES, 8);
        if (this.upgradeListWidget.children().size() != listTag.size()) {
            this.rebuildWidgets();
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        PortalGunStyle style = getStyle();

        graphics.fill(this.width / 2 - 154, this.height / 2 - 110, this.width / 2 + 165, this.height / 2 + 100, style.bgColor());
        graphics.drawCenteredString(this.font, Component.translatable("menu.ricksportalgun.settings.upgrades"), this.width / 2, this.height / 2 - 92, style.textColor());

        if (upgradeListWidget != null) {
            upgradeListWidget.render(graphics, mouseX, mouseY, partialTick);
            graphics.renderOutline(upgradeListWidget.getLeft(), upgradeListWidget.getTop(), upgradeListWidget.getWidth(), upgradeListWidget.getHeight(), style.highlightColor());
        }

        for (PGItemButton button : itemButtons) {
            button.visible = showUpgradeButtons;
            if (button.visible) GuiHelper.renderOutline(graphics, button, style.highlightColor());
        }

        GuiHelper.renderOutline(graphics, backButton, style.highlightColor());
        GuiHelper.renderOutline(graphics, addUpgrade, style.highlightColor());

        RenderSystem.enableBlend();
        graphics.blit(BG_LOCATION, this.width / 2 - 158, this.height / 2 - 115, 0, 0, 330, 224, 330, 224);
        RenderSystem.disableBlend();
        Style guiStyle = GuiHelper.getStyle(mouseX, mouseY);
        if (guiStyle != null && guiStyle.getHoverEvent() != null) {
            this.renderWithTooltip(graphics, mouseX, mouseY, partialTick);
        }
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    public static class UpgradeListWidget extends ScrollableList<UpgradeListWidget.UpgradeEntry> {

        private final PortalGunStyle style;
        public UpgradeListWidget(Minecraft minecraft, int width, int height, int x0, int pY0, int pItemHeight, PortalGunStyle style) {
            super(minecraft, width, height, x0, pY0, pItemHeight);
            assert minecraft.player != null;
            this.style = style;
            refreshEntries(minecraft.player.getItemInHand(PGHelper.getPortalGunHand(minecraft.player)));
            this.setRenderScrollbar(false);
        }

        public void refreshEntries(ItemStack stack) {
            this.children().clear();
            List<String> upgrades = PortalGunItem.getUpgrades(stack);

            for (String upgrade : upgrades) {
                if (upgrade != null) {
                    this.addEntry(new UpgradeEntry(PGUpgradeTypes.getFromTag(upgrade), this, this.style.highlightColor()));
                } else LogManager.getLogger().warn("Failed to get upgrade");
            }
        }

        @Override
        public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
            super.render(graphics, pMouseX, pMouseY, pPartialTick);

            if (this.children().isEmpty()) {
                graphics.drawCenteredString(Minecraft.getInstance().font, Component.translatable("notice.ricksportalgun.no_upgrades"), this.x0 + this.width / 2, this.y0 + this.height / 2, style.textColor());
            }
        }

        @Override
        public void updateNarration(NarrationElementOutput narrationElementOutput) {}

        public static class UpgradeEntry extends ScrollableList.Entry<UpgradeListWidget.UpgradeEntry> {

            UpgradeType type;
            protected UpgradeListWidget list;
            private final PGTextButton button;
            private final int color;
            public UpgradeEntry(UpgradeType type, UpgradeListWidget widget, int color) {
                this.type = type;
                this.list = widget;
                this.color = color;

                this.button = new PGTextButton(16, 0, 152, 20, type.getName(), button1 -> {}, Minecraft.getInstance().font);
                GuiHelper.setTooltip(button, type.getDescription());
            }

            @Override
            public void render(GuiGraphics graphics,int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pIsMouseOver, float pPartialTick) {

                if (pTop > this.list.headerHeight) {
                    this.button.setX(pLeft - 24);
                    this.button.setY(pTop);
                    this.button.render(graphics, pMouseX, pMouseY, pPartialTick);
                    GuiHelper.renderOutline(graphics, button, this.color);
                }
            }

            @Override
            public List<? extends GuiEventListener> children() {
                return List.of();
            }
        }
    }
}
