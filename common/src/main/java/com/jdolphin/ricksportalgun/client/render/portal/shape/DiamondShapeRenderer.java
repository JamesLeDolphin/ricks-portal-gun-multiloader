package com.jdolphin.ricksportalgun.client.render.portal.shape;

import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class DiamondShapeRenderer extends AbstractPortalShapeRenderer {
    @Override
    public void renderInGui(int x, int y, int width, int height, ItemStack stack, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {

    }

    @Override
    public void renderInLevel(PoseStack stack, float x1, float y1, float x2, float y2, float z1, float z2, float red, float green, float blue, float alpha, float u1, float v1, float u2, float v2, VertexConsumer consumer) {
        Matrix4f matrix4f = stack.last().pose();
        Matrix3f matrix3f = stack.last().normal();
        float midX = (x2 + x1) * 0.5f;
        float midY = (y2 + y1) * 0.5f;
        GuiHelper.renderVertex(consumer, matrix4f, matrix3f, x1, midY, z1, red, green, blue, alpha, u1, v2, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);
        GuiHelper.renderVertex(consumer, matrix4f, matrix3f, midX, y1, z2, red, green, blue, alpha, u2, v2, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);
        GuiHelper.renderVertex(consumer, matrix4f, matrix3f, x2, midY, z1, red, green, blue, alpha, u2, v1, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);
        GuiHelper.renderVertex(consumer, matrix4f, matrix3f, midX, y2, z2, red, green, blue, alpha, u1, v1, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);
    }
}
