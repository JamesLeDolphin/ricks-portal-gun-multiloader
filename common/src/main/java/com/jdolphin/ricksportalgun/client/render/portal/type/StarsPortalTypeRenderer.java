package com.jdolphin.ricksportalgun.client.render.portal.type;

import com.jdolphin.ricksportalgun.client.render.portal.shape.AbstractPortalShapeRenderer;
import com.jdolphin.ricksportalgun.common.customization.PortalType;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

public class StarsPortalTypeRenderer extends AbstractPortalTypeRenderer {

    public StarsPortalTypeRenderer(PortalType type) {
        super(type);
    }

    @Override
    public ResourceLocation getTextureLocation(PortalEntity portal) {
        return TheEndPortalRenderer.END_PORTAL_LOCATION;
    }

    @Override
    public void renderInGui(int x, int y, int width, int height, AbstractPortalShapeRenderer shape, ItemStack stack, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick, int red, int green, int blue) {

        int x2 = x + width / 2;
        int y2 = y + height;
        graphics.fill(RenderType.entitySolid(getTextureLocation(null)), x, y, x2, y2, FastColor.ARGB32.color(255, red, green, blue));

        RenderSystem.enableBlend();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Matrix4f matrix4f = graphics.pose().last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        bufferbuilder.vertex(matrix4f, x, y, 0.001f).color(red, green, blue, 128).endVertex();
        bufferbuilder.vertex(matrix4f, x, y2, 0.001f).color(red, green, blue, 128).endVertex();
        bufferbuilder.vertex(matrix4f, x2, y2, 0.001f).color(red, green, blue, 128).endVertex();
        bufferbuilder.vertex(matrix4f, x2, y, 0.001f).color(red, green, blue, 128).endVertex();

        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.disableBlend();
    }

    @Override
    public void renderPortal(PortalEntity entity, AbstractPortalShapeRenderer shapeRenderer, float yaw, float partialTick, PoseStack stack, MultiBufferSource source, int packedLight, float red, float green, float blue) {
        stack.pushPose();

        stack.mulPose(Axis.YN.rotationDegrees(entity.getYRot()));

        float width = getWidth(entity);
        float height = entity.getSize() > 2 ? getHeight(entity) / 2 : 1;

        RenderType renderType = RenderType.entitySolid(getTextureLocation(entity));

        float progress = entity.tickCount * 0.001F % 1.0F;

        VertexConsumer consumer = source.getBuffer(renderType);
        float f = entity.tickCount + partialTick;
        float f1 = (float) Math.abs((Math.cos(f / 10) + 1) / 2);
        shapeRenderer.renderInLevel(stack, -width, -height, width, height, 0, 0, red, green, blue, f1, 0, 0 + progress, 0.5f, 0.2f + progress, consumer);
        shapeRenderer.renderInLevel(stack, width, -height, -width, height, 0, 0, red, green, blue, f1, 0, 0 + progress, 0.5f, 0.2f + progress, consumer);

        stack.popPose();
    }
}
