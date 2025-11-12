package com.jdolphin.ricksportalgun.common.customization.portaltypes;

import com.jdolphin.ricksportalgun.common.customization.PGPortalType;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class VortexType extends PGPortalType {

    public VortexType() {
        super("vortex", false);
    }

    @Override
    public void renderPortal(PortalEntity entity, float yaw, float partialTick, PoseStack stack, MultiBufferSource source, int packedLight, float red, float green, float blue) {
        stack.pushPose();
        VertexConsumer vertexconsumer = source.getBuffer(RenderType.entityCutout(new ResourceLocation("textures/entity/end_portal.png")));
        stack.mulPose(Axis.YN.rotationDegrees(entity.getYRot()));
        stack.scale(entity.getSize(), Math.max(2, entity.getSize()), entity.getSize() / 2);
        renderVortex(1.2f, 1.2f, 1.2f, red, green, blue, 0.1f, partialTick, entity.tickCount, stack, vertexconsumer);
        stack.popPose();
    }

    private void renderVortex(float x, float y, float z, float r, float g, float b, float alpha, float partialTick, int tickCount, PoseStack poseStack, VertexConsumer vertexconsumer) {
        float sqr = x * x + y * y + z * z;
        float f1 = Mth.sqrt(sqr);
        poseStack.pushPose();
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.translate(0, 0, -2);

        float f2 = 0.0F - ((float)tickCount + partialTick) * 0.01F;
        float f3 = Mth.sqrt(sqr) / 16.0F - ((float)tickCount + partialTick) * 0.01F;

        float f4 = 0.0F;
        float f5 = 0.75F;
        float f6 = 0.0F;
        PoseStack.Pose posestack$pose = poseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();

        for(int j = 1; j <= 8; ++j) {
            float f7 = Mth.sin((float)j * ((float)Math.PI * 2F) / 8.0F) * 0.75f;
            float f8 = Mth.cos((float)j * ((float)Math.PI * 2F) / 8.0F) * 0.75f;
            float f9 = (float)j / 8.0F;
            vertexconsumer.vertex(matrix4f, f7 * 0.2F, f8 * 0.2F, 0.0F).color(r, g, b, alpha).uv(f6, f3).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, f7, f8, f1).color(r, g, b, alpha).uv(f6, f2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, f4, f5, f1).color(r, g, b, alpha).uv(f9, f2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, f4 * 0.2F, f5 * 0.2F, 0.0F).color(r, g, b, alpha).uv(f9, f3).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            f4 = f7;
            f5 = f8;
            f6 = f9;
        }
        float i = 0.15f;
        poseStack.pushPose();
        poseStack.mulPose(Axis.YN.rotationDegrees(45));
        vertexconsumer.vertex(matrix4f, -i, -i, 0.0F).color(r, g, b, alpha).uv(f6, f2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexconsumer.vertex(matrix4f, i, -i, 0.0F).color(r, g, b, alpha).uv(f6, f2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexconsumer.vertex(matrix4f,  i, i, 0.0F).color(r, g, b, alpha).uv(f6, f2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexconsumer.vertex(matrix4f, -i, i, 0.0F).color(r, g, b, alpha).uv(f6, f2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();

        poseStack.popPose();
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        poseStack.popPose();
    }
}
