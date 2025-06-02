package com.jdolphin.ricksportalgun.client;

import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.client.entity.model.PortalEntityModel;
import com.jdolphin.ricksportalgun.client.entity.render.PortalEntityRenderer;
import com.jdolphin.ricksportalgun.client.screen.PortalDispenserScreen;
import com.jdolphin.ricksportalgun.client.screen.workbench.SkinSelectingScreen;
import com.jdolphin.ricksportalgun.client.screen.workbench.WaypointTransferScreen;
import com.jdolphin.ricksportalgun.client.screen.workbench.WorkbenchCraftingScreen;
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
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD, modid = PGConstants.MODID)
public class NeoForgeClientMain {

    @SubscribeEvent
    public static void clientSetup(RegisterMenuScreensEvent event) {
        event.register(PGMenuTypes.PORTAL_DISPENSER, PortalDispenserScreen::new);
        event.register(PGMenuTypes.WORKBENCH_WAYPOINT_TRANSFER, WaypointTransferScreen::new);
        event.register(PGMenuTypes.WORKBENCH_CRAFTING, WorkbenchCraftingScreen::new);
        event.register(PGMenuTypes.WORKBENCH_SKIN_SELECTOR, SkinSelectingScreen::new);
    }

    @SubscribeEvent
    public static void registerTints(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(PGHelper.createLocation("primary_dye"), PrimaryDyeTint.CODEC);
        event.register(PGHelper.createLocation("secondary_dye"), SecondaryDyeTint.CODEC);
        event.register(PGHelper.createLocation("portal_color"), PortalColourTint.CODEC);
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
