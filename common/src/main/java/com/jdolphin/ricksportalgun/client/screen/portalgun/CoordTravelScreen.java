package com.jdolphin.ricksportalgun.client.screen.portalgun;

import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.BetterImageButton;
import com.jdolphin.ricksportalgun.client.screen.widget.SuggestionTextFieldWidget;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.packet.SBSetDestinationPacket;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Optional;

public class CoordTravelScreen extends AbstractBaseScreen {
    private BetterImageButton waypoints, randomise, player_loc, colour, settings;
    private String dS, xS, yS, zS; //Suggestions
    private EditBox xInput, yInput, zInput;
    private SuggestionTextFieldWidget dimInput;
    private final List<String> dimSuggestions;

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
                this.width / 2 - 32, this.height / 2 - 32, 112, 12,
                Component.translatable("chat.editBox"), dimSuggestions));
        this.xInput = this.addWidget(new EditBox(this.font,
                this.width / 2 - 32, this.height / 2 - 16, 64, 12,
                Component.translatable("chat.editBox")));
        this.yInput = this.addWidget(new EditBox(this.font,
                this.width / 2 - 32, this.height / 2, 64, 12,
                Component.translatable("chat.editBox")));
        this.zInput = this.addWidget(new EditBox(this.font,
                this.width / 2 - 32, this.height / 2 + 16, 64, 12,
                Component.translatable("chat.editBox")));

        this.addRenderableWidget(Button.builder(Component.translatable("ricksportalgun.button.select"), (button) -> {
            this.setCoords();
            this.onClose();

        }).pos(this.width / 2 - 136, this.height / 2 + 32).size(128, 20).build());
        this.addRenderableWidget(Button.builder(Component.translatable("ricksportalgun.button.cancel"), (button) -> {
            this.onClose();
        }).size(128, 20).pos(this.width / 2 + 8, this.height / 2 + 32).build());

        this.waypoints = this.addRenderableWidget(new BetterImageButton(this.width / 2 - 90, this.height / 2 + 64, 20, 18, Component.translatable("ricksportalgun.button.waypoint"),
                button -> this.minecraft.setScreen(new WaypointScreen()), 20, 18, WAYPOINT_TEXTURES));


        this.player_loc = this.addRenderableWidget(new BetterImageButton(this.width / 2 - 64, this.height / 2 + 64, 20, 18, Component.translatable("ricksportalgun.button.player_locator"),
                (button) -> this.minecraft.setScreen(new PlayerLocatorScreen()), 20, 18, PLAYER_LOC_TEXTURES));

        this.randomise = this.addRenderableWidget(new BetterImageButton(this.width / 2 - 38, this.height / 2 + 64, 20, 18, Component.translatable("ricksportalgun.button.randomise"),
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

        this.colour = this.addWidget(new BetterImageButton(this.width / 2 - 12, this.height / 2 + 64, 20, 18,
                Component.translatable("ricksportalgun.button.colour"), (button) -> this.minecraft.setScreen(new ColourPickingScreen()), 20, 18, COLOR_TEXTURES));

        this.settings = this.addWidget(new BetterImageButton(this.width / 2 + 14, this.height / 2 + 64, 20, 18, Component.translatable("ricksportalgun.button.settings"),
                (button) -> this.minecraft.setScreen(new SettingsScreen()), 20, 18, SETTINGS_TEXTURES));

        this.dimInput.setMaxLength(256);
        this.dimInput.setBordered(true);
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
    public void render(GuiGraphics pPoseStack, int pMouseX, int pMouseY, float delta) {
        GuiHelper.drawWhiteString(pPoseStack, Component.translatable("ricksportalgun.x", ""), this.width / 2 - 88, this.height / 2 - 16);
        GuiHelper.drawWhiteString(pPoseStack, Component.translatable("ricksportalgun.y", ""), this.width / 2 - 88, this.height / 2);
        GuiHelper.drawWhiteString(pPoseStack, Component.translatable("ricksportalgun.z", ""), this.width / 2 - 88, this.height / 2 + 16);
        GuiHelper.drawWhiteString(pPoseStack, Component.translatable("ricksportalgun.dimension", ""), this.width / 2 - 88, this.height / 2 - 32);

        GuiHelper.renderWidgets(pPoseStack, pMouseX, pMouseY, delta, waypoints, xInput, yInput, zInput, randomise, player_loc, colour, settings);
        dimInput.render(pPoseStack, pMouseX, pMouseY, delta);

        GuiHelper.setTooltip(waypoints, Tooltip.create(Component.translatable("ricksportalgun.button.waypoint")));
        GuiHelper.setTooltip(player_loc, Tooltip.create(Component.translatable("ricksportalgun.button.player_locator")));
        GuiHelper.setTooltip(randomise, Tooltip.create(Component.translatable("ricksportalgun.button.randomise")));
        GuiHelper.setTooltip(colour, Tooltip.create(Component.translatable("ricksportalgun.button.colour")));
        GuiHelper.setTooltip(settings, Tooltip.create(Component.translatable("ricksportalgun.button.settings")));

        Style style = GuiHelper.getStyle(pMouseX, pMouseY);
        if (style != null && style.getHoverEvent() != null) {
            this.renderWithTooltip(pPoseStack, pMouseX, pMouseY, delta);
        }
        super.render(pPoseStack, pMouseX, pMouseY, delta);
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

        xInput.setTextColor(0xffffff);
        yInput.setTextColor(0xffffff);
        zInput.setTextColor(0xffffff);
        dimInput.setTextColor(0xffffff);

    }

    public void setCoords() {
        try {
            assert minecraft != null && minecraft.player != null;
            LocalPlayer player = minecraft.player;
            if (dimInput.getValue().equals("end")) dimInput.setValue("the_end");
            if (dimInput.getValue().equals("nether")) dimInput.setValue("the_nether");
            ResourceLocation resourceLocation =
                    ResourceLocation.parse(dimInput.getValue().isEmpty() ? LevelHelper.getPlayerDimensionLocation(player).toString() : dimInput.getValue());

            SBSetDestinationPacket packet = new SBSetDestinationPacket(getCoords(player), resourceLocation.toString());
            PGHelper.sendPacketToServer(packet);
            this.onClose();
        } catch (Exception error) {
            dimInput.setSuggestion(" §c" + error.getLocalizedMessage());
        }
    }

    private @NotNull BlockPos getCoords(LocalPlayer player) {
        int x = Integer.parseInt(xInput.getValue().isEmpty() ? String.valueOf(((int) player.getX())) : xInput.getValue());
        int y = Integer.parseInt(yInput.getValue().isEmpty() ? String.valueOf(((int) player.getY())) : yInput.getValue());
        int z = Integer.parseInt(zInput.getValue().isEmpty() ? String.valueOf(((int) player.getZ())) : zInput.getValue());

        ItemStack itemStack = player.getMainHandItem();
        if (!itemStack.is(PGTags.Items.PORTAL_GUNS))
            throw new NullPointerException("Portal gun can't be null!");
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