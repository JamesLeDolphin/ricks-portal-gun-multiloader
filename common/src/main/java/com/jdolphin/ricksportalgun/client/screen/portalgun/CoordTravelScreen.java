package com.jdolphin.ricksportalgun.client.screen.portalgun;

import com.jdolphin.ricksportalgun.client.screen.AbstractBaseScreen;
import com.jdolphin.ricksportalgun.client.screen.widget.PGImageButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGTextButton;
import com.jdolphin.ricksportalgun.client.screen.widget.SuggestionTextFieldWidget;
import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.init.PGUpgradeTypes;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBActivateSelfDestructPacket;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBCoordCheckerPacket;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBOpenLocatorScreenPacket;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBSetDestinationPacket;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Optional;

public class CoordTravelScreen extends AbstractBaseScreen {
    private PGImageButton waypoints, randomiseDim, randomiseCoord, locator, settings, selfDestruct;
    private String dS, xS, yS, zS; //Suggestions
    private EditBox xInput, yInput, zInput;
    private SuggestionTextFieldWidget dimInput;
    private final List<String> dimSuggestions;
    private PGTextButton select, cancel;

    public static ResourceLocation WAYPOINT_TEXTURE = PGHelper.id("textures/gui/sprites/icon/waypoint.png");
    public static ResourceLocation PLAYER_LOC_TEXTURE = PGHelper.id("textures/gui/sprites/icon/locator.png");
    public static ResourceLocation RANDOMIZER_TEXTURE = PGHelper.id("textures/gui/sprites/icon/randomizer.png");
    public static ResourceLocation SETTINGS_TEXTURE = PGHelper.id("textures/gui/sprites/icon/settings.png");
    public static ResourceLocation SELF_DESTRUCT_TEXTURE = PGHelper.id("textures/gui/sprites/icon/self_destruct.png");


    public CoordTravelScreen(List<String> suggestions) {
        super("menu.ricksportalgun.coord");
        this.dimSuggestions = suggestions;
    }

