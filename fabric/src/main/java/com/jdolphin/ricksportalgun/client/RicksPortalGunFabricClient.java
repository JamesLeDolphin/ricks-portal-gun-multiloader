package com.jdolphin.ricksportalgun.client;

import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.client.entity.model.PortalEntityModel;
import com.jdolphin.ricksportalgun.client.entity.render.PortalEntityRenderer;
import com.jdolphin.ricksportalgun.client.init.PGItemTints;
import com.jdolphin.ricksportalgun.client.init.PGMenuScreens;
import com.jdolphin.ricksportalgun.common.config.PGCommonConfig;
import com.jdolphin.ricksportalgun.common.init.*;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBOpenCoordGuiPacket;
import fuzs.forgeconfigapiport.fabric.api.forge.v4.ForgeConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.fml.config.ModConfig;

public class RicksPortalGunFabricClient implements ClientModInitializer {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void onInitializeClient() {
        ForgeConfigRegistry.INSTANCE.register(PGConstants.MODID, ModConfig.Type.CLIENT, PGCommonConfig.SPEC, "ricksportalgun-client.toml");

        EntityRendererRegistry.register(PGEntities.PORTAL, PortalEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(PortalEntityModel.LAYER_LOCATION, PortalEntityModel::createBodyLayer);

        PGMenuScreens.ALL.forEach((type, func) -> {
            MenuScreens.ScreenConstructor constructor = func::apply;
            MenuScreens.register(type, constructor);
        });

        BlockRenderLayerMap.INSTANCE.putBlock(PGBlocks.GUN_WORKBENCH, RenderType.cutout());

        PGItemTints.ALL.forEach(ItemTintSources.ID_MAPPER::put);

        initEvents();

        FabricPackets.registerS2CPackets();
    }

    private void initEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (PGKeyBinds.KEY_PORTAL_MENU.isDown() && client.player != null && client.player.getMainHandItem().is(PGTags.Items.PORTAL_GUNS)) {
                SBOpenCoordGuiPacket packet = new SBOpenCoordGuiPacket();
                ClientPlayNetworking.send(packet);
            }
        });

        ClientTickEvents.START_CLIENT_TICK.register(minecraft -> {
            PortalEntityRenderer.tickTexture();
        });
    }
}
