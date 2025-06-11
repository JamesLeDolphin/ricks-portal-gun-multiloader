package com.jdolphin.ricksportalgun.client.screen.portalgun.settings;

import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.PGCycleButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGTextButton;
import com.jdolphin.ricksportalgun.client.screen.widget.SuggestionTextFieldWidget;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public class SecuritySettingsScreen extends AbstractBaseScreen {
    private PGCycleButton<Boolean> lockButton;
    private SuggestionTextFieldWidget playerInput;
    private final List<String> players;
    private EditBox code;
    private PGTextButton select, cancel;

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
        LocalPlayer player = minecraft.player;
        ItemStack stack = player.getMainHandItem();

        MutableComponent sTrue = Component.translatable("ricksportalgun.button.true");
        MutableComponent sFalse = Component.translatable("ricksportalgun.button.false");

        this.lockButton = this.addRenderableWidget(PGCycleButton.booleanBuilder(sTrue, sFalse).withInitialValue(false)
                .create(this.width / 2 + 64, this.height / 2 - 74, 64, 20,
                        Component.translatable("ricksportalgun.button.lock")));

        this.playerInput = this.addRenderableWidget(new SuggestionTextFieldWidget(this.width / 2 + 64, this.height / 2 - 48, 64, 20,
                Component.translatable("chat.editBox"), players));
        this.addRenderableWidget(playerInput.getSuggestionList());

        this.code = this.addRenderableWidget(new EditBox(this.font, this.width / 2 + 64, this.height / 2 - 22, 64, 20, Component.translatable("chat.editBox")));

        this.select = this.addRenderableWidget(new PGTextButton(this.width / 2 - 136, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.select"), (button) -> {
                    //TODO
                    this.onClose();
                }, this.font));

        this.cancel = this.addRenderableWidget(new PGTextButton(this.width / 2 + 8, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.cancel"),
                (button) -> this.onClose(), this.font));

        PortalGunStyle style = getStyle();
        this.lockButton.setTextColor(style.textColor());
        this.lockButton.setRenderBackground(false);
        this.playerInput.setBordered(true);
        this.playerInput.setResponder(s -> playerInput.update());
        this.playerInput.setMaxLength(64);
        this.playerInput.getSuggestionList().setBorderColor(style.highlightColor());
        this.select.setTextColour(style.textColor());
        this.cancel.setTextColour(style.textColor());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        PortalGunStyle style = getStyle();

        graphics.fill(this.width / 2 - 154, this.height / 2 - 110, this.width / 2 + 165, this.height / 2 + 100, style.bgColor());
        graphics.drawCenteredString(this.font, Component.translatable("menu.ricksportalgun.settings.security"), this.width / 2, 30, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.button.lock"), this.width / 4 - 16, this.lockButton.getY() + 4, getStyle().textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.button.ownership"), this.width / 4 - 16, this.playerInput.getY() + 4, getStyle().textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.button.code"), this.width / 4 - 16, this.code.getY() + 4, getStyle().textColor());

        GuiHelper.renderWidgets(graphics, mouseX, mouseY, delta, playerInput, lockButton);
        GuiHelper.renderOutline(graphics, lockButton, style.highlightColor());
        GuiHelper.renderOutline(graphics, playerInput, style.highlightColor());
        GuiHelper.renderOutline(graphics, select, style.highlightColor());
        GuiHelper.renderOutline(graphics, cancel, style.highlightColor());

        Style guiStyle = GuiHelper.getStyle(mouseX, mouseY);
        if (guiStyle != null && guiStyle.getHoverEvent() != null) {
            this.renderWithTooltip(graphics, mouseX, mouseY, delta);
        }
        graphics.blit(RenderType::guiTextured, BG_LOCATION, this.width / 2 - 158, this.height / 2 - 115, 0, 0, 330, 224, 330, 224);
        super.render(graphics, mouseX, mouseY, delta);
    }
}
