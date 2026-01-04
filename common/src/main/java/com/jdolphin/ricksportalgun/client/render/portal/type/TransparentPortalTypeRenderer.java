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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

public class TransparentPortalTypeRenderer extends AbstractPortalTypeRenderer {

    public TransparentPortalTypeRenderer(PortalType type) {
        super(type);
    }

    @Override
    public ResourceLocation getTextureLocation(PortalEntity portal) {
        return null;
    }

    @Override
    public void renderInGui(int x, int y, int width, int height, AbstractPortalShapeRenderer shape, ItemStack stack, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick, int red, int green, int blue) {
        int x2 = x + width / 2;
        int y2 = y + height;

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

        VertexConsumer consumer1 = source.getBuffer(RenderType.translucent());
        shapeRenderer.renderInLevel(stack, -width, -height, width, height, 0.005f, 0.005f, red, green, blue, 0.45f, 0, 0, 0, 0, consumer1);
        shapeRenderer.renderInLevel(stack, width, -height, -width, height, -0.005f, -0.005f, red, green, blue, 0.45f, 0, 0, 0, 0, consumer1);
        stack.popPose();
    }
}
