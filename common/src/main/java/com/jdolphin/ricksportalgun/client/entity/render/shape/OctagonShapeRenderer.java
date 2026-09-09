package com.jdolphin.ricksportalgun.client.entity.render.shape;

import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Math;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.function.Consumer;

public class OctagonShapeRenderer extends AbstractPortalShapeRenderer {

    @Override
    public void renderInGui(int x, int y, int z, int width, int height, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick, int red, int green, int blue, int alpha, float u1, float v1, float u2, float v2, RenderType type, Consumer<BufferBuilder> consumer) {
        int x2 = x + width;
        int y2 = y + height;

        float width1 = x2 - x;
        float height1 = y2 - y;

        float cx = x + width1 / 2f;
        float cy = y + height1 / 2f;

        float rx = width1 * 0.5f;
        float ry = height1 * 0.5f;

        for (int i = 0; i < 8; i++) {
            double angle1 = Math.toRadians(45 * i);
            double angle2 = Math.toRadians(45 * ((i + 1) % 8));

            float vx1 = cx + (float) (Math.cos(angle1) * rx);
            float vy1 = cy + (float) (Math.sin(angle1) * ry);

            float vx2 = cx + (float) (Math.cos(angle2) * rx);
            float vy2 = cy + (float) (Math.sin(angle2) * ry);

            RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
            Matrix4f matrix4f = graphics.pose().last().pose();
            BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);

            bufferbuilder.vertex(matrix4f, cx, cy, z).color(red, green, blue, alpha).uv(u1, v1).endVertex();
            bufferbuilder.vertex(matrix4f, vx1, vy1, z).color(red, green, blue, alpha).uv(u1, v2).endVertex();
            bufferbuilder.vertex(matrix4f, vx2, vy2, z).color(red, green, blue, alpha).uv(u2, v2).endVertex();
            bufferbuilder.vertex(matrix4f, vx1, vy1, z).color(red, green, blue, alpha).uv(u2, v1).endVertex();

            consumer.accept(bufferbuilder);
            RenderSystem.disableBlend();
        }
    }

    @Override
    public void renderInLevel(PoseStack stack, float x1, float y1, float x2, float y2, float z1, float z2, float red, float green, float blue, float alpha, float u1, float v1, float u2, float v2, VertexConsumer consumer) {
        Matrix4f matrix4f = stack.last().pose();
        Matrix3f matrix3f = stack.last().normal();

        float width = x2 - x1;
        float height = y2 - y1;

        float cx = x1 + width / 2f;
        float cy = y1 + height / 2f;

        float rx = width * 0.5f;
        float ry = height * 0.5f;

        for (int i = 0; i < 8; i++) {
            double angle1 = Math.toRadians(45 * i);
            double angle2 = Math.toRadians(45 * ((i + 1) % 8));

            float vx1 = cx + (float)(Math.cos(angle1) * rx);
            float vy1 = cy + (float)(Math.sin(angle1) * ry);

            float vx2 = cx + (float)(org.joml.Math.cos(angle2) * rx);
            float vy2 = cy + (float)(Math.sin(angle2) * ry);

            GuiHelper.renderVertex(consumer, matrix4f, matrix3f,
                    cx, cy, z1, red, green, blue, alpha,
                    u2, v2, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, 1, 0);
            GuiHelper.renderVertex(consumer, matrix4f, matrix3f,
                    vx1, vy1, z2, red, green, blue, alpha,
                    u1, v2, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, 1, 0);
            GuiHelper.renderVertex(consumer, matrix4f, matrix3f,
                    vx2, vy2, z2, red, green, blue, alpha,
                    u1, v1, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, 1, 0);
            GuiHelper.renderVertex(consumer, matrix4f, matrix3f,
                    vx2, vy2, z1, red, green, blue, alpha,
                    u2, v1, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, 1, 0);
        }
    }
}