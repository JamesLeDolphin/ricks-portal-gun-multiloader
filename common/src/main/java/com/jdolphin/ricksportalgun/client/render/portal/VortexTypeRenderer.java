package com.jdolphin.ricksportalgun.client.render.portal;

import com.jdolphin.ricksportalgun.client.render.portal.shape.AbstractPortalShapeRenderer;
import com.jdolphin.ricksportalgun.common.customization.PortalType;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.init.PGEntities;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class VortexTypeRenderer extends AbstractPortalTypeRenderer {

    public VortexTypeRenderer(PortalType type) {
        super(type);
    }

    @Override
    public ResourceLocation getTextureLocation(PortalEntity portal) {
        return PGHelper.vanilla("textures/entity/end_portal.png");
    }

    @Override
    public void renderInGui(int x, int y, int width, int height, ItemStack stack, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick, int red, int green, int blue) {
        try {
            //PoseStack pose = graphics.pose();
            //Tesselator tesselator = Tesselator.getInstance();
            //BufferBuilder builder = tesselator.getBuilder();
            //MultiBufferSource.BufferSource source = MultiBufferSource.immediate(builder);
            //renderVortex(1, 1, 1, red / 255f, green / 255f, blue / 255f, 1, pPartialTick, Minecraft.getInstance().player.tickCount,
            //        pose, source.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(null))));
            //source.endBatch();
            float f1 = (float)Math.atan(pMouseY / 20.0F);
            float f = (float)Math.atan(pMouseX / 40.0F);
            PortalEntity entity = new PortalEntity(PGEntities.PORTAL, Minecraft.getInstance().level);
            float f2 = (Minecraft.getInstance().level.getGameTime() + pPartialTick) % 1;
            float v = (float) (Math.cos((f2 / 45) * Math.PI * 2));
            entity.setLifetime(Math.round(v));
            entity.setMaxLifetime(PGHelper.seconds(5));
            //entity.setPortalType(PGPortalTypes.VORTEX);
            entity.setYRot(f * 40.0F);
            float scale = 75;

            Quaternionf quaternionf = (new Quaternionf());
            Quaternionf quaternionf1 = (new Quaternionf()).rotateX(f1 * 20.0F * ((float)Math.PI / 180F));
            quaternionf.mul(quaternionf1);

            graphics.pose().pushPose();
            graphics.pose().translate(x, y, (double)50.0F);
            graphics.pose().mulPoseMatrix((new Matrix4f()).scaling(scale, scale, -scale));
            graphics.pose().translate(0, 0, -5);

            graphics.pose().mulPose(quaternionf);
            //graphics.pose().mulPose(Axis.YP.rotationDegrees(180));
            Lighting.setupForEntityInInventory();
            EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
            if (quaternionf1 != null) {
                quaternionf1.conjugate();
                entityrenderdispatcher.overrideCameraOrientation(quaternionf1);
            }

            entityrenderdispatcher.setRenderShadow(false);
            RenderSystem.enableCull();
            RenderSystem.runAsFancy(() -> entityrenderdispatcher.render(entity, 0.0F, 0.0F, 0.0F, 0, pPartialTick, graphics.pose(), graphics.bufferSource(),
                    15728880));
            RenderSystem.disableCull();
            graphics.flush();
            entityrenderdispatcher.setRenderShadow(true);
            graphics.pose().popPose();
            Lighting.setupFor3DItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void renderPortal(PortalEntity entity, AbstractPortalShapeRenderer shapeRenderer, float yaw, float partialTick, PoseStack stack, MultiBufferSource source, int packedLight, float red, float green, float blue) {
        stack.pushPose();

        stack.mulPose(Axis.YN.rotationDegrees(entity.getYRot()));
        stack.mulPose(Axis.ZN.rotationDegrees(((float)entity.tickCount + partialTick)));

        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        VertexConsumer consumer1 = source.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));
        renderVortex(1.2f, 1.2f, 1.2f, red, green, blue, 0.5f, partialTick, entity.getLifetime(), stack, consumer1);

        RenderSystem.depthMask(true);
        stack.popPose();
    }

    private void renderVortex(float x, float y, float z, float r, float g, float b, float alpha, float partialTick, int tickCount, PoseStack poseStack, VertexConsumer vertexconsumer) {
        float sqr = x * x + y * y + z * z;
        float f1 = Mth.sqrt(sqr);
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.translate(0, 0, -2);
        float f2 = 0.0F - ((float)tickCount - partialTick) * 0.01F;
        float f3 = Mth.sqrt(sqr) / 16.0F - ((float)tickCount - partialTick) * 0.01F;

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

            GuiHelper.renderVertex(vertexconsumer, matrix4f, matrix3f, 0, 0, 0, r, g, b, alpha, f6, f3, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);
            GuiHelper.renderVertex(vertexconsumer, matrix4f, matrix3f, f7, f8, f1, r, g, b, alpha, f6, f2, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);
            GuiHelper.renderVertex(vertexconsumer, matrix4f, matrix3f, f4, f5, f1, r, g, b, alpha, f9, f2, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);
            GuiHelper.renderVertex(vertexconsumer, matrix4f, matrix3f, 0, 0, 0, r, g, b, alpha, f9, f3, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, -1, 0);

            f4 = f7;
            f5 = f8;
            f6 = f9;
        }
        poseStack.popPose();
    }
}
