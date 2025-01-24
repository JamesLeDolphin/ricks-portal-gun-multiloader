package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.packets.*;
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

        ServerPlayNetworking.registerGlobalReceiver(SBColourPacket.ID, SBColourPacket::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBCoordCheckerPacket.ID, SBCoordCheckerPacket::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBLocatePlayerPacket.ID, SBLocatePlayerPacket::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBManageWaypointsPacket.ID, SBManageWaypointsPacket::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBSetDestinationPacket.ID, SBSetDestinationPacket::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBSettingsPacket.ID, SBSettingsPacket::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBOpenGuiPacket.ID, SBOpenGuiPacket::handle);
        ServerPlayNetworking.registerGlobalReceiver(SBChangePortalGunTypePacket.ID, SBChangePortalGunTypePacket::handle);
    }

    public static void registerS2CPackets() {
        PayloadTypeRegistry.playS2C().register(CBOpenGuiPacket.ID, CBOpenGuiPacket.PACKET_CODEC);

        ClientPlayNetworking.registerGlobalReceiver(CBOpenGuiPacket.ID, CBOpenGuiPacket::handle);
    }
}