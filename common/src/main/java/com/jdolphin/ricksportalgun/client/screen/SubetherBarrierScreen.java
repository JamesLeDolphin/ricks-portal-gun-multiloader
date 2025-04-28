package com.jdolphin.ricksportalgun.client.screen;

import com.jdolphin.ricksportalgun.common.packet.SBColourPacket;
import com.jdolphin.ricksportalgun.common.packet.SBSetBarrierCodePacket;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class SubetherBarrierScreen extends AbstractBaseScreen {
    private EditBox codeBox;
    private BlockPos pos;
    public SubetherBarrierScreen(BlockPos pos) {
        super("menu.ricksportalgun.sub_ether_barrier");
        this.pos = pos;
    }

    public void init() {
        super.init();
        this.codeBox = this.addWidget(new EditBox(this.font, this.width / 2 - 64, this.height / 2 - 40,
                128, 20, Component.translatable("chat.editBox")));

        this.addRenderableWidget(Button.builder(Component.translatable("ricksportalgun.button.select"), (button) -> {
            this.setCode();
            this.onClose();

        }).pos(this.width / 2 - 128, this.height / 2 + 32).size(128, 20).build());

        this.addRenderableWidget(Button.builder(Component.translatable("ricksportalgun.button.cancel"), (button) -> this.onClose())
                .size(128, 20).pos(this.width / 2 + 8, this.height / 2 + 32).build());
    }

    public void render(@NotNull GuiGraphics stack, int pMouseX, int pMouseY, float pPartialTick) {
        GuiHelper.drawWhiteCenteredString(stack, Component.translatable("menu.ricksportalgun.sub_ether_barrier"), this.width / 2, 30);
        GuiHelper.drawWhiteCenteredString(stack, Component.translatable("ricksportalgun.barrier_code", ""), this.width / 2, this.codeBox.getY() - 16);
        this.codeBox.render(stack, pMouseX, pMouseY, pPartialTick);

        Style style = GuiHelper.getStyle(pMouseX, pMouseY);
        if (style != null && style.getHoverEvent() != null) {
            this.renderWithTooltip(stack, pMouseX, pMouseY, pPartialTick);
        }
        super.render(stack, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        switch (pKeyCode) {
            case GLFW.GLFW_KEY_ENTER:
            case GLFW.GLFW_KEY_KP_ENTER:
                setCode();
                break;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    protected void setCode() {
        String code = this.codeBox.getValue();
        SBSetBarrierCodePacket packet = new SBSetBarrierCodePacket(code, this.pos);
        PGHelper.sendPacketToServer(packet);
        this.onClose();
    }
}
