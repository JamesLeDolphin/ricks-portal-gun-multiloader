package com.jdolphin.ricksportalgun.client.render.portal.shape;

import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.function.Consumer;

public class TriangleShapeRenderer extends AbstractPortalShapeRenderer {

    @Override
    public void renderInGui(int x, int y, int z, int width, int height, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick, int red, int green, int blue, int alpha,
                            float u1, float v1, float u2, float v2, RenderType type, Consumer<BufferBuilder> consumer) {
        int x2 = x + width;
        int y2 = y + height;

        float midX = x + (float) width / 2;

        RenderSystem.enableBlend();

        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        Matrix4f matrix4f = graphics.pose().last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);

        bufferbuilder.vertex(matrix4f, x, y2, z).color(red, green, blue, alpha).uv(u1, v2).endVertex();
        bufferbuilder.vertex(matrix4f, midX, y2, z).color(red, green, blue, alpha).uv(u2, v1).endVertex();
        bufferbuilder.vertex(matrix4f, x2, y2, z).color(red, green, blue, alpha).uv(u2, v2).endVertex();
        bufferbuilder.vertex(matrix4f, midX, y, z).color(red, green, blue, alpha).uv(u2, v1).endVertex();

        consumer.accept(bufferbuilder);
        RenderSystem.disableBlend();
    }

    @Override
    public void renderInLevel(PoseStack stack, float x1, float y1, float x2, float y2, float z1, float z2, float red, float green, float blue, float alpha, float u1, float v1, float u2, float v2, VertexConsumer consumer) {
        Matrix4f matrix4f = stack.last().pose();
        Matrix3f matrix3f = stack.last().normal();
        float midX = (x2 + x1) * 0.5f;
        GuiHelper.renderVertex(consumer, matrix4f, matrix3f, x1, y1, z2, red, green, blue, alpha, u2, v2, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,0, -1, 0);
        GuiHelper.renderVertex(consumer, matrix4f, matrix3f, x2, y1, z1, red, green, blue, alpha, u1, v2, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,0, -1, 0);
        GuiHelper.renderVertex(consumer, matrix4f, matrix3f, midX, y2, z2, red, green, blue, alpha, u1, v1, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,0, -1, 0);
        GuiHelper.renderVertex(consumer, matrix4f, matrix3f, x1, y1, z1, red, green, blue, alpha, u2, v1, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,0, -1, 0);
    }
}
