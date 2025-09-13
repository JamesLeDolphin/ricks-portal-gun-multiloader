package com.jdolphin.ricksportalgun.client.screen.portalgun;

import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.PGCycleButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGImageButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGTextButton;
import com.jdolphin.ricksportalgun.client.screen.widget.SuggestionTextFieldWidget;
import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBLocatePacket;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBOpenCoordGuiPacket;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Optional;

public class LocatorScreen extends AbstractBaseScreen {
    private SuggestionTextFieldWidget input;
    private PGTextButton select, cancel;
    private PGCycleButton<LocatorType> locatorType;
    private final List<String> playerList, biomeList, structureList;
    private PGImageButton backButton;

    public LocatorScreen(final List<String> playerList, final List<String> biomeList, final List<String> structureList) {
        super("menu.ricksportalgun.player_locator");
        this.playerList = playerList;
        this.biomeList = biomeList;
        this.structureList = structureList;
    }

    private List<String> getListFromType(LocatorType type) {
        switch (type) {
            case STRUCTURE -> {
                return this.structureList;
            }
            case PLAYER -> {
                return this.playerList;
            }
            default -> {
                return this.biomeList;
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Optional<GuiEventListener> optional = this.getChildAt(mouseX, mouseY);
        if (optional.isEmpty()) {
            input.setFocused(false);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private LocatorType[] getAllowedLocators() {
        ItemStack stack = getItemStack();
        CompoundTag tag = stack.getOrCreateTag();
        boolean canPlayerLocate = tag.contains(PGNbtKeys.UPGRADE_PLAYER_LOC) && tag.getBoolean(PGNbtKeys.UPGRADE_PLAYER_LOC);
        boolean canStructureLocate = tag.contains(PGNbtKeys.UPGRADE_STRUCTURE_LOC) && tag.getBoolean(PGNbtKeys.UPGRADE_STRUCTURE_LOC);

        if (canPlayerLocate && canStructureLocate) {
            return LocatorType.values();
        } else if (!canPlayerLocate && !canStructureLocate) {
            return new LocatorType[]{LocatorType.BIOME};
        } else if (canPlayerLocate && !canStructureLocate) {
            return new LocatorType[]{LocatorType.BIOME, LocatorType.PLAYER};
        }
        return new LocatorType[]{};
    }

    @Override
    protected void init() {

        this.locatorType = this.addRenderableWidget(PGCycleButton.builder(LocatorType::getDisplayName)
                .withValues(getAllowedLocators())
                .create(this.width / 2 - 64, this.height / 2 - 64, 128, 20, Component.translatable("ricksportalgun.button.locator"),
                        (button, type) -> {
                            List<String> list = getListFromType(type);
                            this.input.setSuggestions(list);
                        }));

        this.input = this.addWidget(new SuggestionTextFieldWidget(this.width / 2 - 64, this.height / 2 - 32, 128, 24,
                Component.translatable("chat.editBox"), getListFromType(this.locatorType.getValue())));
        this.addRenderableWidget(this.input.getSuggestionList());

        this.select = this.addRenderableWidget(new PGTextButton(this.width / 2 - 136, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.select"), (button) -> {
            this.setCoords();
            this.onClose();
        }, this.font));

        this.backButton = this.addRenderableWidget(new PGImageButton(this.width / 2 - 140, this.height / 2 - 96, 20, 20, Component.translatable("ricksportalgun.button.back"),
                (button) -> {
                    SBOpenCoordGuiPacket packet = new SBOpenCoordGuiPacket();
                    PGHelper.sendPacketToServer(packet);
                }, 20, 20, BACK_BUTTON_TEXTURE));

        this.cancel = this.addRenderableWidget(new PGTextButton(this.width / 2 + 8, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.cancel"), (button) -> this.onClose(), this.font));


        PortalGunStyle style = getStyle();
        this.input.setMaxLength(256);
        this.input.setBordered(true);
        this.input.setResponder(s -> input.update());
        this.locatorType.setRenderBackground(false);
        this.locatorType.setTextColor(style.textColor());
        this.locatorType.setRenderArrows(true);
        this.input.getSuggestionList().setBorderColor(style.highlightColor());
        this.backButton.setColor(style.highlightColor());
        this.backButton.setRenderBackground(false);
        GuiHelper.setTooltip(backButton, Component.translatable("ricksportalgun.button.back"));
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        PortalGunStyle style = getStyle();

        graphics.fill(this.width / 2 - 154, this.height / 2 - 110, this.width / 2 + 165, this.height / 2 + 100, style.bgColor());
        graphics.drawCenteredString(this.font, Component.translatable("menu.ricksportalgun.locator"), this.width / 2, this.height / 2 - 92, style.textColor());
        if (this.input != null) this.input.render(graphics, pMouseX, pMouseY, pPartialTick);

        GuiHelper.renderOutline(graphics, select, style.highlightColor());
        GuiHelper.renderOutline(graphics, cancel, style.highlightColor());
        GuiHelper.renderOutline(graphics, locatorType, style.highlightColor());
        GuiHelper.renderOutline(graphics, input, style.highlightColor());
        GuiHelper.renderOutline(graphics, backButton, style.highlightColor());

        Style guiStyle = GuiHelper.getStyle(pMouseX, pMouseY);
        if (guiStyle != null && guiStyle.getHoverEvent() != null) {
            this.renderWithTooltip(graphics, pMouseX, pMouseY, pPartialTick);
        }
        RenderSystem.enableBlend();
        graphics.blit(BG_LOCATION, this.width / 2 - 158, this.height / 2 - 115, 0, 0, 330, 224, 330, 224);
        RenderSystem.disableBlend();
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
    }

    public void setCoords() {
        SBLocatePacket packet = new SBLocatePacket(input.getValue(), this.locatorType.getValue().ordinal());
        PGHelper.sendPacketToServer(packet);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        switch (pKeyCode) {
            case GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER:
                if(this.getFocused() instanceof Button)
                    return super.keyPressed(pKeyCode, pScanCode, pModifiers);
                this.setCoords();
                break;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    public enum LocatorType {
        BIOME("biome"), //0
        PLAYER("player"), //1
        STRUCTURE("structure") //2
        ;
        final String key;
        LocatorType(String key) {
            this.key = key;
        }

        public Component getDisplayName() {
            return Component.translatable("ricksportalgun.button.locator." + this.key);
        }
    }
}
