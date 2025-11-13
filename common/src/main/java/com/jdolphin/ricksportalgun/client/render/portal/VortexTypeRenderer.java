package com.jdolphin.ricksportalgun.client.render.portal;

import com.jdolphin.ricksportalgun.common.customization.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class VortexTypeRenderer extends PortalTypeRenderer {

    public VortexTypeRenderer() {

    }

    @Override
    public ResourceLocation getTextureLocation(PortalEntity portal) {
        return PGHelper.vanilla("textures/entity/end_portal.png");
    }

    @Override
    public void renderInGui(ItemStack stack, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick, PortalGunStyle style) {

    }

    @Override
    public void renderPortal(PortalEntity entity, float yaw, float partialTick, PoseStack stack, MultiBufferSource source, int packedLight, float red, float green, float blue) {
        stack.pushPose();

        stack.mulPose(Axis.YN.rotationDegrees(entity.getYRot()));
        stack.mulPose(Axis.ZN.rotationDegrees(((float)entity.tickCount + partialTick)));

        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        VertexConsumer consumer = source.getBuffer(RenderType.eyes(getTextureLocation(entity)));
        renderVortex(2, 1.2f, 0.5f, red, green, blue, 1, partialTick, entity.tickCount, stack, consumer);

        VertexConsumer consumer1 = source.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));
        renderVortex(1.2f, 1.2f, 1.2f, red, green, blue, 1f, partialTick, entity.tickCount, stack, consumer1);

        RenderSystem.depthMask(true);
        stack.popPose();
    }

    private void renderVortex(float x, float y, float z, float r, float g, float b, float alpha, float partialTick, int tickCount, PoseStack poseStack, VertexConsumer vertexconsumer) {
        float sqr = x * x + y * y + z * z;
        float f1 = Mth.sqrt(sqr);
        poseStack.pushPose();

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

            renderVertex(vertexconsumer, matrix4f, matrix3f, 0, 0, 0, r, g, b, alpha, f6, f3, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);
            renderVertex(vertexconsumer, matrix4f, matrix3f, f7, f8, f1, r, g, b, alpha, f6, f2, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);
            renderVertex(vertexconsumer, matrix4f, matrix3f, f4, f5, f1, r, g, b, alpha, f9, f2, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);
            renderVertex(vertexconsumer, matrix4f, matrix3f, 0, 0, 0, r, g, b, alpha, f9, f3, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);

            f4 = f7;
            f5 = f8;
            f6 = f9;
        }
        poseStack.popPose();
    }
}
