package com.jdolphin.ricksportalgun.client;

import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.client.event.PGClientEventHandler;
import com.jdolphin.ricksportalgun.client.init.*;
import com.jdolphin.ricksportalgun.common.init.PGKeyBinds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ForgeClientMain {

    @Mod.EventBusSubscriber(modid = PGConstants.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void clientTickEvent(TickEvent.ClientTickEvent event) {
            PGClientEventHandler.onClientTick(Minecraft.getInstance());
        }
    }

    @Mod.EventBusSubscriber(modid = PGConstants.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
            event.register(PGTintHandler::tint, PGTintHandler.TINTABLES);
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                PGMenuScreens.ALL.forEach((type, func) -> {
                    MenuScreens.ScreenConstructor constructor = func::apply;
                    MenuScreens.register(type, constructor);
                });
                PGPortalTypeRenderers.init();
                PGPortalShapeRenderers.init();
            });
        }

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(PGKeyBinds.KEY_PORTAL_MENU);
        }

        @SubscribeEvent
        public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
            PGEntityRenderRegistry.initLayers();
            PGEntityRenderRegistry.BODY_LAYERS.forEach(event::registerLayerDefinition);
        }

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            PGEntityRenderRegistry.initRenderers();
            PGEntityRenderRegistry.RENDERERS.forEach((entityType, entityRendererProvider) -> {
                if (entityType != null && entityRendererProvider != null) {
                    event.registerEntityRenderer(entityType, entityRendererProvider);
                }
            });
        }
    }
}