    @Override
    protected void init() {
        super.init();
        assert this.minecraft != null;
        LocalPlayer player = minecraft.player;
        assert player != null;
        ItemStack stack = getItemStack();

        this.dimInput = this.addWidget(new SuggestionTextFieldWidget(this.width / 2 - 32, this.height / 2 - 64, 112, 16,
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

        this.randomiseDim = this.addRenderableWidget(new PGImageButton(this.dimInput.getX() + this.dimInput.getWidth() + 5, this.dimInput.getY() - 1, 20, 18,
                Component.translatable("ricksportalgun.button.randomise.dimension"),
                (button) -> {
                    List<String> strings = this.dimInput.getSuggestions();
                    String s = PGHelper.getRandomFromList(strings);
                    this.dimInput.setValue(s);
                }, 20, 18, RANDOMIZER_TEXTURE));

        this.randomiseCoord = this.addRenderableWidget(new PGImageButton(this.randomiseDim.getX(), this.yInput.getY(), 20, 18, Component.translatable("ricksportalgun.button.randomise.coord"),
                (button) -> {
                    if (stack.is(PGTags.Items.PORTAL_GUNS)) {
                        Level level = player.level();
                        String dim = this.dimInput.getValue().isEmpty() ? LevelHelper.getLevelDimensionLocation(level).toString() : this.dimInput.getValue();
                        SBCoordCheckerPacket packet = new SBCoordCheckerPacket(dim);
                        PGHelper.sendPacketToServer(packet);
                    }
                    this.onClose();
                }, 20, 18, RANDOMIZER_TEXTURE));

        this.selfDestruct = this.addRenderableWidget(new PGImageButton(this.yInput.getX() - 96, this.yInput.getY() - 2, 20, 20,
                Component.translatable("ricksportalgun.button.self_destruct.activate"),
                (button) -> {
                    if (stack.is(PGTags.Items.PORTAL_GUNS)) {
                        SBActivateSelfDestructPacket packet = new SBActivateSelfDestructPacket();
                        PGHelper.sendPacketToServer(packet);

                    }
                    this.onClose();
                }, 20, 18, SELF_DESTRUCT_TEXTURE));
        selfDestruct.active = stack.getOrDefault(PGDataComponents.SELF_DESTRUCT, false);

        this.waypoints = this.addRenderableWidget(new PGImageButton(this.width / 2 - 36, this.height / 2 + 32, 20, 18, Component.translatable("ricksportalgun.button.waypoint"),
                button -> this.minecraft.setScreen(new WaypointScreen()), 20, 18, WAYPOINT_TEXTURE));

        this.settings = this.addRenderableWidget(new PGImageButton(this.width / 2 - 10, this.height / 2 + 32, 20, 18, Component.translatable("ricksportalgun.button.settings"),
                (button) -> this.minecraft.setScreen(new SettingsScreen()), 20, 18, SETTINGS_TEXTURE));

        this.locator = this.addRenderableWidget(new PGImageButton(this.width / 2 + 16, this.height / 2 + 32, 20, 18, Component.translatable("ricksportalgun.button.locator"),
                (button) -> {
                    SBOpenLocatorScreenPacket packet = new SBOpenLocatorScreenPacket();
                    PGHelper.sendPacketToServer(packet);

                }, 20, 18, PLAYER_LOC_TEXTURE));


        this.select = this.addRenderableWidget(new PGTextButton(this.width / 2 - 136, this.height / 2 + 64, 128, 20, Component.translatable("ricksportalgun.button.select"), (button) -> {
            this.setCoords();
            this.onClose();
        }, this.font));

        this.cancel = this.addRenderableWidget(new PGTextButton(this.width / 2 + 8, this.height / 2 + 64, 128, 20,
                Component.translatable("ricksportalgun.button.cancel"), (button) -> this.onClose(), this.font));

        if (!PortalGunItem.getUpgrades(stack).contains(PGUpgradeTypes.DIM_1.getId())) {
            this.randomiseDim.active = false;
            this.dimInput.setEditable(false);
            this.dimInput.getSuggestionList().active = false;
        }
        if (!PortalGunItem.getUpgrades(stack).contains(PGUpgradeTypes.WAYPOINTS.getId())) {
            this.waypoints.active = false;
        }
        if (!PortalGunItem.getUpgrades(stack).contains(PGUpgradeTypes.BIOME_LOC.getId())) {
            this.locator.active = false;
        }

        PortalGunStyle style = getStyle();

        this.dimInput.getSuggestionList().setBorderColor(style.highlightColor());
        setupEditBox(dimInput, style);
        setupEditBox(xInput, style);
        setupEditBox(yInput, style);
        setupEditBox(zInput, style);

        setupImgButtons(settings, style);
        setupImgButtons(waypoints, style);
        setupImgButtons(locator, style);
        setupImgButtons(randomiseCoord, style);
        setupImgButtons(randomiseDim, style);
        setupImgButtons(selfDestruct, style);

        ResourceLocation location = LevelHelper.getPlayerDimensionLocation(player);
        this.dS = location.getNamespace().equals("minecraft") ?
                location.getPath() : location.toString();
        this.dimInput.setSuggestion(dS);

        BlockPos dest = stack.getOrDefault(PGDataComponents.PORTAL_POS, player.blockPosition());
        this.xS = String.valueOf(dest.getX());
        this.yS = String.valueOf(dest.getY());
        this.zS = String.valueOf(dest.getZ());

        this.xInput.setSuggestion(xS);
        this.yInput.setSuggestion(yS);
        this.zInput.setSuggestion(zS);
        GuiHelper.setTooltip(randomiseCoord, Component.translatable("ricksportalgun.button.randomise.coord"));
    }

    private void setupImgButtons(PGImageButton button, PortalGunStyle style) {
        button.setColor(style.highlightColor());
        button.setRenderBackground(false);
    }

    private void setupEditBox(EditBox box, PortalGunStyle style) {
        box.setResponder(this::onEdited);
        box.setMaxLength(256);
        box.setTextColor(style.textColor());
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
        PortalGunStyle style = getStyle();

        graphics.fill(this.width / 2 - 154, this.height / 2 - 110, this.width / 2 + 165, this.height / 2 + 100, style.bgColor());

        graphics.drawCenteredString(this.font, Component.translatable("menu.ricksportalgun.coord"), this.width / 2, this.height / 2 - 92, style.textColor());

        graphics.drawString(this.font, Component.translatable("ricksportalgun.x", ""), this.width / 2 - 88, this.xInput.getY() + 3, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.y", ""), this.width / 2 - 88, this.yInput.getY() + 3, style.textColor());
        graphics.drawString(this.font, Component.translatable("ricksportalgun.z", ""), this.width / 2 - 88, this.zInput.getY() + 3, style.textColor());

        GuiHelper.renderWidgets(graphics, pMouseX, pMouseY, delta, xInput, yInput, zInput);


        GuiHelper.renderOutline(graphics, xInput, style.highlightColor());
        GuiHelper.renderOutline(graphics, yInput, style.highlightColor());
        GuiHelper.renderOutline(graphics, zInput, style.highlightColor());


        GuiHelper.renderOutline(graphics, randomiseCoord, style.highlightColor());
        GuiHelper.renderOutline(graphics, select, style.highlightColor());
        GuiHelper.renderOutline(graphics, cancel, style.highlightColor());

        ItemStack stack = getItemStack();

        if (stack.getOrDefault(PGDataComponents.SELF_DESTRUCT, false)) {
            GuiHelper.renderOutline(graphics, selfDestruct, style.highlightColor());
            selfDestruct.render(graphics, pMouseX, pMouseY, delta);
            GuiHelper.setTooltip(selfDestruct, Component.translatable("ricksportalgun.button.self_destruct.activate"));
        }

        if (PortalGunItem.getUpgrades(stack).contains(PGUpgradeTypes.DIM_1.getId())) {
            dimInput.render(graphics, pMouseX, pMouseY, delta);
            randomiseDim.render(graphics, pMouseX, pMouseY, delta);
            graphics.drawString(this.font, Component.translatable("ricksportalgun.dimension", ""), this.width / 2 - 88, this.dimInput.getY() + 3, style.textColor());
            GuiHelper.setTooltip(randomiseDim, Component.translatable("ricksportalgun.button.randomise.dimension"));
            GuiHelper.renderOutline(graphics, randomiseDim, style.highlightColor());
            GuiHelper.renderOutline(graphics, dimInput, style.highlightColor());
        }
        if (PortalGunItem.getUpgrades(stack).contains(PGUpgradeTypes.WAYPOINTS.getId())) {
            waypoints.render(graphics, pMouseX, pMouseY, delta);
            GuiHelper.renderOutline(graphics, waypoints, style.highlightColor());
            GuiHelper.setTooltip(waypoints, Component.translatable("ricksportalgun.button.waypoint"));
        }
        settings.render(graphics, pMouseX, pMouseY, delta);
        GuiHelper.renderOutline(graphics, settings, style.highlightColor());
        GuiHelper.setTooltip(settings, Component.translatable("ricksportalgun.button.settings"));

        if (PortalGunItem.getUpgrades(stack).contains(PGUpgradeTypes.BIOME_LOC.getId())) {
            locator.render(graphics, pMouseX, pMouseY, delta);
            GuiHelper.renderOutline(graphics, locator, style.highlightColor());
            GuiHelper.setTooltip(locator, Component.translatable("ricksportalgun.button.locator"));
        }
        RenderSystem.enableBlend();
        graphics.blit(BG_LOCATION, this.width / 2 - 158, this.height / 2 - 115, 0, 0, 330, 224, 330, 224);
        RenderSystem.disableBlend();

        Style guiStyle = GuiHelper.getStyle(pMouseX, pMouseY);
        if (guiStyle != null && guiStyle.getHoverEvent() != null) {
            this.renderWithTooltip(graphics, pMouseX, pMouseY, delta);
        }

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