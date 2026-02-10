package com.jdolphin.ricksportalgun.client.render;

import com.jdolphin.ricksportalgun.common.blockentity.PortalFluidStorageBlockEntity;
import com.jdolphin.ricksportalgun.common.comp.sodium.SodiumCompat;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class PortalFluidTankBlockEntityRenderer implements BlockEntityRenderer<PortalFluidStorageBlockEntity> {
    private TextureAtlasSprite sideSprite;
    private TextureAtlasSprite topSprite;

    public PortalFluidTankBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(PortalFluidStorageBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (sideSprite == null) {
            sideSprite = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(PGHelper.id("block/portal_fluid_flow"));
        } else if (PGHelper.hasSodium()) {
            SodiumCompat.markSpriteActive(sideSprite);
        }
        if (topSprite == null) {
            topSprite = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(PGHelper.id("block/portal_fluid_still"));
        } else if (PGHelper.hasSodium()) {
            SodiumCompat.markSpriteActive(topSprite);
        }

        stack.pushPose();
        int fluid = blockEntity.getAmount();
        int max = blockEntity.getMaxAmount();
        float amount = Mth.clamp((float) fluid / max, 0.1f, 0.9f);
        float vHeight = ((float) fluid / max) * 16f;

        if (fluid > 0) {
            Matrix4f matrix4f = stack.last().pose();
            Matrix3f matrix3f = stack.last().normal();

            GuiHelper.renderVertex(buffer.getBuffer(RenderType.entitySolid(topSprite.atlasLocation())), matrix4f, matrix3f,
                    0.1f, amount, 0.9f, 1, 1, 1, 1, topSprite.getU0(), topSprite.getV1(), packedOverlay, packedLight, 0, 1, 0);

            GuiHelper.renderVertex(buffer.getBuffer(RenderType.entitySolid(topSprite.atlasLocation())), matrix4f, matrix3f,
                    0.9f, amount, 0.9f, 1, 1, 1, 1, topSprite.getU1(), topSprite.getV1(), packedOverlay, packedLight, 0, 1, 0);

            GuiHelper.renderVertex(buffer.getBuffer(RenderType.entitySolid(topSprite.atlasLocation())), matrix4f, matrix3f,
                    0.9f, amount, 0.1f, 1, 1, 1, 1, topSprite.getU1(), topSprite.getV0(), packedOverlay, packedLight, 0, 1, 0);

            GuiHelper.renderVertex(buffer.getBuffer(RenderType.entitySolid(topSprite.atlasLocation())), matrix4f, matrix3f,
                    0.1f, amount, 0.1f, 1, 1, 1, 1, topSprite.getU0(), topSprite.getV0(), packedOverlay, packedLight, 0, 1, 0);

            //Sides
            renderVertexes(matrix4f, matrix3f, buffer.getBuffer(RenderType.entitySolid(sideSprite.atlasLocation())), 0.9f, 0.1f, 0.01f, amount, 0.1f, 0.1f,
                    1, 1, 1, 1,
                    sideSprite.getU(0), sideSprite.getV(0), sideSprite.getU(16), sideSprite.getV(vHeight), packedOverlay, packedLight, 0, 1, 0);

            renderVertexes(matrix4f, matrix3f, buffer.getBuffer(RenderType.entitySolid(sideSprite.atlasLocation())), 0.1f, 0.9f, 0.01f, amount, 0.9f, 0.9f,
                    1, 1, 1, 1,
                    sideSprite.getU(0), sideSprite.getV(0), sideSprite.getU(16), sideSprite.getV(vHeight), packedOverlay, packedLight, 0, 1, 0);

            renderVertexes(matrix4f, matrix3f, buffer.getBuffer(RenderType.entitySolid(sideSprite.atlasLocation())), 0.1f, 0.1f, 0.01f, amount, 0.1f, 0.9f,
                    1, 1, 1, 1,
                    sideSprite.getU(0), sideSprite.getV(0), sideSprite.getU(16), sideSprite.getV(vHeight), packedOverlay, packedLight, 0, 1, 0);

            renderVertexes(matrix4f, matrix3f, buffer.getBuffer(RenderType.entitySolid(sideSprite.atlasLocation())), 0.9f, 0.9f, 0.01f, amount, 0.9f, 0.1f,
                    1, 1, 1, 1,
                    sideSprite.getU(0), sideSprite.getV(0), sideSprite.getU(16), sideSprite.getV(vHeight), packedOverlay, packedLight, 0, 1, 0);
        }
        stack.popPose();
    }

    private void renderVertexes(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer consumer, float x1, float x2, float y1, float y2, float z1, float z2,
                                      float red, float green, float blue, float alpha,
                                      float u1, float v1, float u2, float v2,
                                      int overlay, int light,
                                      float normalX, float normalY, float normalZ) {

        GuiHelper.renderVertex(consumer, matrix4f, matrix3f, x1, y1, z1, red, green, blue, alpha, u1, v2, overlay, light, normalX, normalY, normalZ);
        GuiHelper.renderVertex(consumer, matrix4f, matrix3f, x2, y1, z2, red, green, blue, alpha, u2, v2, overlay, light, normalX, normalY, normalZ);
        GuiHelper.renderVertex(consumer, matrix4f, matrix3f, x2, y2, z2, red, green, blue, alpha, u2, v1, overlay, light, normalX, normalY, normalZ);
        GuiHelper.renderVertex(consumer, matrix4f, matrix3f, x1, y2, z1, red, green, blue, alpha, u1, v1, overlay, light, normalX, normalY, normalZ);
    }
}
