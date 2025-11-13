package com.jdolphin.ricksportalgun.client.render.portal;

import com.jdolphin.ricksportalgun.common.customization.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class EndPortalTypeRenderer extends PortalTypeRenderer {

    public EndPortalTypeRenderer() {
    }

    @Override
    public ResourceLocation getTextureLocation(PortalEntity portal) {
        return null;
    }

    @Override
    public void renderInGui(ItemStack stack, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick, PortalGunStyle style) {

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
        renderVertexes(matrix4f, matrix3f, consumer, -width, width, -height, height, 0f, 0f,
                1, 1, 1, 1,
                0, 0, 0, 0,
                OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,
                0, 1, 0);
        renderVertexes(matrix4f, matrix3f, consumer, -width, width, height, -height, 0f, 0f,
                1, 1, 1, 1,
                0, 0, 0, 0,
                OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,
                0, 1, 0);

        //Color overlay
        VertexConsumer consumer1 = source.getBuffer(RenderType.translucent());
        renderVertexes(matrix4f, matrix3f, consumer1, -width, width, -height, height, 0.001f, 0.001f,
                red, green, blue, 0.45f,
                0, 0, 0, 0,
                OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,
                0, 1, 0);
        renderVertexes(matrix4f, matrix3f, consumer1, -width, width, height, -height, -0.001f, -0.001f,
                red, green, blue, 0.45f,
                0, 0, 0, 0,
                OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,
                0, 1, 0);

        stack.popPose();
    }
}
