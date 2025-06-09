package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.client.handler.ClientPacketHandler;
import com.jdolphin.ricksportalgun.common.packet.clientbound.*;
import com.jdolphin.ricksportalgun.common.packet.serverbound.*;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

public class ForgePackets {

    static int index = 0;
    public static final SimpleChannel INSTANCE = ChannelBuilder.named(PGHelper.createLocation("main")).simpleChannel();

    public static void init() {
        //Server bound
        INSTANCE.messageBuilder(SBSettingsPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .codec(SBSettingsPacket.CODEC.cast())
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBSetDestinationPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .codec(SBSetDestinationPacket.CODEC.cast())
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBLocatePlayerPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .codec(SBLocatePlayerPacket.CODEC.cast())
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBCoordCheckerPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .codec(SBCoordCheckerPacket.CODEC.cast())
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBColourPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .codec(SBColourPacket.CODEC.cast())
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBManageWaypointsPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .codec(SBManageWaypointsPacket.CODEC.cast())
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBOpenCoordGuiPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .codec(SBOpenCoordGuiPacket.CODEC.cast())
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBChangePortalGunTypePacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .codec(SBChangePortalGunTypePacket.CODEC.cast())
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBSetWorkbenchTypePacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .codec(SBSetWorkbenchTypePacket.CODEC.cast())
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBOpenLocatorScreen.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .codec(SBOpenLocatorScreen.CODEC.cast())
                .consumerMainThread(ForgePackets::handle)
                .add();

        //Client bound
        INSTANCE.messageBuilder(CBOpenCoordGuiPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .codec(CBOpenCoordGuiPacket.CODEC.cast())
                .consumerMainThread((packet, context) -> {
                    if (context.isClientSide()) ClientPacketHandler.openCoordTravelScreen(packet.strings);
                }).add();
        INSTANCE.messageBuilder(CBOpenBarrierGuiPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .codec(CBOpenBarrierGuiPacket.CODEC.cast())
                .consumerMainThread((packet, context) -> {
                    if (context.isClientSide()) ClientPacketHandler.openBarrierGui(packet.pos());
                }).add();
        INSTANCE.messageBuilder(CBSyncGunTypesPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .codec(CBSyncGunTypesPacket.CODEC.cast())
                .consumerMainThread((packet, context) -> {
                    if (context.isClientSide()) ClientPacketHandler.syncGunTypes(packet.types());
                }).add();
        INSTANCE.messageBuilder(CBSyncDimensionListPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .codec(CBSyncDimensionListPacket.CODEC.cast())
                .consumerMainThread((packet, context) -> {
                    if (context.isClientSide()) ClientPacketHandler.syncClientDimensions(packet.dimensions());
                }).add();
        INSTANCE.messageBuilder(CBOpenLocatorScreenPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .codec(CBOpenLocatorScreenPacket.CODEC.cast())
                .consumerMainThread((packet, context) -> {
                    if (context.isClientSide()) ClientPacketHandler.openLocatorScreen(packet.playerList(), packet.biomeList(), packet.structureList());
                }).add();
    }

    private static  <P extends PGPayload> void handle(P packet, CustomPayloadEvent.Context context) {
        packet.handle(context.getSender());
    }

    public static void sendToServer(Object msg) {
        INSTANCE.send(msg, PacketDistributor.SERVER.noArg());
    }

    public static void sendToPlayer(ServerPlayer player, Object... messages) {
        for (Object obj : messages) {
            INSTANCE.send(obj, PacketDistributor.PLAYER.with(player));
        }
    }
}