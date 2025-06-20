package com.jdolphin.ricksportalgun.client.screen.portalgun.settings;

import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.portalgun.SettingsScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.PGCycleButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGImageButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGTextButton;
import com.jdolphin.ricksportalgun.client.screen.widget.SuggestionTextFieldWidget;
import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBSecuritySettingsPacket;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public class SecuritySettingsScreen extends AbstractBaseScreen {
    private PGCycleButton<Boolean> lockButton, selfDestruct;
    private SuggestionTextFieldWidget playerInput;
    private final List<String> players;
    private EditBox code;
    private PGTextButton select, cancel;
    private PGImageButton backButton;

    public SecuritySettingsScreen(List<String> players) {
        super("menu.ricksportalgun.settings.security");
        this.players = players;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Optional<GuiEventListener> optional = this.getChildAt(mouseX, mouseY);
        if (optional.isEmpty()) {
            playerInput.setFocused(false);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void init() {
        super.init();
        assert minecraft != null;
        ItemStack stack = getItemStack();
        boolean destruct = stack.getOrDefault(PGDataComponents.SELF_DESTRUCT, false);
        boolean lock = stack.getOrDefault(PGDataComponents.LOCK, false);
        MutableComponent sTrue = Component.translatable("ricksportalgun.button.true");
        MutableComponent sFalse = Component.translatable("ricksportalgun.button.false");

        this.lockButton = this.addRenderableWidget(PGCycleButton.booleanBuilder(sTrue, sFalse).withInitialValue(lock)
                .create(this.width / 2 + 64, this.height / 2 - 70, 64, 20,
                        Component.translatable("ricksportalgun.button.lock")));

        this.playerInput = this.addWidget(new SuggestionTextFieldWidget(this.width / 2 + 64, this.height / 2 - 44, 64, 20,
                Component.translatable("chat.editBox"), players));
        this.addRenderableWidget(playerInput.getSuggestionList());

        this.code = this.addWidget(new EditBox(this.font, this.width / 2 + 64, this.height / 2 - 18, 64, 20, Component.translatable("chat.editBox")));

        this.selfDestruct = this.addRenderableWidget(PGCycleButton.booleanBuilder(sTrue, sFalse).withInitialValue(destruct)
                .create(this.width / 2 + 64, this.height / 2 + 8, 64, 20,
                        Component.translatable("ricksportalgun.button.lock")));

        this.select = this.addRenderableWidget(new PGTextButton(this.width / 2 - 136, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.select"), (button) -> {
            SBSecuritySettingsPacket packet = new SBSecuritySettingsPacket(this.lockButton.getValue(), this.playerInput.getValue(), this.code.getValue(), this.selfDestruct.getValue());
            PGHelper.sendPacketToServer(packet);
            this.onClose();
        }, this.font));

        this.cancel = this.addRenderableWidget(new PGTextButton(this.width / 2 + 8, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.cancel"),
                (button) -> this.onClose(), this.font));

        this.backButton = this.addRenderableWidget(new PGImageButton(this.width / 2 - 140, this.height / 2 - 96, 20, 20, Component.translatable("ricksportalgun.button.back"),
                (button) -> minecraft.setScreen(new SettingsScreen()), 20, 20, BACK_BUTTON_TEXTURE));

        PortalGunStyle style = getStyle();
        this.lockButton.setTextColor(style.textColor());
        this.lockButton.setRenderBackground(false);
        this.selfDestruct.setTextColor(style.textColor());
        this.selfDestruct.setRenderBackground(false);
        this.playerInput.setBordered(true);
        this.playerInput.setResponder(s -> playerInput.update());
        this.playerInput.setMaxLength(64);
        this.code.setBordered(true);
        this.playerInput.getSuggestionList().setBorderColor(style.highlightColor());
        this.select.setTextColour(style.textColor());
        this.cancel.setTextColour(style.textColor());
        this.backButton.setColor(style.highlightColor());
        this.backButton.setRenderBackground(false);
        GuiHelper.setTooltip(backButton, Component.translatable("ricksportalgun.button.back"));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        PortalGunStyle style = getStyle();

        graphics.fill(this.width / 2 - 154, this.height / 2 - 110, this.width / 2 + 165, this.height / 2 + 100, style.bgColor());

        graphics.drawCenteredString(this.font, Component.translatable("menu.ricksportalgun.settings.security"), this.width / 2, this.height / 2 - 92, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.button.lock"), this.width / 2 - 128, this.lockButton.getY() + 4, getStyle().textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.button.ownership"), this.width / 2 - 128, this.playerInput.getY() + 4, getStyle().textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.button.code"), this.width / 2 - 128, this.code.getY() + 4, getStyle().textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.button.self_destruct"), this.width / 2 - 128, this.selfDestruct.getY() + 4, getStyle().textColor());

        GuiHelper.renderWidgets(graphics, mouseX, mouseY, delta, playerInput, lockButton, code);
        GuiHelper.renderOutline(graphics, lockButton, style.highlightColor());
        GuiHelper.renderOutline(graphics, playerInput, style.highlightColor());
        GuiHelper.renderOutline(graphics, code, style.highlightColor());
        GuiHelper.renderOutline(graphics, select, style.highlightColor());
        GuiHelper.renderOutline(graphics, cancel, style.highlightColor());
        GuiHelper.renderOutline(graphics, selfDestruct, style.highlightColor());
        GuiHelper.renderOutline(graphics, backButton, style.highlightColor());

        Style guiStyle = GuiHelper.getStyle(mouseX, mouseY);
        if (guiStyle != null && guiStyle.getHoverEvent() != null) {
            this.renderWithTooltip(graphics, mouseX, mouseY, delta);
        }
        graphics.blit(RenderType::guiTextured, BG_LOCATION, this.width / 2 - 158, this.height / 2 - 115, 0, 0, 330, 224, 330, 224);
        super.render(graphics, mouseX, mouseY, delta);
    }
}
