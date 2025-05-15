package com.jdolphin.ricksportalgun.client;

import com.jdolphin.ricksportalgun.client.entity.model.PortalEntityModel;
import com.jdolphin.ricksportalgun.client.entity.render.PortalEntityRenderer;
import com.jdolphin.ricksportalgun.client.screen.PortalDispenserScreen;
import com.jdolphin.ricksportalgun.common.init.*;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBOpenCoordGuiPacket;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.tint.PortalColourTint;
import com.jdolphin.ricksportalgun.common.util.tint.PrimaryDyeTint;
import com.jdolphin.ricksportalgun.common.util.tint.SecondaryDyeTint;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;

public class RicksPortalGunFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(PGEntities.PORTAL, PortalEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(PortalEntityModel.LAYER_LOCATION, PortalEntityModel::createBodyLayer);
        MenuScreens.register(PGMenuTypes.PORTAL_DISPENSER, PortalDispenserScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlock(PGBlocks.GUN_WORKBENCH, RenderType.cutout());

        ItemTintSources.ID_MAPPER.put(PGHelper.createLocation("primary_dye"), PrimaryDyeTint.CODEC);
        ItemTintSources.ID_MAPPER.put(PGHelper.createLocation("secondary_dye"), SecondaryDyeTint.CODEC);
        ItemTintSources.ID_MAPPER.put(PGHelper.createLocation("portal_color"), PortalColourTint.CODEC);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (PGKeyBinds.KEY_PORTAL_MENU.isDown() && client.player != null && client.player.getMainHandItem().is(PGTags.Items.PORTAL_GUNS)) {
                SBOpenCoordGuiPacket packet = new SBOpenCoordGuiPacket();
                ClientPlayNetworking.send(packet);
            }
        });

        FabricPackets.registerS2CPackets();
    }
}
