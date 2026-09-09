package com.jdolphin.ricksportalgun.client.entity.render.type;

import com.jdolphin.ricksportalgun.client.entity.render.shape.AbstractPortalShapeRenderer;
import com.jdolphin.ricksportalgun.common.customization.type.PortalType;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class EndPortalTypeRenderer extends AbstractPortalTypeRenderer {

    public EndPortalTypeRenderer(PortalType type) {
        super(type);
    }

    @Override
    public ResourceLocation getTextureLocation(PortalEntity portal) {
        return null;
    }

    @Override
    public void renderInGui(int x, int y, int width, int height, AbstractPortalShapeRenderer shape, ItemStack stack, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick, int red, int green, int blue) {

        shape.renderInGui(x, y, 0, width, height, graphics, pMouseX, pMouseY, pPartialTick, red, green, blue, 255,
                0, 0, 0, 0, RenderType.endPortal(), bufferBuilder -> RenderType.endPortal().end(bufferBuilder, RenderSystem.getVertexSorting()));

        shape.renderInGui(x, y, 0, width, height, graphics, pMouseX, pMouseY, pPartialTick, red, green, blue, 100,
                0, 0, 0, 0, RenderType.guiOverlay(), bufferBuilder -> RenderType.guiOverlay().end(bufferBuilder, RenderSystem.getVertexSorting()));
    }

    @Override
    public void renderPortal(PortalEntity entity, AbstractPortalShapeRenderer shapeRenderer, float yaw, float partialTick, PoseStack stack, MultiBufferSource source, int packedLight, float red, float green, float blue) {

        float width = getWidth(entity);
        float height = entity.getSize() > 2 ? width : 1;

        RenderType renderType = RenderType.endPortal();
        float progress = entity.tickCount * 0.001F % 1.0F; //Scrolls the image if shaders are enabled, otherwise does visually nothing
        boolean iris = PGHelper.hasIris() && net.irisshaders.iris.Iris.getCurrentPack().isPresent();
        if (iris) {
            renderType = RenderType.entitySolid(TheEndPortalRenderer.END_PORTAL_LOCATION);
        }
        //End portal
        VertexConsumer consumer = source.getBuffer(renderType);
        float f = entity.tickCount + partialTick;
        float f1 = (float) Math.abs((Math.cos(f / 10) + 1) / 2);
        shapeRenderer.renderInLevel(stack, -width, -height, width, height, 0, 0, red, green, blue, f1, 0, 0 + progress, 0.5f, 0.2f + progress, consumer);
        shapeRenderer.renderInLevel(stack, width, -height, -width, height, 0, 0, red, green, blue, f1, 0, 0 + progress, 0.5f, 0.2f + progress, consumer);

        if (!iris) {
            //Color overlay - We only render when shaders are disabled, otherwise the portal would just glow that color
            VertexConsumer consumer1 = source.getBuffer(RenderType.translucent());
            shapeRenderer.renderInLevel(stack, -width, -height, width, height, 0.007f, 0.007f, red, green, blue, 0.45f, 0, 0, 0, 0, consumer1);
            shapeRenderer.renderInLevel(stack, width, -height, -width, height, -0.005f, -0.005f, red, green, blue, 0.45f, 0, 0, 0, 0, consumer1);
        }
    }
}