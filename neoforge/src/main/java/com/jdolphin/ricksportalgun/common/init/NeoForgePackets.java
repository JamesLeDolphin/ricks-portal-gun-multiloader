package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.client.handler.ClientPacketHandler;
import com.jdolphin.ricksportalgun.common.packet.clientbound.*;
import com.jdolphin.ricksportalgun.common.packet.serverbound.*;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NeoForgePackets {

    public static void init(PayloadRegistrar registrar) {
        //Server bound
        registrar.commonToServer(SBSecuritySettingsPacket.ID, SBSecuritySettingsPacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBSetDestinationPacket.ID, SBSetDestinationPacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBLocatePacket.ID, SBLocatePacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBCoordCheckerPacket.ID, SBCoordCheckerPacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBColourPacket.ID, SBColourPacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBManageWaypointsPacket.ID, SBManageWaypointsPacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBOpenCoordGuiPacket.ID, SBOpenCoordGuiPacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBSetWorkbenchTypePacket.ID, SBSetWorkbenchTypePacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBOpenLocatorScreenPacket.ID, SBOpenLocatorScreenPacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBOpenSecuritySettingsPacket.ID, SBOpenSecuritySettingsPacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBSetPortalGunStylePacket.ID, SBSetPortalGunStylePacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBCustomizeSettingsPacket.ID, SBCustomizeSettingsPacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBWorkbenchWaypointEditPackage.ID, SBWorkbenchWaypointEditPackage.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBActivateSelfDestructPacket.ID, SBActivateSelfDestructPacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBSetDispenserDestinationPacket.ID, SBSetDispenserDestinationPacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBSetBarrierCodePacket.ID, SBSetBarrierCodePacket.CODEC, NeoForgePackets::handle);

        //Client bound
        registrar.commonToClient(CBOpenCoordGuiPacket.ID, CBOpenCoordGuiPacket.CODEC, (packet, context) -> ClientPacketHandler.openCoordTravelScreen(packet.strings()));
        registrar.commonToClient(CBOpenBarrierGuiPacket.ID, CBOpenBarrierGuiPacket.CODEC, (packet, context) ->ClientPacketHandler.openBarrierGui(packet.pos()));
        registrar.commonToClient(CBSyncDimensionListPacket.ID, CBSyncDimensionListPacket.CODEC, (packet, context) ->
                ClientPacketHandler.syncClientDimensions(packet.dimensions()));

        registrar.commonToClient(CBOpenLocatorScreenPacket.ID, CBOpenLocatorScreenPacket.CODEC, (packet, context) ->
                ClientPacketHandler.openLocatorScreen(packet.playerList(), packet.biomeList(), packet.structureList()));
        registrar.commonToClient(CBOpenSecurityGuiPacket.ID, CBOpenSecurityGuiPacket.CODEC, (packet, context) ->
                ClientPacketHandler.openSecurityScreen(packet.strings()));
    }

    private static  <P extends PGPayload> void handle(P packet, IPayloadContext context) {
        packet.handle((ServerPlayer) context.player());
    }
}
