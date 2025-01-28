package com.jdolphin.ricksportalgun.client.entity.render;

import com.jdolphin.ricksportalgun.client.entity.PortalEntityRenderState;
import com.jdolphin.ricksportalgun.client.entity.model.PortalEntityModel;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
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
    }

    protected void scale(PortalEntityRenderState state, PoseStack stack) {
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

        scale(state, stack);

        stack.translate(0, -1, 0);
        stack.mulPose(Axis.YN.rotationDegrees(state.yRot));
        VertexConsumer vertexconsumer = source.getBuffer(RenderType.entityTranslucent(PORTAL_TEXTURE));

        this.model.renderToBuffer(stack, vertexconsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, state.rgb);

        stack.popPose();
        super.render(state, stack, source, pPackedLight);
    }
}