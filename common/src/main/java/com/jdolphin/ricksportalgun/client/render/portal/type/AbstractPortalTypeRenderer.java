package com.jdolphin.ricksportalgun.client.render.portal.type;

import com.jdolphin.ricksportalgun.client.render.portal.shape.AbstractPortalShapeRenderer;
import com.jdolphin.ricksportalgun.common.customization.PortalType;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.joml.Math;

public abstract class AbstractPortalTypeRenderer {
    protected final PortalType type;

    public AbstractPortalTypeRenderer(PortalType type) {
        this.type = type;
    }

    public void openAnimation(PortalEntity entity, PoseStack stack, float delta, int packedLight) {
        float f = Mth.lerp((entity.getMaxLifetime() - entity.getLifetime()) / 20f, 0.0f, 1.0f);
        f = Mth.clamp(f, 0.0f, 1.0f);
        f *= f;
        f *= f;
        stack.scale(f, f, f);
    }

    public void closeAnimation(PortalEntity entity, PoseStack stack, float delta, int packedLight) {
        float f = (float) entity.getLifetime() / 20.0f;
        f = Mth.clamp(f, 0.0f, 1.0f);
        f = f * f * f * f;
        stack.scale(f, f, f);
    }

    public float getWidth(PortalEntity entity) {
        return (entity.getSize() / 3) * 1.5f;
    }

    public float getHeight(PortalEntity entity) {
        return Math.max(2, entity.getSize());
    }



    public abstract ResourceLocation getTextureLocation(PortalEntity portal);

    public abstract void renderInGui(int x, int y, int width, int height, ItemStack stack, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick, int red, int green, int blue);

    public abstract void renderPortal(PortalEntity entity, AbstractPortalShapeRenderer shapeRenderer, float yaw, float partialTick, PoseStack stack, MultiBufferSource source, int packedLight, float red, float green, float blue);
}
