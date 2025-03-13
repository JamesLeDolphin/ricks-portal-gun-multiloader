package com.jdolphin.ricksportalgun.client.screen;

import com.jdolphin.ricksportalgun.client.screen.widget.ScrollableList;
import com.jdolphin.ricksportalgun.client.screen.widget.SuggestionTextFieldWidget;
import com.jdolphin.ricksportalgun.common.menu.PortalDispenserMenu;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.ItemSlotMouseAction;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class PortalDispenserScreen extends AbstractContainerScreen<PortalDispenserMenu> implements IScreenBase {
    public static final ResourceLocation CONTAINER_LOCATION = PGHelper.createLocation("textures/gui/container/portal_dispenser.png");
    private EditBox xInput, yInput, zInput;
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
            if (optional.get() instanceof ScrollableList<?> list) {
                return list.mouseScrolled(d, d1, d2, d3);
            }
        }
        return super.mouseScrolled(d, d1, d2, d3);
    }

    protected void init() {
        super.init();
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        System.out.println(dimSuggestions);
        this.dimInput = new SuggestionTextFieldWidget(this, this.width / 2 - 32, this.height / 2 - 64, 112, 12, Component.translatable("chat.editBox"), dimSuggestions);
        this.dimInput.setCanLoseFocus(true);
        this.dimInput.setTextColor(-1);
        this.dimInput.update();
        this.dimInput.setTextColorUneditable(-1);
        this.dimInput.setBordered(true);
        this.dimInput.setMaxLength(50);
        this.dimInput.setValue("");
        this.dimInput.setResponder(this::onEdited);
        this.addWidget(this.dimInput);

        this.xInput = this.addWidget(new EditBox(this.font,
                this.width / 2 - 32, this.height / 2 - 16, 64, 12,
                Component.translatable("chat.editBox")));
        this.yInput = this.addWidget(new EditBox(this.font,
                this.width / 2 - 32, this.height / 2, 64, 12,
                Component.translatable("chat.editBox")));
        this.zInput = this.addWidget(new EditBox(this.font,
                this.width / 2 - 32, this.height / 2 + 16, 64, 12,
                Component.translatable("chat.editBox")));
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

        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        int maxFuel = Math.max(menu.getMaxFuel(), 1); //Prevent dividing by zero
        int percentage = (52 * menu.getFuel()) / maxFuel;
        graphics.fill(i + 8, j + 68, i + 23, j + 68 - percentage, menu.getColor());
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float v, int i, int i1) {
        int i2 = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderType::guiTextured, CONTAINER_LOCATION, i2, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    public <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget) {
        return super.addRenderableWidget(widget);
    }
}
