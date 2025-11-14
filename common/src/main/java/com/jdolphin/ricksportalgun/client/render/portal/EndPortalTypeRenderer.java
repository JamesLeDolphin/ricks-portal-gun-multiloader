package com.jdolphin.ricksportalgun.client.render.portal;

import com.jdolphin.ricksportalgun.common.customization.PortalType;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class EndPortalTypeRenderer extends PortalTypeRenderer {

    public EndPortalTypeRenderer(PortalType type) {
        super(type);
    }

    @Override
    public ResourceLocation getTextureLocation(PortalEntity portal) {
        return null;
    }

    @Override
    public void renderInGui(int x, int y, int width, int height, ItemStack stack, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick, int red, int green, int blue) {

        int x2 = x + width / 2;
        int y2 = y + height;
        graphics.fill(RenderType.endPortal(), x, y, x2, y2, FastColor.ARGB32.color(red, green, blue, 255));

        RenderSystem.enableBlend();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Matrix4f matrix4f = graphics.pose().last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        bufferbuilder.vertex(matrix4f, x, y, 0.001f).color(red, green, blue, 128).endVertex();
        bufferbuilder.vertex(matrix4f, x, y2, 0.001f).color(red, green, blue, 128).endVertex();
        bufferbuilder.vertex(matrix4f, x2, y2, 0.001f).color(red, green, blue, 128).endVertex();
        bufferbuilder.vertex(matrix4f, x2, y, 0.001f).color(red, green, blue, 128).endVertex();

        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.disableBlend();
    }

    @Override
    public void renderPortal(PortalEntity entity, float yaw, float partialTick, PoseStack stack, MultiBufferSource source, int packedLight, float red, float green, float blue) {
        stack.pushPose();
        stack.mulPose(Axis.YN.rotationDegrees(entity.getYRot()));


        float width = getWidth(entity);
        float height = entity.getSize() > 2 ? getHeight(entity) : 1;
        Matrix4f matrix4f = stack.last().pose();
        Matrix3f matrix3f = stack.last().normal();

        //End portal effect
        VertexConsumer consumer = source.getBuffer(RenderType.endPortal());
        GuiHelper.renderVertexes(matrix4f, matrix3f, consumer, -width, width, -height, height, 0f, 0f,
                1, 1, 1, 1,
                0, 0, 0, 0,
                OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,
                0, 1, 0);
        GuiHelper.renderVertexes(matrix4f, matrix3f, consumer, -width, width, height, -height, 0f, 0f,
                1, 1, 1, 1,
                0, 0, 0, 0,
                OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,
                0, 1, 0);

        //Color overlay
        VertexConsumer consumer1 = source.getBuffer(RenderType.translucent());
        GuiHelper.renderVertexes(matrix4f, matrix3f, consumer1, -width, width, -height, height, 0.001f, 0.001f,
                red, green, blue, 0.45f,
                0, 0, 0, 0,
                OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,
                0, 1, 0);
        GuiHelper.renderVertexes(matrix4f, matrix3f, consumer1, -width, width, height, -height, -0.001f, -0.001f,
                red, green, blue, 0.45f,
                0, 0, 0, 0,
                OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,
                0, 1, 0);

        stack.popPose();
    }
}
