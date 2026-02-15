package com.jdolphin.ricksportalgun.client.render.portal;

import com.jdolphin.ricksportalgun.common.comp.immersive_portals.SeeThroughPortalEntity;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import qouteall.imm_ptl.core.CHelper;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.q_misc_util.my_util.DQuaternion;

public class DinoPortalRenderer extends EntityRenderer<Portal> {
    public DinoPortalRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    public void openAnimation(SeeThroughPortalEntity entity, PoseStack stack, float delta, int packedLight) {
        float f = Mth.lerp((entity.getMaxLifetime() - entity.getLifetime()) / 20f, 0.0f, 1.0f);
        f = Mth.clamp(f, 0.0f, 1.0f);
        f *= f;
        f *= f;
        stack.scale(f, f, f);
    }

    public void closeAnimation(SeeThroughPortalEntity entity, PoseStack stack, float delta, int packedLight) {
        float f = (float) entity.getLifetime() / 20.0f;
        f = Mth.clamp(f, 0.0f, 1.0f);
        f = f * f * f * f;
        stack.scale(f, f, f);
    }

    public void render(Portal entity, float yaw, float tickDelta, PoseStack stack, MultiBufferSource vertexConsumers, int light) {
        if (entity instanceof SeeThroughPortalEntity portal) {
            super.render(entity, yaw, tickDelta, stack, vertexConsumers, light);

            // don't render overlay from back side
            if (!entity.isInFrontOfPortal(CHelper.getCurrentCameraPos())) {
                return;
            }

            stack.pushPose();
            Matrix4f matrix4f = stack.last().pose();
            Matrix3f matrix3f = stack.last().normal();

            matrix3f.rotate(DQuaternion.rotationByDegrees(new Vec3(1, 0, 0), -90).toMcQuaternion());
            matrix4f.rotate(entity.getOrientationRotation().toMcQuaternion());

            stack.translate(0, 0, 0.001);

            int color = portal.getColor();
            int red = FastColor.ARGB32.red(color);
            int green = FastColor.ARGB32.green(color);
            int blue = FastColor.ARGB32.blue(color);

            VertexConsumer consumer = vertexConsumers.getBuffer(RenderType.entityTranslucent(getTextureLocation(portal)));
            float width = (float) portal.width * 0.5f;
            float height = (float) portal.height * 0.5f;

            GuiHelper.renderVertexes(matrix4f, matrix3f, consumer, -width, width, -height, height, 0.01f, 0.01f,
                    red / 255f, green / 255f, blue / 255f, 0.6f,
                    0, 0, 1, 1,
                    OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,
                    0, 1, 0);
            GuiHelper.renderVertexes(matrix4f, matrix3f, consumer, -width, width, height, -height, -0.01f, -0.01f,
                    red / 255f, green / 255f, blue / 255f, 0.6f,
                    0, 0, 1, 1,
                    OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,
                    0, -1, 0);
            stack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Portal entity) {
        return PGHelper.id("textures/entity/dino_portal_overlay.png");
    }
}
