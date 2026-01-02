package com.jdolphin.ricksportalgun.client.render.portal.shape;

import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.joml.Math;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class VortexShapeRenderer extends AbstractPortalShapeRenderer {

    @Override
    public void renderInGui(int x, int y, int width, int height, ItemStack stack, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {

    }

    @Override
    public void renderInLevel(PoseStack stack, float x1, float y1, float x2, float y2, float z1, float z2, float red, float green, float blue, float alpha, float u1, float v1, float u2, float v2, VertexConsumer consumer) {
        Matrix4f matrix4f = stack.last().pose();
        Matrix3f matrix3f = stack.last().normal();

        RenderSystem.enableCull();
        stack.mulPose(Axis.YP.rotationDegrees(180));
        stack.translate(0, 0, -1);
        float sqr = x1 * x1 + y1 * y1 + z1 * z1;
        float f1 = Mth.sqrt(sqr);
        float f4 = 0.0F;
        float f5 = 0.75F;

        for(int j = 1; j <= 8; ++j) {
            float f7 = Mth.sin((float)j * ((float) org.joml.Math.PI * 2F) / 8.0F) * 0.75f;
            float f8 = Mth.cos((float)j * ((float) Math.PI * 2F) / 8.0F) * 0.75f;

            GuiHelper.renderVertex(consumer, matrix4f, matrix3f, 0, 0, z1, red, green, blue, alpha, u2, v2, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);
            GuiHelper.renderVertex(consumer, matrix4f, matrix3f, f7, f8, f1, red, green, blue, alpha, u1, v2, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);
            GuiHelper.renderVertex(consumer, matrix4f, matrix3f, f4, f5, f1, red, green, blue, alpha, u1, v1, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);
            GuiHelper.renderVertex(consumer, matrix4f, matrix3f, 0, 0, z1, red, green, blue, alpha, u2, v1, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);

            f4 = f7;
            f5 = f8;
        }
        RenderSystem.disableCull();
    }
}
