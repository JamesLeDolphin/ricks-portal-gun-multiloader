package com.jdolphin.ricksportalgun.client.render.portal;

import com.jdolphin.ricksportalgun.common.customization.PortalType;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.joml.Math;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public abstract class PortalTypeRenderer {
    protected final PortalType type;

    public PortalTypeRenderer(PortalType type) {
        this.type = type;
    }

    public void openAnimation(PortalEntity entity, PoseStack stack, float delta, int packedLight) {
        if (!entity.exists() && entity.getLifetime() > entity.getMaxLifetime() - 20) {
            float f = Mth.lerp((entity.getMaxLifetime() - entity.getLifetime()) / 20f, 0.0f, 1.0f);
            f = Mth.clamp(f, 0.0f, 1.0f);
            f *= f;
            f *= f;
            stack.scale(f, f, f);
        }
    }

    protected void drawShapedVertex(PoseStack stack, float x1, float y1, float x2, float y2, float z1, float z2, float red, float green, float blue, float alpha, float u1, float v1, float u2, float v2,
                                    VertexConsumer consumer, PortalType.PortalShape shape) {
        stack.pushPose();
        Matrix4f matrix4f = stack.last().pose();
        Matrix3f matrix3f = stack.last().normal();
        float midX = (x2 + x1) * 0.5f;
        float midY = (y2 + y1) * 0.5f;
        switch (shape) {
            case SQUARE -> GuiHelper.renderVertexes(matrix4f, matrix3f, consumer, x1, x2, y1, y2, z1, z2,
                    red, green, blue, alpha,
                    u1, v1, u2, v2,
                    OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,
                    0, -1, 0);
            case TRIANGLE -> {
                GuiHelper.renderVertex(consumer, matrix4f, matrix3f, x1, y1, z1, red, green, blue, alpha, u2, v2, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);
                GuiHelper.renderVertex(consumer, matrix4f, matrix3f, midX, y2, z2, red, green, blue, alpha, u2, v1, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);
                GuiHelper.renderVertex(consumer, matrix4f, matrix3f, x2, y1, z1, red, green, blue, alpha, u1, v2, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);
                GuiHelper.renderVertex(consumer, matrix4f, matrix3f, x1, y1, z1, 0, 0, 0, 0, 0, 0, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);
            }
            case DIAMOND -> {
                GuiHelper.renderVertex(consumer, matrix4f, matrix3f, x1, midY, z1, red, green, blue, alpha, u1, v2, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);
                GuiHelper.renderVertex(consumer, matrix4f, matrix3f, midX, y1, z2, red, green, blue, alpha, u2, v2, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);
                GuiHelper.renderVertex(consumer, matrix4f, matrix3f, x2, midY, z1, red, green, blue, alpha, u2, v1, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);
                GuiHelper.renderVertex(consumer, matrix4f, matrix3f, midX, y2, z2, red, green, blue, alpha, u1, v1, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);
            }
            case OCTAGON -> {
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

                    float vx2 = cx + (float)(Math.cos(angle2) * rx);
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
            case VORTEX -> {
                RenderSystem.enableCull();
                stack.mulPose(Axis.YP.rotationDegrees(180));
                stack.translate(0, 0, -1);
                float sqr = x1 * x1 + y1 * y1 + z1 * z1;
                float f1 = Mth.sqrt(sqr);
                float f4 = 0.0F;
                float f5 = 0.75F;

                for(int j = 1; j <= 8; ++j) {
                    float f7 = Mth.sin((float)j * ((float) Math.PI * 2F) / 8.0F) * 0.75f;
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
        stack.popPose();
    }

    public float getWidth(PortalEntity entity) {
        return (entity.getSize() / 3) * 1.5f;
    }

    public float getHeight(PortalEntity entity) {
        return Math.max(2, entity.getSize());
    }

    public void closeAnimation(PortalEntity entity, PoseStack stack, float delta, int packedLight) {
        if (entity.getLifetime() < 20) {
            float f = (float) entity.getLifetime() / 20.0f;
            f = Mth.clamp(f, 0.0f, 1.0f);
            f = f * f * f * f;
            stack.scale(f, f, f);
        }
    }

    public abstract ResourceLocation getTextureLocation(PortalEntity portal);

    public abstract void renderInGui(int x, int y, int width, int height, ItemStack stack, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick, int red, int green, int blue);

    public abstract void renderPortal(PortalEntity entity, float yaw, float partialTick, PoseStack stack, MultiBufferSource source, int packedLight, float red, float green, float blue);
}
