package com.jdolphin.ricksportalgun.client.screen.portalgun;

import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.Slider;
import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.init.PortalGunTypeRegistry;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBChangePortalGunTypePacket;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBSettingsPacket;
import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SettingsScreen extends AbstractBaseScreen {
    private Button lockButton;
    private Slider portalSize;
    private boolean lock;
    private EditBox playerInput;
    public SettingsScreen() {
        super("menu.ricksportalgun.settings");
    }

    @Override
    public void init() {
        super.init();
        LocalPlayer player = minecraft.player;
        ItemStack stack = player.getMainHandItem();

        lock = stack.getOrDefault(PGDataComponents.LOCK, false);
        float size = stack.getOrDefault(PGDataComponents.PORTAL_SIZE, 1.0f);

        this.addRenderableWidget(Button.builder(
                Component.translatable("ricksportalgun.button.select"), (button) -> {
                    SBSettingsPacket packet = new SBSettingsPacket(lock, playerInput.getValue(), (float) this.portalSize.getValue());
                    PGHelper.sendPacketToServer(packet);
                    this.onClose();

        }).pos(this.width / 2 - 136, this.height / 2 + 32).size(128, 20).build());
        this.addRenderableWidget(Button.builder(Component.translatable("ricksportalgun.button.cancel"), (button) -> this.onClose())
                .pos(this.width / 2 + 8, this.height / 2 + 32).size(128, 20).build());

        MutableComponent sTrue = Component.translatable("ricksportalgun.button.true");
        MutableComponent sFalse = Component.translatable("ricksportalgun.button.false");
        MutableComponent component = lock ? sTrue : sFalse;
        this.lockButton = this.addRenderableWidget(Button.builder(component, (button) -> {
            lock = !lock;
            lockButton.setMessage(component);
        }).size(64, 20).pos(this.width / 2 + 70, this.height / 2 - 74).build());

        this.portalSize = this.addRenderableWidget(new Slider(this.width / 2 + 92, this.height / 2 - 28, 42, 18,
                Component.empty(), size, 1.0f, 3.0f, true));

        this.playerInput = this.addWidget(new EditBox(this.font, this.width / 2 + 54, this.height / 2 - 50, 80, 16,
                Component.translatable("chat.editBox")));

        //Temp
        this.addRenderableWidget(Button.builder(Component.literal("Types"), button -> {
            PortalGunType type = PortalGunTypeRegistry.PORTAL_GUN_TYPES.get(PGConstants.RANDOM.nextInt(PortalGunTypeRegistry.PORTAL_GUN_TYPES.size()));
            System.out.println(PortalGunTypeRegistry.PORTAL_GUN_TYPES);
            SBChangePortalGunTypePacket packet = new SBChangePortalGunTypePacket(type);
            PGHelper.sendPacketToServer(packet);
        }).size(20, 20).pos(this.width / 2 + 70, this.height / 2).build());

        this.playerInput.setBordered(true);
        this.playerInput.setMaxLength(256);
    }

    @Override
    public void render(@NotNull GuiGraphics pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        GuiHelper.drawWhiteCenteredString(pPoseStack, Component.translatable("ricksportalgun.button.settings"), this.width / 2, this.height / 10);
        GuiHelper.drawWhiteString(pPoseStack, Component.translatable("ricksportalgun.button.lock"), this.width / 4 - 16, this.lockButton.getY() + 4);
        GuiHelper.drawWhiteString(pPoseStack, Component.translatable("ricksportalgun.button.ownership"), this.width / 4 - 16, this.playerInput.getY() + 4);
        GuiHelper.drawWhiteString(pPoseStack, Component.translatable("ricksportalgun.button.portal_size"), this.width / 4 - 16, this.portalSize.getY() + 4);
        GuiHelper.renderWidgets(pPoseStack, pMouseX, pMouseY, pPartialTick, lockButton, playerInput, portalSize);
        Style style = GuiHelper.getStyle(pMouseX, pMouseY);
        if (style != null && style.getHoverEvent() != null) {
            this.renderWithTooltip(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }
}