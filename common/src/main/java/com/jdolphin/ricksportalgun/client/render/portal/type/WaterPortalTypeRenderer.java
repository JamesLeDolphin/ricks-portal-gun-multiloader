package com.jdolphin.ricksportalgun.client.render.portal.type;

import com.jdolphin.ricksportalgun.client.render.portal.shape.AbstractPortalShapeRenderer;
import com.jdolphin.ricksportalgun.common.comp.sodium.SodiumCompat;
import com.jdolphin.ricksportalgun.common.customization.type.PortalType;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
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

public class WaterPortalTypeRenderer extends AbstractPortalTypeRenderer {
    private TextureAtlasSprite sprite;

    public WaterPortalTypeRenderer(PortalType type) {
        super(type);
    }

    @Override
    public ResourceLocation getTextureLocation(PortalEntity portal) {
        return PGHelper.vanilla("block/water_flow");
    }

    @Override
    public void renderInGui(int x, int y, int width, int height, AbstractPortalShapeRenderer shape, ItemStack stack, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick, int red, int green, int blue) {
        if (sprite == null) {
            sprite = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(getTextureLocation(null));
        } else {
            if (PGHelper.hasSodium()) {
                SodiumCompat.markSpriteActive(sprite);
            }
            RenderSystem.setShaderTexture(0, sprite.atlasLocation());
            shape.renderInGui(x, y, 0, width, height, graphics, pMouseX, pMouseY, pPartialTick, red, green, blue, 255,
                    sprite.getU0(), sprite.getV0(), sprite.getU1() , sprite.getV1(), RenderType.entityCutoutNoCull(sprite.atlasLocation()),
                    bufferBuilder -> BufferUploader.drawWithShader(bufferBuilder.end()));
        }
    }

    @Override
    public void renderPortal(PortalEntity entity, AbstractPortalShapeRenderer shapeRenderer, float yaw, float partialTick, PoseStack stack, MultiBufferSource source, int packedLight, float red, float green, float blue) {
        if (sprite == null) {
            sprite = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(getTextureLocation(entity));
        } else {
            if (PGHelper.hasSodium()) {
                SodiumCompat.markSpriteActive(sprite);
            }
            stack.pushPose();
            stack.mulPose(Axis.YN.rotationDegrees(entity.getYRot()));
            VertexConsumer consumer1 = source.getBuffer(RenderType.entityTranslucent(sprite.atlasLocation()));
            float width = (entity.getSize() / 3) * 1.5f;
            float height = entity.getSize() > 2 ? width : 1;

            shapeRenderer.renderInLevel(stack, -width, -height, width, height, 0, 0, red, green, blue, 1,
                    sprite.getU(0), sprite.getV(0), sprite.getU(16), sprite.getV(16), consumer1);

            stack.popPose();
        }
    }
}
