package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.client.handler.ClientPacketHandler;
import com.jdolphin.ricksportalgun.common.packet.clientbound.CBOpenBarrierGuiPacket;
import com.jdolphin.ricksportalgun.common.packet.clientbound.CBOpenCoordGuiPacket;
import com.jdolphin.ricksportalgun.common.packet.clientbound.CBSyncDimensionListPacket;
import com.jdolphin.ricksportalgun.common.packet.clientbound.CBSyncGunTypesPacket;
import com.jdolphin.ricksportalgun.common.packet.serverbound.*;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class FabricPackets {

    public static void registerC2SPackets() {
        PayloadTypeRegistry.playC2S().register(SBColourPacket.ID, SBColourPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBCoordCheckerPacket.ID, SBCoordCheckerPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBLocatePlayerPacket.ID, SBLocatePlayerPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBManageWaypointsPacket.ID, SBManageWaypointsPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBSetDestinationPacket.ID, SBSetDestinationPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBSettingsPacket.ID, SBSettingsPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBOpenCoordGuiPacket.ID, SBOpenCoordGuiPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBChangePortalGunTypePacket.ID, SBChangePortalGunTypePacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBSetBarrierCodePacket.ID, SBSetBarrierCodePacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBSetDispenserDestinationPacket.ID, SBSetDispenserDestinationPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBSetWorkbenchTypePacket.ID, SBSetWorkbenchTypePacket.CODEC);

        PayloadTypeRegistry.playS2C().register(CBOpenCoordGuiPacket.ID, CBOpenCoordGuiPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(CBSyncDimensionListPacket.ID, CBSyncDimensionListPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(CBSyncGunTypesPacket.ID, CBSyncGunTypesPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(CBOpenBarrierGuiPacket.ID, CBOpenBarrierGuiPacket.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SBColourPacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBCoordCheckerPacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBLocatePlayerPacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBManageWaypointsPacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBSetDestinationPacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBSettingsPacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBOpenCoordGuiPacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBChangePortalGunTypePacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBSetBarrierCodePacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBSetDispenserDestinationPacket.ID, FabricPackets::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBSetWorkbenchTypePacket.ID, FabricPackets::handle);
    }

    private static  <P extends PGPayload> void handle(P packet, ServerPlayNetworking.Context context) {
        packet.handle(context.player());
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(CBOpenCoordGuiPacket.ID, (packet, context) -> ClientPacketHandler.openCoordTravelScreen(packet.strings));
        ClientPlayNetworking.registerGlobalReceiver(CBSyncDimensionListPacket.ID, (packet, context) -> ClientPacketHandler.syncClientDimensions(packet.dimensions()));
        ClientPlayNetworking.registerGlobalReceiver(CBSyncGunTypesPacket.ID, (packet, context) -> ClientPacketHandler.syncGunTypes(packet.types()));
        ClientPlayNetworking.registerGlobalReceiver(CBOpenBarrierGuiPacket.ID, (packet, context) -> ClientPacketHandler.openBarrierGui(packet.pos()));
    }
}