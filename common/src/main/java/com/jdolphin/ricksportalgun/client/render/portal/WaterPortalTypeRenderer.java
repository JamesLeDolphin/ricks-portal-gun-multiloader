package com.jdolphin.ricksportalgun.client.render.portal;

import com.jdolphin.ricksportalgun.common.customization.PortalType;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;

public class WaterPortalTypeRenderer extends PortalTypeRenderer {
    private TextureAtlasSprite sprite;

    public WaterPortalTypeRenderer(PortalType type) {
        super(type);
    }

    @Override
    public ResourceLocation getTextureLocation(PortalEntity portal) {
        return PGHelper.vanilla("block/water_flow");
    }

    @Override
    public void renderInGui(int x, int y, int width, int height, ItemStack stack, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick, int red, int green, int blue) {
        if (sprite == null) {
            sprite = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(getTextureLocation(null));
        }
        int x2 = x + width;
        int y2 = y + height;
        graphics.blit(x, y, 0, width, height, sprite, red / 255f, green / 255f, blue / 255f, 1);

        //RenderSystem.enableBlend();
        //RenderSystem.setShader(GameRenderer::getPositionColorShader);
        //Matrix4f matrix4f = graphics.pose().last().pose();
        //BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        //bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        //bufferbuilder.vertex(matrix4f, x, y, 0.001f).color(red, green, blue, 128).endVertex();
        //bufferbuilder.vertex(matrix4f, x, y2, 0.001f).color(red, green, blue, 128).endVertex();
        //bufferbuilder.vertex(matrix4f, x2, y2, 0.001f).color(red, green, blue, 128).endVertex();
        //bufferbuilder.vertex(matrix4f, x2, y, 0.001f).color(red, green, blue, 128).endVertex();
        //BufferUploader.drawWithShader(bufferbuilder.end());
        //RenderSystem.disableBlend();
    }

    @Override
    public void renderPortal(PortalEntity entity, float yaw, float partialTick, PoseStack stack, MultiBufferSource source, int packedLight, float red, float green, float blue) {
        if (sprite == null) {
            sprite = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(getTextureLocation(entity));
        }

        stack.pushPose();
        stack.mulPose(Axis.YN.rotationDegrees(entity.getYRot()));
        VertexConsumer consumer1 = source.getBuffer(RenderType.entitySmoothCutout(sprite.atlasLocation()));
        float width = (entity.getSize() / 3) * 1.5f;
        float height = entity.getSize() > 2 ? width : 1;

        drawShapedVertex(stack, -width, -height, width, height, red, green, blue, 1,
                sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), consumer1, entity.getShape());

       // GuiHelper.renderVertexes(stack.last().pose(), stack.last().normal(), consumer1,
       //         -width, width, -height, height, 0f, 0f, red, green, blue, 1,
       //         sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(),
       //         OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,
       //         0, 1, 0);
        stack.popPose();
    }
}
