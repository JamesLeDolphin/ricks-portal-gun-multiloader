package com.jdolphin.ricksportalgun.client;

import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.client.entity.model.PortalEntityModel;
import com.jdolphin.ricksportalgun.client.entity.render.PortalEntityRenderer;
import com.jdolphin.ricksportalgun.client.screen.PortalDispenserScreen;
import com.jdolphin.ricksportalgun.common.init.PGEntities;
import com.jdolphin.ricksportalgun.common.init.PGKeyBinds;
import com.jdolphin.ricksportalgun.common.init.PGMenuTypes;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBOpenCoordGuiPacket;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.tint.PortalColourTint;
import com.jdolphin.ricksportalgun.common.util.tint.PrimaryDyeTint;
import com.jdolphin.ricksportalgun.common.util.tint.SecondaryDyeTint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

public class ForgeClientMain {

    @Mod.EventBusSubscriber(modid = PGConstants.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {

            ItemTintSources.ID_MAPPER.put(PGHelper.createLocation("primary_dye"), PrimaryDyeTint.CODEC);
            ItemTintSources.ID_MAPPER.put(PGHelper.createLocation("secondary_dye"), SecondaryDyeTint.CODEC);
            ItemTintSources.ID_MAPPER.put(PGHelper.createLocation("portal_color"), PortalColourTint.CODEC);
        }

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


    @Mod.EventBusSubscriber(modid = PGConstants.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void clientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> MenuScreens.register(PGMenuTypes.PORTAL_DISPENSER, PortalDispenserScreen::new));
        }

        @SubscribeEvent
        public static void a(FMLConstructModEvent event) {
            ItemTintSources.ID_MAPPER.put(PGHelper.createLocation("primary_dye"), PrimaryDyeTint.CODEC);
            ItemTintSources.ID_MAPPER.put(PGHelper.createLocation("secondary_dye"), SecondaryDyeTint.CODEC);
            ItemTintSources.ID_MAPPER.put(PGHelper.createLocation("portal_color"), PortalColourTint.CODEC);
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
