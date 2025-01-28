package com.jdolphin.ricksportalgun.client.gui.portalgun;

import com.jdolphin.ricksportalgun.client.gui.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.packet.SBSettingsPacket;
import com.jdolphin.ricksportalgun.common.util.helpers.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SettingsScreen extends AbstractBaseScreen {
    private Button lockButton, portalSize;
    private boolean lock, big;
    private String owner;
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
        owner = stack.getOrDefault(PGDataComponents.OWNER, "");

        this.addRenderableWidget(Button.builder(
                Component.translatable("ricksportalgun.button.settings.done"), (button) -> {
                    SBSettingsPacket packet = new SBSettingsPacket(lock, owner);
                    Helper.sendPacketToServer(packet);
                    this.onClose();

        }).pos(this.width / 2 - 136, this.height / 2 + 32).size(128, 20).build());
        this.addRenderableWidget(Button.builder(Component.translatable("ricksportalgun.button.cancel"), (button) -> this.onClose())
                .pos(this.width / 2 + 8, this.height / 2 + 32).size(128, 20).build());

        this.lockButton = this.addRenderableWidget(Button.builder(Component.translatable(lock ? "ricksportalgun.button.true" : "ricksportalgun.button.false"), (button) -> {
            lock = !lock;
            lockButton.setMessage(Component.translatable(lock ? "ricksportalgun.button.true" : "ricksportalgun.button.false"));
        }).size(64, 20).pos(this.width / 2 + 72, this.height / 2 - 74).build());

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
        GuiHelper.renderWidgets(pPoseStack, pMouseX, pMouseY, pPartialTick, lockButton, playerInput);

        Style style = GuiHelper.getStyle(pMouseX, pMouseY);
        if (style != null && style.getHoverEvent() != null) {
            this.renderWithTooltip(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }
}