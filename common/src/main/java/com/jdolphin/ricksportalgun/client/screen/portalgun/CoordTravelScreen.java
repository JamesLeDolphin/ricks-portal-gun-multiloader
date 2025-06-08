package com.jdolphin.ricksportalgun.client.screen.portalgun;

import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.PGImageButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGTextButton;
import com.jdolphin.ricksportalgun.client.screen.widget.SuggestionTextFieldWidget;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBSetDestinationPacket;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Optional;

public class CoordTravelScreen extends AbstractBaseScreen {
    private PGImageButton waypoints, randomise, player_loc, colour, settings;
    private String dS, xS, yS, zS; //Suggestions
    private EditBox xInput, yInput, zInput;
    private SuggestionTextFieldWidget dimInput;
    private final List<String> dimSuggestions;
    private PGTextButton select, cancel;

    public static ResourceLocation WAYPOINT_TEXTURES = PGHelper.createLocation("icon/waypoint");
    public static ResourceLocation PLAYER_LOC_TEXTURES = PGHelper.createLocation("icon/player_locating");
    public static ResourceLocation RANDOMIZER_TEXTURES = PGHelper.createLocation("icon/randomizer");
    public static ResourceLocation COLOR_TEXTURES = PGHelper.createLocation("icon/color_selection");
    public static ResourceLocation SETTINGS_TEXTURES = PGHelper.createLocation("icon/settings");


    public CoordTravelScreen(List<String> suggestions) {
        super("menu.ricksportalgun.coord");
        this.dimSuggestions = suggestions;
    }

    @Override
    protected void init() {
        super.init();

        this.dimInput = this.addWidget(new SuggestionTextFieldWidget(this,
                this.width / 2 - 32, this.height / 2 - 64, 112, 16,
                Component.translatable("chat.editBox"), dimSuggestions));
        this.addWidget(this.dimInput.getSuggestionList());

        this.xInput = this.addWidget(new EditBox(this.font,
                this.width / 2 - 32, this.height / 2 - 40, 64, 16,
                Component.translatable("chat.editBox")));
        this.yInput = this.addWidget(new EditBox(this.font,
                this.width / 2 - 32, this.height / 2 - 16, 64, 16,
                Component.translatable("chat.editBox")));
        this.zInput = this.addWidget(new EditBox(this.font,
                this.width / 2 - 32, this.height / 2 + 8, 64, 16,
                Component.translatable("chat.editBox")));

        this.select = this.addRenderableWidget(new PGTextButton(this.width / 2 - 136, this.height / 2 + 32, 128, 20, Component.translatable("ricksportalgun.button.select"), (button) -> {
            this.setCoords();
            this.onClose();

        }, this.font));
        this.cancel = this.addRenderableWidget(new PGTextButton(this.width / 2 + 8, this.height / 2 + 32, 128, 20,
                Component.translatable("ricksportalgun.button.cancel"), (button) -> this.onClose(), this.font));

        this.waypoints = this.addRenderableWidget(new PGImageButton(this.width / 2 - 90, this.height / 2 + 64, 20, 18, Component.translatable("ricksportalgun.button.waypoint"),
                button -> this.minecraft.setScreen(new WaypointScreen()), 20, 18, WAYPOINT_TEXTURES));


        this.player_loc = this.addRenderableWidget(new PGImageButton(this.width / 2 - 64, this.height / 2 + 64, 20, 18, Component.translatable("ricksportalgun.button.player_locator"),
                (button) -> this.minecraft.setScreen(new PlayerLocatorScreen()), 20, 18, PLAYER_LOC_TEXTURES));

        this.randomise = this.addRenderableWidget(new PGImageButton(this.width / 2 - 38, this.height / 2 + 64, 20, 18, Component.translatable("ricksportalgun.button.randomise"),
                (button) -> {
                    Player player = minecraft.player;
                    ItemStack itemStack = player.getMainHandItem();
                    if (itemStack.is(PGTags.Items.PORTAL_GUNS)) {
                        Level level = player.level();
                        String dim = this.dimInput.getValue().isEmpty() ? level.dimension().location().toString() : this.dimInput.getValue();
                        SBSetDestinationPacket packet = new SBSetDestinationPacket(player.blockPosition(), dim);
                        PGHelper.sendPacketToServer(packet);
                        this.onClose();
                    }
                }, 20, 18, RANDOMIZER_TEXTURES));

        this.colour = this.addRenderableWidget(new PGImageButton(this.width / 2 - 12, this.height / 2 + 64, 20, 18,
                Component.translatable("ricksportalgun.button.colour"), (button) -> this.minecraft.setScreen(new ColourPickingScreen()), 20, 18, COLOR_TEXTURES));

        this.settings = this.addRenderableWidget(new PGImageButton(this.width / 2 + 14, this.height / 2 + 64, 20, 18, Component.translatable("ricksportalgun.button.settings"),
                (button) -> this.minecraft.setScreen(new SettingsScreen()), 20, 18, SETTINGS_TEXTURES));

        this.dimInput.setMaxLength(256);
        this.dimInput.getSuggestionList().setBorderColor(ARGB.color(255, 0, 0));
        LocalPlayer player = minecraft.player;
        if (player != null) {
            ResourceLocation location = player.level().dimension().location();
            this.dS = location.getNamespace().equals("minecraft") ?
                    location.getPath() : location.toString();
            this.dimInput.setSuggestion(dS);

            this.xS = String.valueOf(((int) player.getX()));
            this.yS = String.valueOf(((int) player.getY()));
            this.zS = String.valueOf(((int) player.getZ()));

            this.xInput.setSuggestion(xS);
            this.yInput.setSuggestion(yS);
            this.zInput.setSuggestion(zS);
        }
        this.dimInput.setResponder(this::onEdited);
        this.xInput.setResponder(this::onEdited);
        this.yInput.setResponder(this::onEdited);
        this.zInput.setResponder(this::onEdited);
    }

