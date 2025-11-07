package com.jdolphin.ricksportalgun.common.customization.portaltypes;

import com.jdolphin.ricksportalgun.common.customization.PGPortalType;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class DefaultPortalType extends PGPortalType {

    public DefaultPortalType() {
        super("default", true);

    }


    @Override
    public void renderPortal(PortalEntity entity, float yaw, float partialTick, PoseStack stack, MultiBufferSource source, int packedLight, float red, float green, float blue) {
        if (model != null) {
            Direction direction = entity.getPortalDirection();
            if (direction != null) {
                stack.pushPose();

                Direction facing = entity.getPortalFacing();
                stack.translate(0, -1, 0);

                Direction.Axis axis = facing.getAxis();

                float zRot = 0;
                float yRot = 0;
                float xRot = 0;

                float height = entity.getSize() > 2 ? entity.getSize() / 2 : 1;
                if (direction.getAxis().isVertical()) {
                    if (axis.equals(Direction.Axis.Z)) {
                        stack.scale(entity.getSize(), 1, height);
                        zRot = 180;
                        yRot = 180;
                        xRot = 90;
                        stack.translate(0, 1.1, -1);
                    }
                    if (axis.equals(Direction.Axis.X)) {
                        stack.scale(height, 1, entity.getSize());
                        xRot = 0;
                        yRot = 270;
                        zRot = 90;
                        stack.translate(-1, 1.1, 0);
                    }
                } else {
                    stack.scale(entity.getSize(), height, entity.getSize());
                }
                stack.mulPose(Axis.XN.rotationDegrees(xRot));
                stack.mulPose(Axis.ZN.rotationDegrees(zRot));
                stack.mulPose(Axis.YN.rotationDegrees(direction.getAxis().isVertical() ? yRot : entity.getYRot()));

                VertexConsumer consumer = source.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
                this.model.renderToBuffer(stack, consumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, red, green, blue, 1);

                stack.popPose();
            }
        }
    }

    public ResourceLocation getTextureLocation(PortalEntity entity) {
        int frame = (entity.tickCount / 5) % 8;
        return PGHelper.id("textures/entity/portal_" + frame + ".png");
    }
}
