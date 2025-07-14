package com.jdolphin.ricksportalgun.client;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

public class PortalGunItemRenderer extends BlockEntityWithoutLevelRenderer {

    public PortalGunItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @ParametersAreNonnullByDefault
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource source, int light, int overlay) {
        ResourceLocation model = PortalGunItem.getPortalGunType(stack).model(); 
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(model, "hand");
        Minecraft.getInstance().getItemRenderer().getModel(stack,null, null, light);
    }
}
