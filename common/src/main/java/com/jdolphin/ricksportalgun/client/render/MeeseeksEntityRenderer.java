package com.jdolphin.ricksportalgun.client.render;

import com.jdolphin.ricksportalgun.common.entity.MeeseeksEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class MeeseeksEntityRenderer extends EntityRenderer<MeeseeksEntity> {


    public MeeseeksEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(MeeseeksEntity entity) {
        return null;
    }
}
