package com.jdolphin.ricksportalgun.client;

import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.client.init.PGItemTints;
import com.jdolphin.ricksportalgun.client.init.PGMenuScreens;
import com.jdolphin.ricksportalgun.client.model.PortalEntityModel;
import com.jdolphin.ricksportalgun.client.render.PortalEntityRenderer;
import com.jdolphin.ricksportalgun.common.init.ForgePackets;
import com.jdolphin.ricksportalgun.common.init.PGEntities;
import com.jdolphin.ricksportalgun.common.init.PGKeyBinds;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBOpenCoordGuiPacket;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

public class ForgeClientMain {

    @Mod.EventBusSubscriber(modid = PGConstants.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            PortalEntityRenderer.tickTexture();
        }

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (PGKeyBinds.KEY_PORTAL_MENU.consumeClick()) {
                Player player = Minecraft.getInstance().player;
                if (player != null) {
                    InteractionHand hand = player.getUsedItemHand();
                    ItemStack stack = player.getItemInHand(hand);
                    if (stack.is(PGTags.Items.PORTAL_GUNS)) {
                        SBOpenCoordGuiPacket packet = new SBOpenCoordGuiPacket();
                        ForgePackets.sendToServer(packet);
                    }
                }
            }
        }
    }


    @Mod.EventBusSubscriber(modid = PGConstants.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {

        @SuppressWarnings({"rawtypes", "unchecked"})
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() ->
                    PGMenuScreens.ALL.forEach((type, func) -> {
                MenuScreens.ScreenConstructor constructor = func::apply;
                MenuScreens.register(type, constructor);
            }));
        }

        @SubscribeEvent
        public static void constructEvent(FMLConstructModEvent event) {
            PGItemTints.ALL.forEach(ItemTintSources.ID_MAPPER::put);
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
    }
}
