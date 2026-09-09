package com.jdolphin.ricksportalgun.client.entity.render.type;

import com.jdolphin.ricksportalgun.client.entity.render.shape.AbstractPortalShapeRenderer;
import com.jdolphin.ricksportalgun.common.customization.type.PortalType;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class PentagramPortalTypeRenderer extends AbstractPortalTypeRenderer {

    public PentagramPortalTypeRenderer(PortalType type) {
        super(type);
    }

    @Override
    public void renderPortal(PortalEntity entity, AbstractPortalShapeRenderer shapeRenderer, float yaw, float partialTick, PoseStack stack, MultiBufferSource source, int packedLight, float red, float green, float blue) {
        stack.pushPose();
        float width = getWidth(entity);

        Matrix4f matrix4f = stack.last().pose();
        Matrix3f matrix3f = stack.last().normal();
        VertexConsumer consumer = source.getBuffer(RenderType.entityTranslucentEmissive(getTextureLocation(entity)));

        stack.mulPose(Axis.ZN.rotationDegrees((entity.tickCount % 360)));
        GuiHelper.renderVertexes(matrix4f, matrix3f, consumer, -width, width, -width, width, 0.01f, 0.01f,
                red, green, blue, 1,
                0, 0, 1, 1,
                OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,
                0, 1, 0);
        GuiHelper.renderVertexes(matrix4f, matrix3f, consumer, width, -width, -width, width, -0.01f, -0.01f,
                red, green, blue, 1,
                0, 0, 1, 1,
                OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,
                0, -1, 0);

        stack.popPose();
    }

    public ResourceLocation getTextureLocation(PortalEntity entity) {
        return PGHelper.id("textures/entity/pentagram.png");
    }

    @Override
    public void renderInGui(int x, int y, int width, int height, AbstractPortalShapeRenderer shape, ItemStack stack, GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick, int red, int green, int blue) {
        float r = red / 255f;
        float g = green / 255f;
        float b = blue / 255f;

        Player player = Minecraft.getInstance().player;

        graphics.setColor(r, g, b, 1);
        RenderSystem.enableBlend();
        graphics.blit(getTextureLocation(null), x, y, 0, 0, width, height / 2, width, height / 2);
        RenderSystem.disableBlend();
        graphics.setColor(1, 1, 1, 1);
    }
}