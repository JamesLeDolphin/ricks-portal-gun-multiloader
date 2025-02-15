package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.packet.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
        PayloadTypeRegistry.playC2S().register(SBOpenGuiPacket.ID, SBOpenGuiPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SBChangePortalGunTypePacket.ID, SBChangePortalGunTypePacket.CODEC);
        PayloadTypeRegistry.playS2C().register(CBOpenGuiPacket.ID, CBOpenGuiPacket.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SBColourPacket.ID, (packet, context) -> packet.handle(context.player()));
        ServerPlayNetworking.registerGlobalReceiver(SBCoordCheckerPacket.ID, (packet, context) -> packet.handle(context.player()));
        ServerPlayNetworking.registerGlobalReceiver(SBLocatePlayerPacket.ID, (packet, context) -> packet.handle(context.player()));
        ServerPlayNetworking.registerGlobalReceiver(SBManageWaypointsPacket.ID, (packet, context) -> packet.handle(context.player()));
        ServerPlayNetworking.registerGlobalReceiver(SBSetDestinationPacket.ID, (packet, context) -> packet.handle(context.player()));
        ServerPlayNetworking.registerGlobalReceiver(SBSettingsPacket.ID, (packet, context) -> packet.handle(context.player()));
        ServerPlayNetworking.registerGlobalReceiver(SBOpenGuiPacket.ID, (packet, context) -> packet.handle(context.player()));
        ServerPlayNetworking.registerGlobalReceiver(SBChangePortalGunTypePacket.ID, (packet, context) -> packet.handle(context.player()));

    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(CBOpenGuiPacket.ID, ((packet, context) -> packet.handle(context.client())));
    }
}