package com.jdolphin.ricksportalgun;

import com.jdolphin.ricksportalgun.common.config.PGCommonConfig;
import com.jdolphin.ricksportalgun.common.data.FabricPortalGunTypeReloadListener;
import com.jdolphin.ricksportalgun.common.init.*;
import com.jdolphin.ricksportalgun.common.packet.CBSyncDimensionListPacket;
import com.jdolphin.ricksportalgun.common.packet.CBSyncGunTypesPacket;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.CreativeModeTabs;

import java.util.List;

public class RicksPortalGunFabricMain implements ModInitializer {

    @Override
    public void onInitialize() {
        RicksPortalGunCommonMain.init();
        FabricBlocks.register();
        FabricBlockEntities.register();
        FabricItems.register();
        FabricEntities.register();
        FabricDataComponents.register();
        FabricMenuTypes.register();
        FabricPackets.registerC2SPackets();

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new FabricPortalGunTypeReloadListener());

        ClientLifecycleEvents.CLIENT_STARTED.register(mc -> PGCommonConfig.INSTANCE = new PGCommonConfig());

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            PGCommonConfig.INSTANCE = new PGCommonConfig();
            List<String> strings = LevelHelper.getDimensionsAsString(server.getAllLevels());
            LevelHelper.addDimensions(strings);
        });

        ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> {
            CBSyncDimensionListPacket dimPacket = new CBSyncDimensionListPacket(LevelHelper.getDimensionsAsString(server.getAllLevels()));
            CBSyncGunTypesPacket typesPacket = new CBSyncGunTypesPacket(PortalGunTypeRegistry.PORTAL_GUN_TYPES);
            PGHelper.sendPacketToClient(listener.player, dimPacket);
            PGHelper.sendPacketToClient(listener.player, typesPacket);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(content -> {
            content.accept(PGItems.PORTAL_GUN);
            content.accept(PGItems.PRIME_PORTAL_GUN);
            content.accept(PGItems.GOLDEN_PORTAL_GUN);
        });


        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(content -> {
            content.accept(PGItems.PORTAL_FLUID);
            content.accept(PGItems.BOOTLEG_PORTAL_FLUID);
            content.accept(PGItems.QUANTUM_LEAP_ELIXIR);
        });
    }
}
