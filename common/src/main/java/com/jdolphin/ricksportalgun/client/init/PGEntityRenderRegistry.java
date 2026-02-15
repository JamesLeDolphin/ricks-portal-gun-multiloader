package com.jdolphin.ricksportalgun.client.init;

import com.jdolphin.ricksportalgun.client.model.PortalEntityModel;
import com.jdolphin.ricksportalgun.client.render.MeeseeksEntityRenderer;
import com.jdolphin.ricksportalgun.client.render.portal.DinoPortalRenderer;
import com.jdolphin.ricksportalgun.client.render.portal.PortalEntityRenderer;
import com.jdolphin.ricksportalgun.common.comp.immersive_portals.ImmersivePortalsHandler;
import com.jdolphin.ricksportalgun.common.init.PGEntities;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("ALL")
public class PGEntityRenderRegistry {
    public static final Map<EntityType, EntityRendererProvider> RENDERERS = new HashMap<>();
    public static final Map<ModelLayerLocation, Supplier<LayerDefinition>> BODY_LAYERS = new HashMap<>();

    public static void initLayers() {
         registerLayer(PortalEntityModel.LAYER_LOCATION, PortalEntityModel::createBodyLayer);
    }

    public static void initRenderers() {
        registerRenderers(PGEntities.PORTAL, PortalEntityRenderer::new);
        registerRenderers(PGEntities.MEESEEKS, MeeseeksEntityRenderer::new);
        registerRenderers(PGEntities.EXPLOSIVE_ITEM, ItemEntityRenderer::new);

        if (PGHelper.hasImmersivePortals()) {
            registerRenderers(ImmersivePortalsHandler.ENTITY_TYPE, DinoPortalRenderer::new);

        }
    }

    private static void registerLayer(ModelLayerLocation layerLocation, Supplier<LayerDefinition> supplier) {
        BODY_LAYERS.put(layerLocation, supplier);
    }

    private static <E extends Entity> void registerRenderers(EntityType<? extends E> entityType, EntityRendererProvider<E> entityRendererFactory) {
        RENDERERS.put(entityType, entityRendererFactory);
    }
}
