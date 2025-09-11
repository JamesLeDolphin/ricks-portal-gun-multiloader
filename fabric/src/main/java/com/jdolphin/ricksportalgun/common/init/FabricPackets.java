package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.client.handler.ClientPacketHandler;
import com.jdolphin.ricksportalgun.common.packet.clientbound.*;
import com.jdolphin.ricksportalgun.common.packet.serverbound.*;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class FabricPackets {

    public static void registerC2SPackets() {
        PayloadTypeRegistry.playC2S().register(SBColourPacket.ID, SBColourPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBCoordCheckerPacket.ID, SBCoordCheckerPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBLocatePacket.ID, SBLocatePacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBManageWaypointsPacket.ID, SBManageWaypointsPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBSetDestinationPacket.ID, SBSetDestinationPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBSecuritySettingsPacket.ID, SBSecuritySettingsPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBOpenCoordGuiPacket.ID, SBOpenCoordGuiPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBSetBarrierCodePacket.ID, SBSetBarrierCodePacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBSetDispenserDestinationPacket.ID, SBSetDispenserDestinationPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBSetWorkbenchTypePacket.ID, SBSetWorkbenchTypePacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBOpenLocatorScreenPacket.ID, SBOpenLocatorScreenPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBOpenSecuritySettingsPacket.ID, SBOpenSecuritySettingsPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBSetPortalGunStylePacket.ID, SBSetPortalGunStylePacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBCustomizeSettingsPacket.ID, SBCustomizeSettingsPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBWorkbenchWaypointEditPackage.ID, SBWorkbenchWaypointEditPackage.CODEC);
        PayloadTypeRegistry.playC2S().register(SBActivateSelfDestructPacket.ID, SBActivateSelfDestructPacket.CODEC);

        PayloadTypeRegistry.playS2C().register(CBOpenCoordGuiPacket.ID, CBOpenCoordGuiPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(CBSyncDimensionListPacket.ID, CBSyncDimensionListPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(CBOpenBarrierGuiPacket.ID, CBOpenBarrierGuiPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(CBOpenLocatorScreenPacket.ID, CBOpenLocatorScreenPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(CBOpenSecurityGuiPacket.ID, CBOpenSecurityGuiPacket.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SBColourPacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBCoordCheckerPacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBLocatePacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBManageWaypointsPacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBSetDestinationPacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBSecuritySettingsPacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBOpenCoordGuiPacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBSetBarrierCodePacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBSetDispenserDestinationPacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBSetWorkbenchTypePacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBOpenLocatorScreenPacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBOpenSecuritySettingsPacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBSetPortalGunStylePacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBCustomizeSettingsPacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBWorkbenchWaypointEditPackage.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBActivateSelfDestructPacket.ID, FabricPackets::handle);
    }

    private static  <P extends PGPayload> void handle(P packet, ServerPlayNetworking.Context context) {
        packet.handle(context.player());
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(CBOpenCoordGuiPacket.ID, (packet, context) -> ClientPacketHandler.openCoordTravelScreen(packet.strings()));
        ClientPlayNetworking.registerGlobalReceiver(CBSyncDimensionListPacket.ID, (packet, context) -> ClientPacketHandler.syncClientDimensions(packet.dimensions()));
        ClientPlayNetworking.registerGlobalReceiver(CBOpenBarrierGuiPacket.ID, (packet, context) -> ClientPacketHandler.openBarrierGui(packet.pos()));
        ClientPlayNetworking.registerGlobalReceiver(CBOpenLocatorScreenPacket.ID, (packet, context) ->
                ClientPacketHandler.openLocatorScreen(packet.playerList(), packet.biomeList(), packet.structureList()));
        ClientPlayNetworking.registerGlobalReceiver(CBOpenSecurityGuiPacket.ID, (packet, context) -> ClientPacketHandler.openSecurityScreen(packet.strings()));
    }
}