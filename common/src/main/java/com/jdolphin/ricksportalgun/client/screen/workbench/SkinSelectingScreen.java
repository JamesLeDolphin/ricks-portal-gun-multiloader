package com.jdolphin.ricksportalgun.client.screen.workbench;

import com.jdolphin.ricksportalgun.client.screen.widget.PGImageButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGItemButton;
import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.init.PortalGunTypeRegistry;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.menu.workbench.SkinSelectorMenu;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBSetPortalGunTypePacket;
import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class SkinSelectingScreen extends AbstractWorkbenchScreen<SkinSelectorMenu> {
    public static final ResourceLocation BG = PGHelper.createLocation("textures/gui/workbench/skin_select.png");
    public static ResourceLocation NEXT = PGHelper.createLocation("textures/gui/sprites/next.png");
    public static ResourceLocation NEXT_HL = PGHelper.createLocation("textures/gui/sprites/next_highlighted.png");
    public static ResourceLocation PREVIOUS = PGHelper.createLocation("textures/gui/sprites/previous.png");
    public static ResourceLocation PREVIOUS_HL = PGHelper.createLocation("textures/gui/sprites/previous_highlighted.png");

    private PGImageButton next, previous;
    private Button select;
    private int index = 0;
    private float rot = 0;
    private double scroll = 0;

    public SkinSelectingScreen(SkinSelectorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 212 + 8;
        this.imageHeight = 192;
        this.inventoryLabelX = this.imageWidth - 188;
        this.inventoryLabelY = this.imageHeight - 93;
        this.titleLabelX = this.imageWidth - 193;
        this.titleLabelY = this.imageHeight - 188;
    }

    public void init() {
        super.init();

        this.next = this.addWidget(new PGImageButton(this.width / 2 + 66, this.height / 2 - 26, 12, 32, Component.translatable("ricksportalgun.button.next"),
                button -> cycleValue(1), 32, 23, NEXT));
        next.setRenderBackground(false);
        this.previous = this.addWidget(new PGImageButton(this.width / 2 + 8, this.height / 2 - 26, 12, 32, Component.translatable("ricksportalgun.button.previous"),
                button -> cycleValue(-1), 32, 23, PREVIOUS));
        previous.setRenderBackground(false);

        this.select = this.addWidget(Button.builder(Component.translatable("ricksportalgun.button.select"), button -> {
                    SBSetPortalGunTypePacket packet = new SBSetPortalGunTypePacket(getType());
                    PGHelper.sendPacketToServer(packet);
                })
                .bounds(this.width / 2 + 16, this.height / 2 - 18, 48, 16).build());

        PGItemButton skin = this.addRenderableWidget(new PGItemButton(this.width / 2 + 87, this.height / 2 - 95, 24, 24,
                Component.translatable("menu.ricksportalgun.workbench.skin"), button -> {
        }, PGItems.PORTAL_GUN.getDefaultInstance()));
        skin.setRenderBackground(false);

        PGItemButton waypoint = this.addRenderableWidget(new PGItemButton(this.width / 2 + 87, this.height / 2 - 70, 24, 24,
                Component.translatable("menu.ricksportalgun.workbench.waypoint"), button -> setScreen(0), PGItems.DATA_CARD.getDefaultInstance()));
        waypoint.setRenderBackground(false);

        PGItemButton craft = this.addRenderableWidget(new PGItemButton(this.width / 2 + 87, this.height / 2 - 45, 24, 24,
                Component.translatable("menu.ricksportalgun.workbench.craft"), button -> setScreen(2), Items.CRAFTING_TABLE.getDefaultInstance()));
        craft.setRenderBackground(false);

        GuiHelper.setTooltip(skin, Component.translatable("menu.ricksportalgun.workbench.skin"));
        GuiHelper.setTooltip(waypoint, Component.translatable("menu.ricksportalgun.workbench.waypoint"));
        GuiHelper.setTooltip(craft, Component.translatable("menu.ricksportalgun.workbench.craft"));
    }

    private ItemStack getStack(int slotId) {
        return this.menu.getSlot(slotId).getItem();
    }

    private PortalGunType getType() {
        List<PortalGunType> list = PortalGunTypeRegistry.CLIENT_TYPES;
        return list.get(index);
    }

    private void cycleValue(int delta) {
        List<PortalGunType> list = PortalGunTypeRegistry.CLIENT_TYPES;
        this.index = Mth.positiveModulo(this.index + delta, list.size());
    }

    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        Optional<Component> optional = Optional.empty();
        if (this.hoveredSlot != null) {
            if (!hoveredSlot.hasItem()) {
                if (hoveredSlot.index == 36) {

                    optional = Optional.of(Component.translatable("notice.ricksportalgun.workbench.insert_portal_gun"));
                } else if (hoveredSlot.index == 37 || hoveredSlot.index == 38) {
                    optional = Optional.of(Component.translatable("notice.ricksportalgun.workbench.insert_dye_or_bucket"));
                }
                optional.ifPresent((component) -> guiGraphics.renderTooltip(this.font, this.font.split(component, 115), x, y));
            }
        }
        super.renderTooltip(guiGraphics, x, y);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        this.renderTooltip(graphics, mouseX, mouseY);
        ItemStack stack = getStack(36);
        if (!stack.isEmpty() && stack.is(PGTags.Items.PORTAL_GUNS)) {
            PortalGunType chooseType = getType();
            PortalGunType currentType = PortalGunItem.getPortalGunType(stack);

            graphics.enableScissor(this.width / 2 - 91, this.height / 2 - 84, this.width / 2 - 3, this.height / 2 - 28);
            GuiHelper.drawWordWrap(graphics, this.font, Component.translatable("ricksportalgun.gun_type", "").append(currentType.name()),
                    this.width / 2 - 48, this.height / 2 - 62, 86, Color.WHITE.getRGB());
            graphics.disableScissor();

            graphics.enableScissor(this.width / 2, this.height / 2 - 84, this.width / 2 + 81, this.height / 2 - 60);
            GuiHelper.drawWordWrap(graphics, this.font, chooseType.name(), this.width / 2 + 40, this.height / 2 - 82, 82, Color.WHITE.getRGB());
            graphics.disableScissor();
            this.select.render(graphics, mouseX, mouseY, delta);
            this.next.render(graphics, mouseX, mouseY, delta);
            this.previous.render(graphics, mouseX, mouseY, delta);
        }


        this.next.setTexture(next.isHovered() ? NEXT_HL : NEXT);
        this.previous.setTexture(previous.isHovered() ? PREVIOUS_HL : PREVIOUS);

        renderPortalGunType(graphics, mouseX, mouseY, delta);
    }

    private void renderPortalGunType(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        PortalGunType type = getType();

        ItemStack stack = getStack(36);
        if (stack.is(PGTags.Items.PORTAL_GUNS)) {
            ItemStack fakeStack = stack.copy();
            ItemStack dyeStack1 = getStack(37);
            ItemStack dyeStack2 = getStack(38);

            int primary = 0;
            int secondary = 0;

            if (!dyeStack1.isEmpty()) {
                if (dyeStack1.getItem() instanceof DyeItem dyeItem) {
                    primary = dyeItem.getDyeColor().getTextureDiffuseColor();
                } else if (dyeStack1.is(Items.WATER_BUCKET)) fakeStack.remove(PGDataComponents.PRIMARY_DYE);
            }
            if (!dyeStack2.isEmpty()) {
                if (dyeStack2.getItem() instanceof DyeItem dyeItem) {
                    secondary = dyeItem.getDyeColor().getTextureDiffuseColor();
                } else if (dyeStack2.is(Items.WATER_BUCKET)) fakeStack.remove(PGDataComponents.SECONDARY_DYE);
            }
            int color = PortalGunItem.getColor(stack);


            PortalGunItem.setPortalGunType(fakeStack, type);
            PortalGunItem.setColor(fakeStack, color);
            if (primary != 0) PortalGunItem.setPrimaryDye(fakeStack, primary);
            if (secondary != 0) PortalGunItem.setSecondaryDye(fakeStack, secondary);

            graphics.enableScissor(this.width / 2, this.height / 2 - 62, this.width / 2 + 81, this.height / 2 - 18);
            renderFakeItem(graphics, fakeStack, 36, this.width / 2 + 40, this.height / 2 - 44, 32, mouseX, mouseY);
            graphics.disableScissor();
        }
    }

    public void renderFakeItem(GuiGraphics graphics, ItemStack stack, float scale, int x, int y, int z, int mouseX, int mouseY) {
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();

        poseStack.translate(x, y, z);
        poseStack.scale(scale, -scale, scale);

        if (hasShiftDown()) {
            poseStack.mulPose(Axis.YP.rotationDegrees(mouseX));
            poseStack.mulPose(Axis.XN.rotationDegrees(((float) mouseY * 2) + 180));
            float zoom = (float) (scroll / 2);
            if (zoom > 0) poseStack.scale(zoom, zoom, zoom);
        }
        else {
            poseStack.mulPose(Axis.YP.rotationDegrees((rot++) / 3));
            poseStack.mulPose(Axis.ZN.rotationDegrees(20));
        }
        ItemStackRenderState state = new ItemStackRenderState();

        Minecraft.getInstance().getItemModelResolver().updateForTopItem(state, stack, ItemDisplayContext.FIXED, false, null, null, 0);
        graphics.drawSpecial(source -> state.render(poseStack, source, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY));

        poseStack.popPose();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (hasShiftDown()) {
            scroll += dragY > 0 ? Mth.clamp(dragY, 0.1, 2) : Mth.clamp(dragY, -2, -0.1);
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.blit(RenderType::guiTextured, BG, x + 12, y, 0f, 0f, imageWidth, imageHeight, 256, 256);
    }
}
