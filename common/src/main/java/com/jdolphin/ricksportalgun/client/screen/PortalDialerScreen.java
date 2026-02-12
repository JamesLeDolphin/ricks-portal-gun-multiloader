package com.jdolphin.ricksportalgun.client.screen;

import com.jdolphin.ricksportalgun.common.block.PortalControllerBlock;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBPortalDialActionPacket;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class PortalDialerScreen extends AbstractBaseScreen {
    private final BlockPos pos;
    private EditBox input;
    private Button activate, disconnect;

    public PortalDialerScreen(BlockPos pos) {
        super(Component.translatable("menu.ricksportalgun.portal_dialer"));
        this.pos = pos;
    }

    @Override
    protected void init() {
        super.init();

        this.input = this.addRenderableWidget(new EditBox(this.font, this.width / 2 - 64, this.height / 2 - 30, 128, 20,
                Component.translatable("chat.editBox")));

        this.activate = this.addRenderableWidget(Button.builder(Component.literal("Activate"), button -> {
            if (!this.input.getValue().isEmpty()) {
                SBPortalDialActionPacket packet = new SBPortalDialActionPacket(pos, input.getValue(), false);
                PGHelper.sendPacketToServer(packet);
                this.onClose();
            }
        }).bounds(this.width / 2 - 64, this.height / 2, 128, 20).build());

        this.disconnect = this.addRenderableWidget(Button.builder(Component.literal("Disconnect"), button -> {
            SBPortalDialActionPacket packet = new SBPortalDialActionPacket(pos, "", true);
            PGHelper.sendPacketToServer(packet);
            this.onClose();
        }).bounds(this.width / 2 - 64, this.height / 2 + 30, 128, 20).build());
    }

    @Override
    public void tick() {
        super.tick();

        assert minecraft != null;
        Level level = minecraft.level;
        BlockState state = level.getBlockState(pos);
        if (state.hasProperty(PortalControllerBlock.ACTIVE)) {
            boolean portalActive = state.getValue(PortalControllerBlock.ACTIVE);
            disconnect.active = portalActive;
            activate.active = !portalActive;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {



        Style style = GuiHelper.getStyle(mouseX, mouseY);
        if (style != null && style.getHoverEvent() != null) {
            this.renderWithTooltip(guiGraphics, mouseX, mouseY, partialTick);
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