    @Override
    public void tick() {
        super.tick();
        assert this.minecraft != null;
        LocalPlayer player = this.minecraft.player;
        if (player != null) {
            if (!dimInput.isFocused() && dimInput.getValue().isEmpty())
                dimInput.setSuggestion(player.clientLevel.dimension().location().toString());
            else dimInput.setSuggestion("");
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Optional<GuiEventListener> optional = this.getChildAt(mouseX, mouseY);
        if (optional.isEmpty()) {
            dimInput.setFocused(false);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float delta) {
        graphics.fill(this.width / 2 - 154, this.height / 2 - 110, this.width / 2 + 165, this.height / 2 + 100, BG_RED);

        graphics.drawCenteredString(this.font, Component.translatable("menu.ricksportalgun.coord"), this.width / 2, this.height / 4 - 32, TEXT_RED);

        graphics.drawString(this.font, Component.translatable("ricksportalgun.x", ""), this.width / 2 - 88, this.xInput.getY(), TEXT_RED);
        graphics.drawString(this.font, Component.translatable("ricksportalgun.y", ""), this.width / 2 - 88, this.yInput.getY(), TEXT_RED);
        graphics.drawString(this.font, Component.translatable("ricksportalgun.z", ""), this.width / 2 - 88, this.zInput.getY(), TEXT_RED);
        graphics.drawString(this.font, Component.translatable("ricksportalgun.dimension", ""), this.width / 2 - 88, this.dimInput.getY(), TEXT_RED);

        GuiHelper.renderWidgets(graphics, pMouseX, pMouseY, delta, xInput, yInput, zInput);
        dimInput.render(graphics, pMouseX, pMouseY, delta);

        graphics.renderOutline(dimInput.getX(), dimInput.getY(), dimInput.getWidth(), dimInput.getHeight(), TEXT_RED);
        graphics.renderOutline(xInput.getX(), xInput.getY(), xInput.getWidth(), xInput.getHeight(), TEXT_RED);
        graphics.renderOutline(yInput.getX(), yInput.getY(), yInput.getWidth(), yInput.getHeight(), TEXT_RED);
        graphics.renderOutline(zInput.getX(), zInput.getY(), zInput.getWidth(), zInput.getHeight(), TEXT_RED);

        graphics.renderOutline(select.getX(), select.getY(), select.getWidth(), select.getHeight(), select.isHoveredOrFocused() ? HIGHLIGHT_RED : TEXT_RED);
        graphics.renderOutline(cancel.getX(), cancel.getY(), cancel.getWidth(), cancel.getHeight(), cancel.isHoveredOrFocused() ? HIGHLIGHT_RED : TEXT_RED);

        GuiHelper.setTooltip(waypoints, Tooltip.create(Component.translatable("ricksportalgun.button.waypoint")));
        GuiHelper.setTooltip(player_loc, Tooltip.create(Component.translatable("ricksportalgun.button.player_locator")));
        GuiHelper.setTooltip(randomise, Tooltip.create(Component.translatable("ricksportalgun.button.randomise")));
        GuiHelper.setTooltip(colour, Tooltip.create(Component.translatable("ricksportalgun.button.colour")));
        GuiHelper.setTooltip(settings, Tooltip.create(Component.translatable("ricksportalgun.button.settings")));



        Style style = GuiHelper.getStyle(pMouseX, pMouseY);
        if (style != null && style.getHoverEvent() != null) {
            this.renderWithTooltip(graphics, pMouseX, pMouseY, delta);
        }
        graphics.blit(RenderType::guiTextured, BG_LOCATION, this.width / 2 - 158, this.height / 2 - 115, 0, 0, 330, 224, 330, 224);
        super.render(graphics, pMouseX, pMouseY, delta);
    }

    private void onEdited(String string) {
        String xValue = xInput.getValue();
        String yValue = yInput.getValue();
        String zValue = zInput.getValue();

        this.dimInput.setSuggestion("");
        dimInput.update();

        if (this.xS.startsWith(xInput.getValue())) {
            this.xInput.setSuggestion(this.xS.substring(xValue.length()));
        } else this.xInput.setSuggestion("");

        if (this.yS.startsWith(yInput.getValue())) {
            this.yInput.setSuggestion(this.yS.substring(yValue.length()));
        } else this.yInput.setSuggestion("");

        if (this.zS.startsWith(zInput.getValue())) {
            this.zInput.setSuggestion(this.zS.substring(zValue.length()));
        } else this.zInput.setSuggestion("");

        int color = ARGB.color(199, 12, 6);
        xInput.setTextColor(color);
        yInput.setTextColor(color);
        zInput.setTextColor(color);
        dimInput.setTextColor(color);

    }

    public void setCoords() {
        try {
            assert minecraft != null && minecraft.player != null;
            LocalPlayer player = minecraft.player;
            String value = dimInput.getValue();
            if (value.equals("end")) dimInput.setValue("the_end");
            if (value.equals("nether")) dimInput.setValue("the_nether");
            ResourceLocation resourceLocation =
                    ResourceLocation.parse(value.isEmpty() ? LevelHelper.getPlayerDimensionLocation(player).toString() : value);

            SBSetDestinationPacket packet = new SBSetDestinationPacket(getCoords(player), resourceLocation.toString());
            PGHelper.sendPacketToServer(packet);
            this.onClose();
        } catch (Exception error) {
            dimInput.setSuggestion(" §c" + error.getLocalizedMessage());
        }
    }

    private int getInt(EditBox box, int fallback) {
        return box.getValue().isEmpty() ? fallback : Integer.parseInt(box.getValue());
    }

    private @NotNull BlockPos getCoords(LocalPlayer player) {
        int x = getInt(xInput, (int) player.getX());
        int y = getInt(yInput, (int) player.getY());
        int z = getInt(zInput, (int) player.getZ());
        return new BlockPos(x, y, z);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        switch (pKeyCode) {
            case GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER:
                if(this.getFocused() instanceof Button)
                    return super.keyPressed(pKeyCode, pScanCode, pModifiers);
                this.setCoords();
                break;
            case GLFW.GLFW_KEY_TAB:
                if (dimInput.isFocused()) dimInput.setValue(dS);
                if (xInput.isFocused()) xInput.setValue(xS);
                if (yInput.isFocused()) yInput.setValue(yS);
                if (zInput.isFocused()) zInput.setValue(zS);

                break;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
}