package com.jdolphin.ricksportalgun.client.screen;

import com.jdolphin.ricksportalgun.client.screen.widget.PGScrollableWidget;
import com.jdolphin.ricksportalgun.client.screen.widget.SuggestionTextFieldWidget;
import com.jdolphin.ricksportalgun.common.menu.PortalDispenserMenu;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBSetDispenserDestinationPacket;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class PortalDispenserScreen extends AbstractContainerScreen<PortalDispenserMenu> {
    public static final ResourceLocation CONTAINER_LOCATION = PGHelper.id("textures/gui/dispenser/portal_dispenser.png");
    private EditBox xInput, yInput, zInput;
    private Button selectButton;
    private SuggestionTextFieldWidget dimInput;
    private final List<String> dimSuggestions;

    public PortalDispenserScreen(PortalDispenserMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.dimSuggestions = LevelHelper.CLIENT_DIMENSIONS;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Optional<GuiEventListener> optional = this.getChildAt(mouseX, mouseY);
        if (optional.isEmpty()) {
            dimInput.setFocused(false);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void onEdited(String string) {
        this.dimInput.setSuggestion("");
        dimInput.update();

        xInput.setTextColor(0xffffff);
        yInput.setTextColor(0xffffff);
        zInput.setTextColor(0xffffff);
        dimInput.setTextColor(0xffffff);
    }

    public boolean mouseScrolled(double d, double d1, double d2, double d3) {
        Optional<GuiEventListener> optional = this.getChildAt(d, d1);
        if (optional.isPresent()) {
            if (optional.get() instanceof PGScrollableWidget<?> list) {
                return list.mouseScrolled(d, d1, d2, d3);
            }
        }
        return super.mouseScrolled(d, d1, d2, d3);
    }

    protected void renderTooltip(GuiGraphics graphics, int x, int y) {
        Optional<Component> optional = Optional.empty();
        if (this.hoveredSlot != null) {
            if (!hoveredSlot.hasItem()) {
                if (hoveredSlot.index == 36) {
                    optional = Optional.of(Component.translatable("notice.ricksportalgun.workbench.insert_portal_fluid"));
                }
                optional.ifPresent((component) -> graphics.renderTooltip(this.font, this.font.split(component, 115), x, y));
            }
        }
        super.renderTooltip(graphics, x, y);
    }

    protected void init() {
        super.init();
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;

        this.xInput = this.addWidget(new EditBox(this.font,
                this.width / 2 - 32,this.height / 2 - 64, 32, 12,
                Component.translatable("chat.editBox")));
        this.yInput = this.addWidget(new EditBox(this.font,
                this.width / 2 + 8, this.height / 2 - 64, 32, 12,
                Component.translatable("chat.editBox")));
        this.zInput = this.addWidget(new EditBox(this.font,
                this.width / 2 + 48, this.height / 2 - 64, 32, 12,
                Component.translatable("chat.editBox")));

        this.dimInput = new SuggestionTextFieldWidget(this.width / 2 - 32,this.height / 2 - 48, 112, 12, Component.translatable("chat.editBox"), dimSuggestions);
        dimInput.update();
        setupSuggestionBox(dimInput);
        this.addWidget(dimInput.getSuggestionList());

        this.selectButton = this.addWidget(Button.builder(Component.translatable("ricksportalgun.button.select"), (button) -> {
            try {
                int x = Integer.parseInt(this.xInput.getValue());
                int y = Integer.parseInt(this.yInput.getValue());
                int z = Integer.parseInt(this.zInput.getValue());
                String dim = dimInput.getValue();
                BlockPos pos = new BlockPos(x, y, z);
                SBSetDispenserDestinationPacket packet = new SBSetDispenserDestinationPacket(pos, dim);
                PGHelper.sendPacketToServer(packet);

            } catch (Exception e) {
                dimInput.setSuggestion(" §c" + e.getLocalizedMessage());
            }
            this.onClose();

        }).pos(this.width / 2 - 32, this.height / 2 - 32).size(112, 16).build());
    }

    private void setupSuggestionBox(EditBox box) {
        box.setCanLoseFocus(true);
        box.setTextColor(-1);
        box.setTextColorUneditable(-1);
        box.setBordered(true);
        box.setMaxLength(50);
        box.setValue("");
        box.setResponder(this::onEdited);
        this.addWidget(box);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.minecraft.player.closeContainer();
        }
        return this.dimInput.keyPressed(keyCode, scanCode, modifiers) || this.dimInput.canConsumeInput() || super.keyPressed(keyCode, scanCode, modifiers);
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        this.renderTooltip(graphics, mouseX, mouseY);
        this.dimInput.render(graphics, mouseX, mouseY, delta);
        this.xInput.render(graphics, mouseX, mouseY, delta);
        this.yInput.render(graphics, mouseX, mouseY, delta);
        this.zInput.render(graphics, mouseX, mouseY, delta);
        if (!this.dimInput.isFocused()) this.selectButton.render(graphics, mouseX, mouseY, delta);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        int maxFuel = Math.max(menu.getMaxFuel(), 1); //Prevent dividing by zero
        int percentage = (52 * menu.getFuel()) / maxFuel;
        int minX = x + 8;
        int minY = y + 68;
        int maxX = x + 23;
        graphics.fill(minX, minY, maxX, minY - percentage, Color.GREEN.getRGB());
        if ((mouseX >= minX && mouseX <= maxX) && (mouseY >= minY - 4 * maxFuel + 12 && mouseY <= minY)) {
            graphics.renderTooltip(this.font, Component.translatable("menu.ricksportalgun.portal_dispenser.fuel", menu.getFuel(), maxFuel), mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float v, int i, int i1) {
        int i2 = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        graphics.blit(CONTAINER_LOCATION, i2, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }
}
