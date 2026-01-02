package com.jdolphin.ricksportalgun.client.render;

import com.jdolphin.ricksportalgun.client.init.PGPortalShapeRenderers;
import com.jdolphin.ricksportalgun.client.init.PGPortalTypeRenderers;
import com.jdolphin.ricksportalgun.client.render.portal.AbstractPortalTypeRenderer;
import com.jdolphin.ricksportalgun.client.render.portal.shape.AbstractPortalShapeRenderer;
import com.jdolphin.ricksportalgun.common.customization.PortalType;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;

import java.util.List;


public class PortalEntityRenderer extends EntityRenderer<PortalEntity> {
    public static final ResourceLocation PORTAL_TEXTURE = PGHelper.id("textures/entity/portal.png");
    private final List<String> names = List.of(new String[]{"jeb_", "rainbow", "rgb", "colourful", "colorful"});

    public PortalEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(PortalEntity entity) {
        PortalType type = entity.getPortalType();
        return PGPortalTypeRenderers.getRenderer(type).getTextureLocation(entity);
    }

    @Override
    public void render(PortalEntity entity, float yaw, float partialTick, PoseStack stack, MultiBufferSource source, int packedLight) {
        PortalType type = entity.getPortalType();
        AbstractPortalTypeRenderer typeRenderer = PGPortalTypeRenderers.getRenderer(type);
        AbstractPortalShapeRenderer shapeRenderer = PGPortalShapeRenderers.RENDERER_MAP.get(entity.getShape());
        if (typeRenderer != null && shapeRenderer != null) {
            typeRenderer.openAnimation(entity, stack, partialTick, packedLight);
            typeRenderer.closeAnimation(entity, stack, partialTick, packedLight);

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
            typeRenderer.renderPortal(entity, shapeRenderer, yaw, partialTick, stack, source, packedLight, r, g, b);

            super.render(entity, yaw, partialTick, stack, source, LightTexture.FULL_BRIGHT);
        }
    }
}