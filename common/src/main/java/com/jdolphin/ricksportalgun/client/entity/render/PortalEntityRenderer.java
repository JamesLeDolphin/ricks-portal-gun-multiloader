package com.jdolphin.ricksportalgun.client.entity.render;

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
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class PortalEntityRenderer extends EntityRenderer<PortalEntity> {
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
    public ResourceLocation getTextureLocation(PortalEntity entity) {
        return PORTAL_TEXTURE;
    }

    public ResourceLocation getPortalTexture(int i) {
        return PGHelper.createLocation("textures/entity/portal_" + i + ".png");
    }

    protected void openAnimation(PortalEntity state, PoseStack stack) {
        float f;
        if (!state.exists() && state.tickCount < state.getLifetime() * 0.1) {
            f = Mth.lerp((float) state.tickCount / 20, 0.0f, 1.0f);
            f = Mth.clamp(f, 0.0f, 1.0f);
            f *= f;
            f *= f;
            stack.scale(f, f, f);
        }
        if (state.tickCount > state.lifetime * 0.9) {
            f = Mth.lerp((float) state.tickCount / 20, 1.0f, 0.0f);
            f = Mth.clamp(f, 1.0f, 0.0f);
            f *= f;
            f *= f;
            stack.scale(f, f, f);
        }
    }

    @Override
    public void render(@NotNull PortalEntity entity, float yaw, float partialTick, PoseStack stack, MultiBufferSource source, int packedLight) {
        stack.pushPose();
        openAnimation(entity, stack);
        Direction direction = entity.getPortalDirection();
        Direction facing = entity.getPortalFacing();
        stack.translate(0, -1, 0);
        if (direction != null) {
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
                   stack.scale(height, 1,  entity.getSize());
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
        }

        VertexConsumer consumer = source.getBuffer(RenderType.entityTranslucent(getPortalTexture(textureFrame)));
        int i = entity.getColor();
        entity.getName();
        if (names.contains(entity.getName().getString().toLowerCase())) {
            int j = 25;
            int k = Mth.floor(entity.tickCount);
            int l = k / 25;
            int i1 = DyeColor.values().length;
            int j1 = l % i1;
            int k1 = (l + 1) % i1;
            float f = ((float)(k % 25) + Mth.frac(entity.tickCount)) / 25.0F;
            int l1 = Sheep.getColor(DyeColor.byId(j1));
            int i2 = Sheep.getColor(DyeColor.byId(k1));
            i = FastColor.ARGB32.lerp(f, l1, i2);
        }
        this.model.renderToBuffer(stack, consumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, i);

        stack.popPose();
        super.render(entity, yaw, partialTick, stack, source, packedLight);
    }


    public static void tickTexture() {
        tickTimer++;

        if (tickTimer >= 4) {
            tickTimer = 0;
            textureFrame = (textureFrame + 1) % frames;
        }
    }
}