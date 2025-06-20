package com.jdolphin.ricksportalgun.client.entity.render;

import com.jdolphin.ricksportalgun.client.entity.PortalEntityRenderState;
import com.jdolphin.ricksportalgun.client.entity.model.PortalEntityModel;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
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
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class PortalEntityRenderer extends EntityRenderer<PortalEntity, PortalEntityRenderState> {
    public static final ResourceLocation PORTAL_TEXTURE = PGHelper.createLocation("textures/entity/portal.png");
    public PortalEntityModel model;
    private static int textureFrame = 0;
    private static final int frames = 8;
    private final List<String> names = List.of(new String[]{"_jeb", "rainbow", "rgb", "colourful", "colorful"});
    private static int tickTimer = 0;

    public PortalEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new PortalEntityModel(pContext.bakeLayer(PortalEntityModel.LAYER_LOCATION));
    }

    @Override
    public @NotNull PortalEntityRenderState createRenderState() {
        return new PortalEntityRenderState();
    }

    public ResourceLocation getPortalTexture(int i) {
        return PGHelper.createLocation("textures/entity/portal_" + i + ".png");
    }

    public void extractRenderState(PortalEntity portal, PortalEntityRenderState state, float pPartialTick) {
        super.extractRenderState(portal, state, pPartialTick);
        state.rgb = portal.getColor();
        state.yRot = portal.getYRot();
        state.isNew = !portal.exists();
        state.closing = portal.tickCount > portal.getLifetime() * 0.9;
        state.opening = portal.tickCount < portal.getLifetime() * 0.1;
        state.direction = portal.getPortalDirection();
        state.facing = portal.getPortalFacing();
        state.width = portal.getSize();
        state.name = portal.getName();
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
            f = Mth.clamp(f, 1.0f, 0.0f);
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

            float height = state.width > 2 ? state.width / 2 : 1;
           if (direction.getAxis().isVertical()) {
               if (axis.equals(Direction.Axis.Z)) {
                   stack.scale(state.width, 1, height);
                   zRot = 180;
                   yRot = 180;
                   xRot = 90;
                   stack.translate(0, 1.1, -1);
               }
               if (axis.equals(Direction.Axis.X)) {
                   stack.scale(height, 1,  state.width);
                   xRot = 0;
                   yRot = 270;
                   zRot = 90;
                   stack.translate(-1, 1.1, 0);
               }
           } else {
               stack.scale(state.width, height, state.width);
           }
            stack.mulPose(Axis.XN.rotationDegrees(xRot));
            stack.mulPose(Axis.ZN.rotationDegrees(zRot));
            stack.mulPose(Axis.YN.rotationDegrees(direction.getAxis().isVertical() ? yRot : state.yRot));
        }

        VertexConsumer consumer = source.getBuffer(RenderType.entityTranslucent(getPortalTexture(textureFrame)));
        int i = state.rgb;
        if (state.name != null && names.contains(state.name.getString().toLowerCase())) {
            int j = 25;
            int k = Mth.floor(state.ageInTicks);
            int l = k / 25;
            int i1 = DyeColor.values().length;
            int j1 = l % i1;
            int k1 = (l + 1) % i1;
            float f = ((float)(k % 25) + Mth.frac(state.ageInTicks)) / 25.0F;
            int l1 = Sheep.getColor(DyeColor.byId(j1));
            int i2 = Sheep.getColor(DyeColor.byId(k1));
            i = ARGB.lerp(f, l1, i2);
        }
        this.model.renderToBuffer(stack, consumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, i);

        stack.popPose();
        super.render(state, stack, source, pPackedLight);
    }


    public static void tickTexture() {
        tickTimer++;

        if (tickTimer >= 4) {
            tickTimer = 0;
            textureFrame = (textureFrame + 1) % frames;
        }
    }
}