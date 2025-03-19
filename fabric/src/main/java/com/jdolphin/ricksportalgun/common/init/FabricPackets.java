package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.packet.*;
import com.jdolphin.ricksportalgun.common.packets.CBOpenCoordGuiPacketFabric;
import com.jdolphin.ricksportalgun.common.packets.CBOpenGuiPacketFabric;
import com.jdolphin.ricksportalgun.common.packets.SBOpenCoordGuiPacketFabric;
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
        PayloadTypeRegistry.playC2S().register(SBOpenCoordGuiPacketFabric.ID, SBOpenCoordGuiPacketFabric.CODEC);
        PayloadTypeRegistry.playC2S().register(SBChangePortalGunTypePacket.ID, SBChangePortalGunTypePacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBSetBarrierCodePacket.ID, SBSetBarrierCodePacket.CODEC);

        PayloadTypeRegistry.playS2C().register(CBOpenCoordGuiPacketFabric.ID, CBOpenCoordGuiPacketFabric.CODEC);
        PayloadTypeRegistry.playS2C().register(CBSyncDimensionListPacket.ID, CBSyncDimensionListPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(CBSyncGunTypesPacket.ID, CBSyncGunTypesPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(CBOpenGuiPacketFabric.ID, CBOpenGuiPacketFabric.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SBColourPacket.ID, (packet, context) -> packet.handle(context.player()));
        ServerPlayNetworking.registerGlobalReceiver(SBCoordCheckerPacket.ID, (packet, context) -> packet.handle(context.player()));
        ServerPlayNetworking.registerGlobalReceiver(SBLocatePlayerPacket.ID, (packet, context) -> packet.handle(context.player()));
        ServerPlayNetworking.registerGlobalReceiver(SBManageWaypointsPacket.ID, (packet, context) -> packet.handle(context.player()));
        ServerPlayNetworking.registerGlobalReceiver(SBSetDestinationPacket.ID, (packet, context) -> packet.handle(context.player()));
        ServerPlayNetworking.registerGlobalReceiver(SBSettingsPacket.ID, (packet, context) -> packet.handle(context.player()));
        ServerPlayNetworking.registerGlobalReceiver(SBOpenCoordGuiPacketFabric.ID, (packet, context) -> packet.handle(context.player()));
        ServerPlayNetworking.registerGlobalReceiver(SBChangePortalGunTypePacket.ID, (packet, context) -> packet.handle(context.player()));
        ServerPlayNetworking.registerGlobalReceiver(SBSetBarrierCodePacket.ID, (packet, context) -> packet.handle(context.player()));
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(CBOpenCoordGuiPacketFabric.ID, (packet, context) -> packet.handle(context.client()));
        ClientPlayNetworking.registerGlobalReceiver(CBSyncDimensionListPacket.ID, (packet, context) -> packet.handle());
        ClientPlayNetworking.registerGlobalReceiver(CBSyncGunTypesPacket.ID, (packet, context) -> packet.handle());
        ClientPlayNetworking.registerGlobalReceiver(CBOpenGuiPacketFabric.ID, (packet, context) -> packet.handle(context.client()));
    }
}