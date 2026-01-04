package com.jdolphin.ricksportalgun.client.render.portal.shape;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;

import java.util.function.Consumer;

public abstract class AbstractPortalShapeRenderer {

    public abstract void renderInGui(int x, int y, int z, int width, int height, GuiGraphics graphics,
                                     int pMouseX, int pMouseY, float pPartialTick, int red, int green, int blue, int alpha, float u1, float v1, float u2, float v2, RenderType type, Consumer<BufferBuilder> consumer);

    public abstract void renderInLevel(PoseStack stack, float x1, float y1, float x2, float y2, float z1, float z2, float red, float green, float blue, float alpha, float u1, float v1, float u2, float v2,
                                       VertexConsumer consumer);
}
