package com.jdolphin.ricksportalgun.client.screen.portalgun.settings;

import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.PGImageButton;
import com.jdolphin.ricksportalgun.client.screen.widget.ScrollableList;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.item.upgrade.types.UpgradeType;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBOpenCoordGuiPacket;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;

import java.awt.*;
import java.util.List;

public class UpgradesInfoScreen extends AbstractBaseScreen {
    private PGImageButton backButton;
    private UpgradeListWidget upgradeListWidget;

    public UpgradesInfoScreen() {
        super(Component.translatable("menu.ricksportalgun.settings.upgrades"));
    }

    @Override
    protected void init() {
        super.init();

        upgradeListWidget = this.addWidget(new UpgradeListWidget(Minecraft.getInstance(), 170, 152, this.width / 2 - 75, this.height / 2 - 70, 24));

        this.backButton = this.addRenderableWidget(new PGImageButton(this.width / 2 - 140, this.height / 2 - 96, 20, 20, Component.translatable("ricksportalgun.button.back"),
                (button) -> {
                    SBOpenCoordGuiPacket packet = new SBOpenCoordGuiPacket();
                    PGHelper.sendPacketToServer(packet);
                }, 20, 20, BACK_BUTTON_TEXTURE));

        PortalGunStyle style = getStyle();
        this.backButton.setColor(style.highlightColor());
        this.backButton.setRenderBackground(false);
        GuiHelper.setTooltip(backButton, Component.translatable("ricksportalgun.button.back"));
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

        GuiHelper.renderOutline(graphics, backButton, style.highlightColor());

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

        private ItemStack stack;
        public UpgradeListWidget(Minecraft minecraft, int width, int height, int x0, int pY0, int pItemHeight) {
            super(minecraft, width, height, x0, pY0, pItemHeight);
            assert minecraft.player != null;
            refreshEntries(minecraft.player.getItemInHand(PGHelper.getPortalGunHand(minecraft.player)));
        }

        public void refreshEntries(ItemStack stack) {
            this.children().clear();
            List<UpgradeType> upgrades = PortalGunItem.getUpgrades(stack);

            for (UpgradeType upgrade : upgrades) {
                if (upgrade != null) {
                    this.addEntry(new UpgradeEntry(upgrade));
                } else LogManager.getLogger().warn("Failed to get upgrade");
            }
        }


        @Override
        public void updateNarration(NarrationElementOutput narrationElementOutput) {}

        public static class UpgradeEntry extends ScrollableList.Entry<UpgradeListWidget.UpgradeEntry> {

            UpgradeType type;
            public UpgradeEntry(UpgradeType type) {
                this.type = type;
            }

            @Override
            public void render(GuiGraphics graphics,int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pIsMouseOver, float pPartialTick) {
                graphics.drawString(Minecraft.getInstance().font, type.getId(), pWidth / 2, pHeight, Color.GREEN.getRGB());
            }

            @Override
            public List<? extends GuiEventListener> children() {
                return List.of();
            }
        }
    }
}
