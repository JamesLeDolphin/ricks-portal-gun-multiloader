package com.jdolphin.ricksportalgun.client;

import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.client.init.PGItemTints;
import com.jdolphin.ricksportalgun.client.init.PGMenuScreens;
import com.jdolphin.ricksportalgun.client.model.PortalEntityModel;
import com.jdolphin.ricksportalgun.client.render.PortalEntityRenderer;
import com.jdolphin.ricksportalgun.common.init.PGEntities;
import com.jdolphin.ricksportalgun.common.init.PGKeyBinds;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBOpenCoordGuiPacket;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD, modid = PGConstants.MODID)
public class NeoForgeClientMain {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @SubscribeEvent
    public static void clientSetup(RegisterMenuScreensEvent event) {
        PGMenuScreens.ALL.forEach((type, func) -> {
            MenuScreens.ScreenConstructor constructor = func::apply;
            event.register(type, constructor);
        });
    }

    @SubscribeEvent
    public static void clientTick(ClientTickEvent event) {
        PortalEntityRenderer.tickTexture();
    }

    @SubscribeEvent
    public static void registerTints(RegisterColorHandlersEvent.ItemTintSources event) {
        PGItemTints.ALL.forEach(event::register);
    }

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(PGKeyBinds.KEY_PORTAL_MENU);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(PortalEntityModel.LAYER_LOCATION, PortalEntityModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(PGEntities.PORTAL, PortalEntityRenderer::new);
    }

    @EventBusSubscriber(modid = PGConstants.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (PGKeyBinds.KEY_PORTAL_MENU.consumeClick()) {
                Minecraft minecraft = Minecraft.getInstance();
                LocalPlayer player = minecraft.player;
                ItemStack gun = player.getMainHandItem();

                if (gun.is(PGTags.Items.PORTAL_GUNS)) {
                    SBOpenCoordGuiPacket packet = new SBOpenCoordGuiPacket();
                    PGHelper.sendPacketToServer(packet);
                }
            }
        }
    }
}
