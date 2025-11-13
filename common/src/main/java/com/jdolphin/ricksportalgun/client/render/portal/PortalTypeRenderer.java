package com.jdolphin.ricksportalgun.client.render.portal;

import com.jdolphin.ricksportalgun.common.customization.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.joml.Math;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public abstract class PortalTypeRenderer {

    public PortalTypeRenderer() {}

    public void openAnimation(PortalEntity entity, PoseStack stack, float delta, int packedLight) {
        if (!entity.exists() && entity.getLifetime() > entity.getMaxLifetime() - 20) {
            float f = Mth.lerp((entity.getMaxLifetime() - entity.getLifetime()) / 20f, 0.0f, 1.0f);
            f = Mth.clamp(f, 0.0f, 1.0f);
            f *= f;
            f *= f;
            stack.scale(f, f, f);
        }
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
            f = f * f * f * f; // easing
            stack.scale(f, f, f);
        }
    }

    public abstract ResourceLocation getTextureLocation(PortalEntity portal);

    public abstract void renderInGui(ItemStack stack, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick, PortalGunStyle style);

    public abstract void renderPortal(PortalEntity entity, float yaw, float partialTick, PoseStack stack, MultiBufferSource source, int packedLight, float red, float green, float blue);


    public final void renderVertexes(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer consumer, float x1, float x2, float y1, float y2, float z1, float z2,
                                float red, float green, float blue, float alpha,
                                float u1, float v1, float u2, float v2,
                                int overlay, int light,
                                float normalX, float normalY, float normalZ) {

        renderVertex(consumer, matrix4f, matrix3f, x1, y1, z1, red, green, blue, alpha, u1, v2, overlay, light, normalX, normalY, normalZ);
        renderVertex(consumer, matrix4f, matrix3f, x2, y1, z2, red, green, blue, alpha, u2, v2, overlay, light, normalX, normalY, normalZ);
        renderVertex(consumer, matrix4f, matrix3f, x2, y2, z1, red, green, blue, alpha, u2, v1, overlay, light, normalX, normalY, normalZ);
        renderVertex(consumer, matrix4f, matrix3f, x1, y2, z2, red, green, blue, alpha, u1, v1, overlay, light, normalX, normalY, normalZ);
    }

    public final void renderVertex(VertexConsumer consumer, Matrix4f matrix4f, Matrix3f matrix3f,
                             float x, float y, float z,
                             float red, float green, float blue, float alpha,
                             float u, float v, int overlay, int light,
                             float normalX, float normalY, float normalZ) {
        consumer.vertex(matrix4f, x, y, z).color(red, green, blue, alpha).uv(u, v).overlayCoords(overlay).uv2(light).normal(matrix3f, normalX, normalY, normalZ).endVertex();
    }
}
