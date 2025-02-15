package com.jdolphin.ricksportalgun.client.screen.portalgun;

import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.Slider;
import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.packet.SBSettingsPacket;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.Helper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SettingsScreen extends AbstractBaseScreen {
    private Button lockButton;
    private Slider portalSize;
    private boolean lock;
    private EditBox playerInput;
    public SettingsScreen() {
        super(Component.translatable("menu.ricksportalgun.settings"));
    }

    @Override
    public void init() {
        super.init();
        LocalPlayer player = minecraft.player;
        ItemStack stack = player.getMainHandItem();

        lock = stack.getOrDefault(PGDataComponents.LOCK, false);
        float size = stack.getOrDefault(PGDataComponents.PORTAL_SIZE, 1.0f);

        this.addRenderableWidget(Button.builder(
                Component.translatable("ricksportalgun.button.settings.done"), (button) -> {
                    SBSettingsPacket packet = new SBSettingsPacket(lock, playerInput.getValue(), (float) this.portalSize.getValue());
                    Helper.sendPacketToServer(packet);
                    this.onClose();

        }).pos(this.width / 2 - 136, this.height / 2 + 32).size(128, 20).build());
        this.addRenderableWidget(Button.builder(Component.translatable("ricksportalgun.button.cancel"), (button) -> this.onClose())
                .pos(this.width / 2 + 8, this.height / 2 + 32).size(128, 20).build());

        String sTrue = "ricksportalgun.button.true";
        String sFalse = "ricksportalgun.button.false";
        this.lockButton = this.addRenderableWidget(Button.builder(Component.translatable(lock ? sTrue : sFalse), (button) -> {
            lock = !lock;
            lockButton.setMessage(Component.translatable(lock ? sTrue : sFalse));
        }).size(64, 20).pos(this.width / 2 + 72, this.height / 2 - 74).build());

        this.portalSize = this.addRenderableWidget(new Slider(this.width / 2, this.height / 2 - 36, 36, 20,
                Component.empty(), size, 1.0f, 2.0f, true));

        this.playerInput = this.addWidget(new EditBox(this.font, this.width / 2 + 54, this.height / 2 - 50, 80, 16,
                Component.translatable("chat.editBox")));

        this.playerInput.setBordered(true);
        this.playerInput.setMaxLength(256);
    }

    @Override
    public void render(@NotNull GuiGraphics pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        GuiHelper.drawWhiteCenteredString(pPoseStack, Component.translatable("ricksportalgun.button.settings"), this.width / 2, this.height / 10);
        GuiHelper.drawWhiteString(pPoseStack, Component.translatable("ricksportalgun.button.lock"), this.width / 4 - 16, this.lockButton.getY() + 4);
        GuiHelper.drawWhiteString(pPoseStack, Component.translatable("ricksportalgun.button.ownership"), this.width / 4 - 16, this.playerInput.getY() + 4);
        GuiHelper.renderWidgets(pPoseStack, pMouseX, pMouseY, pPartialTick, lockButton, playerInput, portalSize);
        Style style = GuiHelper.getStyle(pMouseX, pMouseY);
        if (style != null && style.getHoverEvent() != null) {
            this.renderWithTooltip(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }
}