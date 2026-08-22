package com.jdolphin.ricksportalgun.client.screen.workbench;

import com.jdolphin.ricksportalgun.client.screen.widget.PGImageButton;
import com.jdolphin.ricksportalgun.client.screen.widget.PGItemButton;
import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.menu.workbench.SkinSelectorMenu;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBSetPortalGunTypePacket;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
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
    public static final ResourceLocation BG = PGHelper.id("textures/gui/workbench/skin_select.png");
    public static ResourceLocation NEXT = PGHelper.id("textures/gui/sprites/next.png");
    public static ResourceLocation NEXT_HL = PGHelper.id("textures/gui/sprites/next_highlighted.png");
    public static ResourceLocation PREVIOUS = PGHelper.id("textures/gui/sprites/previous.png");
    public static ResourceLocation PREVIOUS_HL = PGHelper.id("textures/gui/sprites/previous_highlighted.png");

    private PGImageButton next, previous;
    private Button select;
    private int index = 0;
    private float rot = 0;
    private double scroll = 0;

    public SkinSelectingScreen(SkinSelectorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 212;
        this.imageHeight = 192;
        this.inventoryLabelX = this.imageWidth - 188;
        this.inventoryLabelY = this.imageHeight - 93;
        this.titleLabelX = this.imageWidth - 193;
        this.titleLabelY = this.imageHeight - 188;
    }

    @Override
    protected void containerTick() {
        super.containerTick();

        boolean showButtons = !getStack(36).isEmpty() && getStack(36).is(PGTags.Items.PORTAL_GUNS);
        if (select != null) select.active = showButtons;
        if (next != null) next.active = showButtons;
        if (previous != null) previous.active = showButtons;
    }

    public void init() {
        super.init();

        this.next = this.addRenderableWidget(new PGImageButton(this.width / 2 + 70, this.height / 2 - 26, 12, 32, Component.translatable("ricksportalgun.button.next"),
                button -> cycleValue(1), 32, 23, NEXT));
        next.setRenderBackground(false);
        this.previous = this.addRenderableWidget(new PGImageButton(this.width / 2 + 12, this.height / 2 - 26, 12, 32, Component.translatable("ricksportalgun.button.previous"),
                button -> cycleValue(-1), 32, 23, PREVIOUS));
        previous.setRenderBackground(false);

        this.select = this.addWidget(Button.builder(Component.translatable("ricksportalgun.button.select"), button -> {
                    PortalGunItem item = PGItems.PORTAL_GUNS.get(this.index);
                    ResourceLocation rl = BuiltInRegistries.ITEM.getKey(item);
                    SBSetPortalGunTypePacket packet = new SBSetPortalGunTypePacket(rl, item.getTints());
                    PGHelper.sendPacketToServer(packet);
                })
                .bounds(this.width / 2 + 20, this.height / 2 - 18, 48, 16).build());

        PGItemButton skin = this.addRenderableWidget(new PGItemButton(this.width / 2 + 91, this.height / 2 - 95, 24, 24,
                Component.translatable("menu.ricksportalgun.workbench.skin"), button -> {
        }, PGItems.PORTAL_GUN.getDefaultInstance()));
        skin.setRenderBackground(false);

        PGItemButton waypoint = this.addRenderableWidget(new PGItemButton(this.width / 2 + 91, this.height / 2 - 70, 24, 24,
                Component.translatable("menu.ricksportalgun.workbench.waypoint"), button -> setScreen(0), PGItems.DATA_CARD.getDefaultInstance()));
        waypoint.setRenderBackground(false);

        PGItemButton craft = this.addRenderableWidget(new PGItemButton(this.width / 2 + 91, this.height / 2 - 45, 24, 24,
                Component.translatable("menu.ricksportalgun.workbench.craft"), button -> setScreen(2), Items.CRAFTING_TABLE.getDefaultInstance()));

        craft.setRenderBackground(false);

        GuiHelper.setTooltip(skin, Component.translatable("menu.ricksportalgun.workbench.skin"));
        GuiHelper.setTooltip(waypoint, Component.translatable("menu.ricksportalgun.workbench.waypoint"));
        GuiHelper.setTooltip(craft, Component.translatable("menu.ricksportalgun.workbench.craft"));
    }

    private ItemStack getStack(int slotId) {
        return this.menu.getSlot(slotId).getItem();
    }

    private void cycleValue(int delta) {
        List<PortalGunItem> list = PGItems.PORTAL_GUNS;
        if (!list.isEmpty()) this.index = Mth.positiveModulo(this.index + delta, list.size());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, delta);

        ItemStack stack = getStack(36);
        if (!stack.isEmpty() && stack.is(PGTags.Items.PORTAL_GUNS)) {
            List<PortalGunItem> list = PGItems.PORTAL_GUNS;
            PortalGunItem item = list.get(this.index);

            if (item != null) {
                Component currentTypeName = stack.getItem().getName(stack);
                graphics.enableScissor(this.width / 2 - 91, this.height / 2 - 84, this.width / 2 - 3, this.height / 2 - 28);
                GuiHelper.drawWordWrap(graphics, this.font, Component.translatable("ricksportalgun.gun_type", "").append(currentTypeName),
                        this.width / 2 - 44, this.height / 2 - 62, 86, Color.WHITE.getRGB());
                graphics.disableScissor();

                Component name = item.getName(item.getDefaultInstance());
                graphics.enableScissor(this.width / 2, this.height / 2 - 84, this.width / 2 + 81, this.height / 2 - 60);
                GuiHelper.drawWordWrap(graphics, this.font, name, this.width / 2 + 44, this.height / 2 - 82, 86, Color.WHITE.getRGB());
                graphics.disableScissor();
                this.select.render(graphics, mouseX, mouseY, delta);
            }


            this.next.setTexture(next.isHovered() ? NEXT_HL : NEXT);
            this.previous.setTexture(previous.isHovered() ? PREVIOUS_HL : PREVIOUS);

            renderPortalGunType(graphics, mouseX, mouseY, delta);

        }
        this.renderTooltip(graphics, mouseX, mouseY);
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

    private void renderPortalGunType(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        ItemStack stack = getStack(36);
        if (stack.is(PGTags.Items.PORTAL_GUNS)) {
            ItemStack fakeStack = PGItems.PORTAL_GUNS.get(this.index).getDefaultInstance();
            ItemStack dyeStack1 = getStack(37);
            ItemStack dyeStack2 = getStack(38);

            int primary = PortalGunItem.getPrimaryDye(stack);
            int secondary = PortalGunItem.getSecondaryDye(stack);

            if (!dyeStack1.isEmpty()) {
                if (dyeStack1.getItem() instanceof DyeItem dyeItem) {
                    primary = PGHelper.getTextureDiffuseColor(dyeItem);
                } else if (dyeStack1.is(Items.WATER_BUCKET)) primary = 0;
            }
            if (!dyeStack2.isEmpty()) {
                if (dyeStack2.getItem() instanceof DyeItem dyeItem) {
                    secondary = PGHelper.getTextureDiffuseColor(dyeItem);
                } else if (dyeStack2.is(Items.WATER_BUCKET)) secondary = 0;
            }
            int color = PortalGunItem.getColor(stack);



            PortalGunItem.setColor(fakeStack, color);
            if (primary != 0) PortalGunItem.setPrimaryDye(fakeStack, primary);
            if (secondary != 0) PortalGunItem.setSecondaryDye(fakeStack, secondary);

            graphics.enableScissor(this.width / 2, this.height / 2 - 62, this.width / 2 + 81, this.height / 2 - 18);
            renderFakeItem(graphics, fakeStack, 48, this.width / 2 + 40, this.height / 2 - 44, 32, mouseX, mouseY);
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
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.NONE, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, poseStack, graphics.bufferSource(), null, 0);

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
        int y = ((height - imageHeight) / 2);

        graphics.blit(BG, x + 12, y, 0f, 0f, imageWidth, imageHeight, 256, 256);
    }
}
