package com.jdolphin.ricksportalgun.client.entity.render;

import com.jdolphin.ricksportalgun.client.entity.model.PortalEntityModel;
import com.jdolphin.ricksportalgun.common.customization.PGPortalType;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;

import java.util.List;


public class PortalEntityRenderer extends EntityRenderer<PortalEntity> {
    public static final ResourceLocation PORTAL_TEXTURE = PGHelper.id("textures/entity/portal.png");
    public PortalEntityModel model;
    private final List<String> names = List.of(new String[]{"_jeb", "rainbow", "rgb", "colourful", "colorful"});

    public PortalEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new PortalEntityModel(pContext.bakeLayer(PortalEntityModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(PortalEntity entity) {
        return PORTAL_TEXTURE;
    }

    protected void openAnimation(PortalEntity entity, PoseStack stack) {
        float f;
        if (!entity.exists() && entity.tickCount < entity.getLifetime() * 0.1) {
            f = Mth.lerp((float) entity.tickCount / 20, 0.0f, 1.0f);
            f = Mth.clamp(f, 0.0f, 1.0f);
            f *= f;
            f *= f;
            stack.scale(f, f, f);
        }
        if (entity.tickCount > entity.getLifetime() * 0.9) {
            f = Mth.lerp((float) entity.tickCount / 20, 1.0f, 0.0f);
            f = Mth.clamp(f, 1.0f, 0.0f);
            f *= f;
            f *= f;
            stack.scale(f, f, f);
        }
    }

    @Override
    public void render(PortalEntity entity, float yaw, float partialTick, PoseStack stack, MultiBufferSource source, int packedLight) {
        PGPortalType type = entity.getPortalType();
        if (type.getModel() == null && type.needsModel()) type.setModel(model);

        openAnimation(entity, stack);

        int color = entity.getColor();
        float r = FastColor.ARGB32.red(color) / 255f;
        float g = FastColor.ARGB32.green(color) / 255f;
        float b = FastColor.ARGB32.blue(color) / 255f;

        if (names.contains(entity.getName().getString().toLowerCase())) {
            int i = entity.tickCount / 25 + entity.getId();
            int j = DyeColor.values().length;
            int k = i % j;
            int l = (i + 1) % j;
            float f3 = ((float) (entity.tickCount % 25) + partialTick) / 25.0F;
            float[] afloat1 = Sheep.getColorArray(DyeColor.byId(k));
            float[] afloat2 = Sheep.getColorArray(DyeColor.byId(l));
            r = afloat1[0] * (1.0F - f3) + afloat2[0] * f3;
            g = afloat1[1] * (1.0F - f3) + afloat2[1] * f3;
            b = afloat1[2] * (1.0F - f3) + afloat2[2] * f3;
        }
        type.renderPortal(entity, yaw, partialTick, stack, source, packedLight, r, g, b);

        super.render(entity, yaw, partialTick, stack, source, LightTexture.FULL_BRIGHT);
    }
}