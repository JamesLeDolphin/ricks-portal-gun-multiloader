package com.jdolphin.ricksportalgun.client.render.portal;

import com.jdolphin.ricksportalgun.common.customization.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;

public class WaterPortalTypeRenderer extends PortalTypeRenderer {
    private TextureAtlasSprite sprite;

    public WaterPortalTypeRenderer() {
    }

    @Override
    public ResourceLocation getTextureLocation(PortalEntity portal) {
        return PGHelper.vanilla("block/water_flow");
    }

    @Override
    public void renderInGui(ItemStack stack, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick, PortalGunStyle style) {

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

        renderVertexes(stack.last().pose(), stack.last().normal(), consumer1,
                -width, width, -height, height, 0f, 0f, red, green, blue, 1,
                sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(),
                OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,
                0, 1, 0);
        stack.popPose();
    }
}
