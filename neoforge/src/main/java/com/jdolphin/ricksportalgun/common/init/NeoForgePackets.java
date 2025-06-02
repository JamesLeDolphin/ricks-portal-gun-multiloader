package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.client.handler.ClientPacketHandler;
import com.jdolphin.ricksportalgun.common.packet.clientbound.CBOpenBarrierGuiPacket;
import com.jdolphin.ricksportalgun.common.packet.clientbound.CBOpenCoordGuiPacket;
import com.jdolphin.ricksportalgun.common.packet.clientbound.CBSyncDimensionListPacket;
import com.jdolphin.ricksportalgun.common.packet.clientbound.CBSyncGunTypesPacket;
import com.jdolphin.ricksportalgun.common.packet.serverbound.*;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NeoForgePackets {

    public static void init(PayloadRegistrar registrar) {
        //Server bound
        registrar.commonToServer(SBSettingsPacket.ID, SBSettingsPacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBSetDestinationPacket.ID, SBSetDestinationPacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBLocatePlayerPacket.ID, SBLocatePlayerPacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBCoordCheckerPacket.ID, SBCoordCheckerPacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBColourPacket.ID, SBColourPacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBManageWaypointsPacket.ID, SBManageWaypointsPacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBOpenCoordGuiPacket.ID, SBOpenCoordGuiPacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBChangePortalGunTypePacket.ID, SBChangePortalGunTypePacket.CODEC, NeoForgePackets::handle);
        registrar.commonToServer(SBSetWorkbenchTypePacket.ID, SBSetWorkbenchTypePacket.CODEC, NeoForgePackets::handle);

        //Client bound
        registrar.commonToClient(CBOpenCoordGuiPacket.ID, CBOpenCoordGuiPacket.CODEC, (packet, context) -> ClientPacketHandler.openCoordTravelScreen(packet.strings));
        registrar.commonToClient(CBOpenBarrierGuiPacket.ID, CBOpenBarrierGuiPacket.CODEC, (packet, context) ->ClientPacketHandler.openBarrierGui(packet.pos()));
        registrar.commonToClient(CBSyncGunTypesPacket.ID, CBSyncGunTypesPacket.CODEC, (packet, context) -> ClientPacketHandler.syncGunTypes(packet.types()));
        registrar.commonToClient(CBSyncDimensionListPacket.ID, CBSyncDimensionListPacket.CODEC, (packet, context) -> ClientPacketHandler.syncClientDimensions(packet.dimensions()));
    }

    private static  <P extends CustomPacketPayload> void handle(P packet, IPayloadContext context) {
        ((PGPayload) packet).handle((ServerPlayer) context.player());
    }
}
