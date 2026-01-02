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

public class SquareShapeRenderer extends AbstractPortalShapeRenderer {

    @Override
    public void renderInGui(int x, int y, int width, int height, ItemStack stack, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {

    }

    @Override
    public void renderInLevel(PoseStack stack, float x1, float y1, float x2, float y2, float z1, float z2, float red, float green, float blue, float alpha, float u1, float v1, float u2, float v2, VertexConsumer consumer) {
        stack.pushPose();
        Matrix4f matrix4f = stack.last().pose();
        Matrix3f matrix3f = stack.last().normal();
        GuiHelper.renderVertexes(matrix4f, matrix3f, consumer, x1, x2, y1, y2, z1, z2,
                red, green, blue, alpha,
                u1, v1, u2, v2,
                OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,
                0, -1, 0);
        stack.popPose();
    }
}
