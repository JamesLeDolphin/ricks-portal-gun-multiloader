package com.jdolphin.ricksportalgun.client;

import com.jdolphin.ricksportalgun.client.entity.model.PortalEntityModel;
import com.jdolphin.ricksportalgun.client.entity.render.PortalEntityRenderer;
import com.jdolphin.ricksportalgun.common.init.*;
import com.jdolphin.ricksportalgun.common.packets.SBOpenGuiPacket;
import com.jdolphin.ricksportalgun.common.platform.Services;
import com.jdolphin.ricksportalgun.common.util.PGPacketType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;

public class RicksPortalGunFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(PGEntities.PORTAL, PortalEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(PortalEntityModel.LAYER_LOCATION, PortalEntityModel::createBodyLayer);

        BlockRenderLayerMap.INSTANCE.putBlock(PGBlocks.GUN_WORKBENCH, RenderType.cutout());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (PGKeyBinds.KEY_PORTAL_MENU.isDown() && client.player != null && client.player.getMainHandItem().is(PGTags.Items.PORTAL_GUNS)) {
                SBOpenGuiPacket packet = new SBOpenGuiPacket(client.player.getStringUUID());
                ClientPlayNetworking.send(packet);
            }
        });

        FabricPackets.registerS2CPackets();
    }
}
