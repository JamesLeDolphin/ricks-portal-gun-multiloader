package com.jdolphin.ricksportalgun.client;

import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.client.entity.model.PortalEntityModel;
import com.jdolphin.ricksportalgun.client.entity.render.PortalEntityRenderer;
import com.jdolphin.ricksportalgun.client.screen.PortalDispenserScreen;
import com.jdolphin.ricksportalgun.common.init.PGEntities;
import com.jdolphin.ricksportalgun.common.init.PGKeyBinds;
import com.jdolphin.ricksportalgun.common.init.PGMenuTypes;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.packet.SBOpenCoordGuiPacket;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ForgeClientMain {

    @Mod.EventBusSubscriber(modid = PGConstants.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (PGKeyBinds.KEY_PORTAL_MENU.consumeClick()) {
                Minecraft minecraft = Minecraft.getInstance();
                LocalPlayer player = minecraft.player;
                ItemStack gun = player.getMainHandItem();

                if (gun.is(PGTags.Items.PORTAL_GUNS)) {
                    SBOpenCoordGuiPacket packet = new SBOpenCoordGuiPacket(player.getStringUUID());
                    PGHelper.sendPacketToServer(packet);
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = PGConstants.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {

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
