package com.jdolphin.ricksportalgun.client.render.portal.type;

import com.jdolphin.ricksportalgun.client.render.portal.shape.AbstractPortalShapeRenderer;
import com.jdolphin.ricksportalgun.common.customization.type.PortalType;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class StarsPortalTypeRenderer extends AbstractPortalTypeRenderer {

    public StarsPortalTypeRenderer(PortalType type) {
        super(type);
    }

    @Override
    public ResourceLocation getTextureLocation(PortalEntity portal) {
        return TheEndPortalRenderer.END_PORTAL_LOCATION;
    }

    @Override
    public void renderInGui(int x, int y, int width, int height, AbstractPortalShapeRenderer shape, ItemStack stack, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick, int red, int green, int blue) {

        shape.renderInGui(x, y, 0, width, height, graphics, pMouseX, pMouseY, pPartialTick, red, green, blue, 255,
                0, 0, 0, 0, RenderType.entitySolid(getTextureLocation(null)), bufferBuilder -> BufferUploader.drawWithShader(bufferBuilder.end()));

    }

    @Override
    public void renderPortal(PortalEntity entity, AbstractPortalShapeRenderer shapeRenderer, float yaw, float partialTick, PoseStack stack, MultiBufferSource source, int packedLight, float red, float green, float blue) {
        stack.pushPose();

        stack.mulPose(Axis.YN.rotationDegrees(entity.getYRot()));

        float width = getWidth(entity);
        float height = entity.getSize() > 2 ? getHeight(entity) / 2 : 1;

        RenderType renderType = RenderType.entitySolid(getTextureLocation(entity));

        float progress = entity.tickCount * 0.001F % 1.0F;

        VertexConsumer consumer = source.getBuffer(renderType);
        float f = entity.tickCount + partialTick;
        float f1 = (float) Math.abs((Math.cos(f / 10) + 1) / 2);
        shapeRenderer.renderInLevel(stack, -width, -height, width, height, 0, 0, red, green, blue, f1, 0, 0 + progress, 0.5f, 0.2f + progress, consumer);
        shapeRenderer.renderInLevel(stack, width, -height, -width, height, 0, 0, red, green, blue, f1, 0, 0 + progress, 0.5f, 0.2f + progress, consumer);

        stack.popPose();
    }
}
