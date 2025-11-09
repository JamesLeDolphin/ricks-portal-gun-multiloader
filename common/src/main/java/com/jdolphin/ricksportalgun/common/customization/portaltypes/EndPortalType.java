package com.jdolphin.ricksportalgun.common.customization.portaltypes;

import com.jdolphin.ricksportalgun.common.customization.PGPortalType;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class EndPortalType extends PGPortalType {

    public EndPortalType() {
        super("end_portal", false);
    }

    @Override
    public void renderPortal(PortalEntity entity, float yaw, float partialTick, PoseStack stack, MultiBufferSource source, int packedLight, float red, float green, float blue) {
        stack.pushPose();
        //stack.translate(0, -1, 0);
        stack.mulPose(Axis.YN.rotationDegrees(entity.getYRot()));
        VertexConsumer consumer1 = source.getBuffer(RenderType.endPortal());
        float width = (entity.getSize() / 3) * 1.5f;
        float height = entity.getSize() > 2 ? width : 1;
        renderVertexes(stack.last().pose(), stack.last().normal(), consumer1,
                -width, width, -height, height, 0f, 0f, 1, 1, 1, 1f);
        renderVertexes(stack.last().pose(), stack.last().normal(), consumer1,
                -width, width, height, -height, 0f, 0f, 1, 1, 1, 1f);


        renderVertexes(stack.last().pose(), stack.last().normal(), source.getBuffer(RenderType.translucent()),
                 -width, width, height, -height, -0.001f, -0.001f, red, green, blue, 0.45f);

        renderVertexes(stack.last().pose(), stack.last().normal(), source.getBuffer(RenderType.translucent()),
                -width,width, -height, height, 0.001f, 0.001f, red, green, blue, 0.45f);

        stack.popPose();
    }

    private void renderVertexes(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer consumer, float x1, float x2, float y1, float y2, float z1, float z2, float red, float green, float blue, float alpha) {
        consumer.vertex(matrix4f, x1, y1, z1).color(red, green, blue, alpha)
                .uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0, 1, 0).endVertex();
        consumer.vertex(matrix4f, x2, y1, z2).color(red, green, blue, alpha)
                .uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0, 1, 0).endVertex();
        consumer.vertex(matrix4f, x2, y2, z1).color(red, green, blue, alpha)
                .uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0, 1, 0).endVertex();
        consumer.vertex(matrix4f, x1, y2, z2).color(red, green, blue, alpha)
                .uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0, 1, 0).endVertex();
    }
}
