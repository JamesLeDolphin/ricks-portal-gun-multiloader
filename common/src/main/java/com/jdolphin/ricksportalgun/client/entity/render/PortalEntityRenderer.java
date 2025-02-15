package com.jdolphin.ricksportalgun.client.entity.render;

import com.jdolphin.ricksportalgun.client.entity.PortalEntityRenderState;
import com.jdolphin.ricksportalgun.client.entity.model.PortalEntityModel;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.util.helper.Helper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;


public class PortalEntityRenderer extends EntityRenderer<PortalEntity, PortalEntityRenderState> {
    public static final ResourceLocation PORTAL_TEXTURE = Helper.createLocation("textures/entity/portal.png");
    public PortalEntityModel model;
    public PortalEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new PortalEntityModel(pContext.bakeLayer(PortalEntityModel.LAYER_LOCATION));
    }

    @Override
    public @NotNull PortalEntityRenderState createRenderState() {
        return new PortalEntityRenderState();
    }

    public void extractRenderState(PortalEntity portal, PortalEntityRenderState state, float pPartialTick) {
        super.extractRenderState(portal, state, pPartialTick);
        state.rgb = portal.getColor();
        state.yRot = portal.getYRot();
        state.isNew = !portal.exists();
        state.closing = portal.tickCount > 9 * 20;
        state.opening = portal.tickCount < 20;
        state.direction = portal.getPortalDirection();
        state.facing = portal.getPortalFacing();
        state.width = portal.getSize();
    }

    protected void openAnimation(PortalEntityRenderState state, PoseStack stack) {
        float f;
        if (state.isNew && state.opening) {
            f = Mth.lerp(state.ageInTicks / 20, 0.0f, 1.0f);
            f = Mth.clamp(f, 0.0f, 1.0f);
            f *= f;
            f *= f;
            stack.scale(f, f, f);
        }
        if (state.closing) {
            f = Mth.lerp(state.ageInTicks / 20, 1.0f, 0.0f);
            f = Mth.clamp(f, 0.0f, 1.0f);
            f *= f;
            f *= f;
            stack.scale(f, f, f);
        }
    }

    @Override
    public void render(@NotNull PortalEntityRenderState state, PoseStack stack, MultiBufferSource source, int pPackedLight) {
        stack.pushPose();
        openAnimation(state, stack);
        Direction direction = state.direction;
        Direction facing = state.facing;
        stack.translate(0, -1, 0);
        if (direction != null) {
            Direction.Axis axis = facing.getAxis();

        float zRot = 0;
        float yRot = 0;
        float xRot = 0;
           if (direction.getAxis().isVertical()) {
               if (axis.equals(Direction.Axis.Z)) {
                   stack.scale(state.width, 1, 1);
                   zRot = 180;
                   yRot = 180;
                   xRot = 90;
                   stack.translate(0, 1.1, -1);
               }
               if (axis.equals(Direction.Axis.X)) {
                   stack.scale(1, 1,  state.width);
                   xRot = 0;
                   yRot = 270;
                   zRot = 90;
                   stack.translate(-1, 1.1, 0);
               }
           } else {
               stack.scale(state.width, 1, state.width);
           }
            stack.mulPose(Axis.XN.rotationDegrees(xRot));
            stack.mulPose(Axis.ZN.rotationDegrees(zRot));
            stack.mulPose(Axis.YN.rotationDegrees(direction.getAxis().isVertical() ? yRot : state.yRot));
        }

        VertexConsumer vertexconsumer = source.getBuffer(RenderType.entityTranslucent(PORTAL_TEXTURE));

        this.model.renderToBuffer(stack, vertexconsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, state.rgb);

        stack.popPose();
        super.render(state, stack, source, pPackedLight);
    }
}