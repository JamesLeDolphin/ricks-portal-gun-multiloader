package com.jdolphin.ricksportalgun.client.render.portal.shape;

import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;
import org.joml.Math;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class OctagonShapeRenderer extends AbstractPortalShapeRenderer {

    @Override
    public void renderInGui(int x, int y, int width, int height, ItemStack stack, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {

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
            double angle1 = org.joml.Math.toRadians(45 * i);
            double angle2 = org.joml.Math.toRadians(45 * ((i + 1) % 8));

            float vx1 = cx + (float)(org.joml.Math.cos(angle1) * rx);
            float vy1 = cy + (float)(org.joml.Math.sin(angle1) * ry);

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
