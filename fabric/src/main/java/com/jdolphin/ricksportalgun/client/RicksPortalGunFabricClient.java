package com.jdolphin.ricksportalgun.client;

import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.client.entity.model.PortalEntityModel;
import com.jdolphin.ricksportalgun.client.entity.render.PortalEntityRenderer;
import com.jdolphin.ricksportalgun.client.init.PGMenuScreens;
import com.jdolphin.ricksportalgun.client.init.PGTintHandler;
import com.jdolphin.ricksportalgun.common.config.PGClientConfig;
import com.jdolphin.ricksportalgun.common.init.*;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBOpenCoordGuiPacket;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import fuzs.forgeconfigapiport.fabric.api.forge.v4.ForgeConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.config.ModConfig;

public class RicksPortalGunFabricClient implements ClientModInitializer {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void onInitializeClient() {
        ForgeConfigRegistry.INSTANCE.register(PGConstants.MODID, ModConfig.Type.CLIENT, PGClientConfig.SPEC, "ricksportalgun-client.toml");

        EntityRendererRegistry.register(PGEntities.PORTAL, PortalEntityRenderer::new);
        EntityRendererRegistry.register(PGEntities.EXPLOSIVE_ITEM, ItemEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(PortalEntityModel.LAYER_LOCATION, PortalEntityModel::createBodyLayer);

        PGMenuScreens.ALL.forEach((type, func) -> {
            MenuScreens.ScreenConstructor constructor = func::apply;
            MenuScreens.register(type, constructor);
        });
        ColorProviderRegistry.ITEM.register(PGTintHandler::tint, PGTintHandler.TINTABLES);
        BlockRenderLayerMap.INSTANCE.putBlock(PGBlocks.GUN_WORKBENCH, RenderType.cutout());
        KeyBindingHelper.registerKeyBinding(PGKeyBinds.KEY_PORTAL_MENU);

        initEvents();

        FabricPackets.registerS2CPackets();


    }

    private void initEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Player player = client.player;
            if (player != null) {
                ItemStack stack = player.getItemInHand(PGHelper.getPortalGunHand(player));
                if (PGKeyBinds.KEY_PORTAL_MENU.isDown() && client.player != null && stack.is(PGTags.Items.PORTAL_GUNS)) {
                    SBOpenCoordGuiPacket packet = new SBOpenCoordGuiPacket();
                    ClientPlayNetworking.send(packet);
                }
            }
        });

        ClientTickEvents.START_CLIENT_TICK.register(minecraft -> {
            PortalEntityRenderer.tickTexture();
        });
    }
}
