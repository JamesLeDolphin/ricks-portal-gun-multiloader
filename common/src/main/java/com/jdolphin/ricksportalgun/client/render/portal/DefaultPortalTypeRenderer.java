package com.jdolphin.ricksportalgun.client.render.portal;

import com.jdolphin.ricksportalgun.client.render.portal.shape.AbstractPortalShapeRenderer;
import com.jdolphin.ricksportalgun.common.customization.PortalType;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class DefaultPortalTypeRenderer extends AbstractPortalTypeRenderer {

    public DefaultPortalTypeRenderer(PortalType type) {
        super(type);
    }

    @Override
    public void renderPortal(PortalEntity entity, AbstractPortalShapeRenderer shapeRenderer, float yaw, float partialTick, PoseStack stack, MultiBufferSource source, int packedLight, float red, float green, float blue) {
        Direction direction = entity.getPortalDirection();
        if (direction != null) {

            stack.pushPose();

//            Direction facing = entity.getPortalFacing();
//            stack.translate(0, -1, 0);
//
//            Direction.Axis axis = facing.getAxis();
//
//            float zRot = 0;
//            float yRot = 0;
//            float xRot = 0;
//
//            float width = getWidth(entity);
//            float height = getHeight(entity);
//
//            if (direction.getAxis().isVertical()) {
//                if (axis.equals(Direction.Axis.Z)) {
//                    stack.scale(entity.getSize(), 1, height);
//                    zRot = 180;
//                    yRot = 180;
//                    xRot = 90;
//                    stack.translate(0, 1.1, -1);
//                }
//                if (axis.equals(Direction.Axis.X)) {
//                    stack.scale(height, 1, entity.getSize());
//                    xRot = 0;
//                    yRot = 270;
//                    zRot = 90;
//                    stack.translate(-1, 1.1, 0);
//                }
//            } else {
//                stack.scale(entity.getSize(), height, entity.getSize());
//            }
//            stack.mulPose(Axis.XN.rotationDegrees(xRot));
//            stack.mulPose(Axis.ZN.rotationDegrees(zRot));
//            stack.mulPose(Axis.YN.rotationDegrees(direction.getAxis().isVertical() ? yRot : entity.getYRot()));
            float width = getWidth(entity);
            float height = entity.getSize() > 2 ? getWidth(entity) : 1;

            Matrix4f matrix4f = stack.last().pose();
            Matrix3f matrix3f = stack.last().normal();
            VertexConsumer consumer = source.getBuffer(RenderType.entitySmoothCutout(getTextureLocation(entity)));

         //   drawShapedVertex(stack, -width, -height, width, height, red, green, blue, 1, 0, 0, 0, 0, consumer, entity.getShape());

            GuiHelper.renderVertexes(matrix4f, matrix3f, consumer, -width, width, -height, height, 0.01f, 0.01f,
                    red, green, blue, 1,
                    0, 0, 0.5f, 1,
                    OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,
                    0, 1, 0);
            GuiHelper.renderVertexes(matrix4f, matrix3f, consumer, -width, width, height, -height, -0.01f, -0.01f,
                    red, green, blue, 1,
                    0, 0, 0.5f, 1,
                    OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,
                    0, 1, 0);

            stack.popPose();
        }
    }

    public ResourceLocation getTextureLocation(PortalEntity entity) {
        int frame = (entity.tickCount / 4) % 8;
        return PGHelper.id("textures/entity/portal_" + frame + ".png");
    }

    @Override
    public void renderInGui(int x, int y, int width, int height, ItemStack stack, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick, int red, int green, int blue) {
        float r = red / 255f;
        float g = green / 255f;
        float b = blue / 255f;
        graphics.setColor(r, g, b, 1);
        graphics.blit(PGHelper.id("textures/entity/portal.png"), x, y, 0, 0, width, height, width, height);
        graphics.setColor(1, 1, 1, 1);
    }
}
