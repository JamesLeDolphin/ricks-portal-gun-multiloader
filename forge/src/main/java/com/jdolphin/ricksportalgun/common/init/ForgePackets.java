package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.client.handler.ClientPacketHandler;
import com.jdolphin.ricksportalgun.common.packet.clientbound.*;
import com.jdolphin.ricksportalgun.common.packet.serverbound.*;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class ForgePackets {

    static int index = 0;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            PGHelper.id("main"), () -> "1", "1"::equals,
            "1"::equals);

    public static void init() {
        //Server bound
        INSTANCE.messageBuilder(SBSecuritySettingsPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBSecuritySettingsPacket::encode)
                .decoder(SBSecuritySettingsPacket::decode)
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBSetDestinationPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBSetDestinationPacket::encode)
                .decoder(SBSetDestinationPacket::decode)
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBLocatePacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBLocatePacket::encode)
                .decoder(SBLocatePacket::decode)
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBCoordCheckerPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBCoordCheckerPacket::encode)
                .decoder(SBCoordCheckerPacket::decode)
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBColourPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBColourPacket::encode)
                .decoder(SBColourPacket::decode)
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBManageWaypointsPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBManageWaypointsPacket::encode)
                .decoder(SBManageWaypointsPacket::decode)
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBOpenCoordGuiPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBOpenCoordGuiPacket::encode)
                .decoder(SBOpenCoordGuiPacket::decode)
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBSetWorkbenchTypePacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBSetWorkbenchTypePacket::encode)
                .decoder(SBSetWorkbenchTypePacket::decode)
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBOpenLocatorScreenPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBOpenLocatorScreenPacket::encode)
                .decoder(SBOpenLocatorScreenPacket::decode)
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBOpenSecuritySettingsPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBOpenSecuritySettingsPacket::encode)
                .decoder(SBOpenSecuritySettingsPacket::decode)
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBSetPortalGunStylePacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBSetPortalGunStylePacket::encode)
                .decoder(SBSetPortalGunStylePacket::decode)
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBCustomizeSettingsPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBCustomizeSettingsPacket::encode)
                .decoder(SBCustomizeSettingsPacket::decode)
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBWorkbenchWaypointEditPackage.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBWorkbenchWaypointEditPackage::encode)
                .decoder(SBWorkbenchWaypointEditPackage::decode)
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBActivateSelfDestructPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBActivateSelfDestructPacket::encode)
                .decoder(SBActivateSelfDestructPacket::decode)
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBSetPortalGunTypePacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBSetPortalGunTypePacket::encode)
                .decoder(SBSetPortalGunTypePacket::decode)
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBSetBarrierCodePacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBSetBarrierCodePacket::encode)
                .decoder(SBSetBarrierCodePacket::decode)
                .consumerMainThread(ForgePackets::handle)
                .add();
        INSTANCE.messageBuilder(SBSetDispenserDestinationPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBSetDispenserDestinationPacket::encode)
                .decoder(SBSetDispenserDestinationPacket::decode)
                .consumerMainThread(ForgePackets::handle)
                .add();

        //Client bound
        INSTANCE.messageBuilder(CBOpenCoordGuiPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(CBOpenCoordGuiPacket::encode)
                .decoder(CBOpenCoordGuiPacket::decode)
                .consumerMainThread((packet, context) -> {
                    if (isClientSide(context)) ClientPacketHandler.openCoordTravelScreen(packet.strings());
                }).add();
        INSTANCE.messageBuilder(CBOpenBarrierGuiPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(CBOpenBarrierGuiPacket::encode)
                .decoder(CBOpenBarrierGuiPacket::decode)
                .consumerMainThread((packet, context) -> {
                    if (isClientSide(context)) ClientPacketHandler.openBarrierGui(packet.pos());
                }).add();
        INSTANCE.messageBuilder(CBSyncDimensionListPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(CBSyncDimensionListPacket::encode)
                .decoder(CBSyncDimensionListPacket::decode)
                .consumerMainThread((packet, context) -> {
                    if (isClientSide(context)) ClientPacketHandler.syncClientDimensions(packet.dimensions());
                }).add();
        INSTANCE.messageBuilder(CBOpenLocatorScreenPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(CBOpenLocatorScreenPacket::encode)
                .decoder(CBOpenLocatorScreenPacket::decode)
                .consumerMainThread((packet, context) -> {
                    if (isClientSide(context)) ClientPacketHandler.openLocatorScreen(packet.playerList(), packet.biomeList(), packet.structureList());
                }).add();
        INSTANCE.messageBuilder(CBOpenSecurityGuiPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(CBOpenSecurityGuiPacket::encode)
                .decoder(CBOpenSecurityGuiPacket::decode)
                .consumerMainThread((packet, context) -> {
                    if (isClientSide(context)) ClientPacketHandler.openSecurityScreen(packet.strings());
                }).add();
    }

    private static boolean isClientSide(Supplier<NetworkEvent.Context> supplier) {
        return supplier.get().getDirection().equals(NetworkDirection.PLAY_TO_CLIENT);
    }

    private static  <P extends PGServerPayload> void handle(P packet, Supplier<NetworkEvent.Context> consumer) {
        packet.handle(consumer.get().getSender());
    }

    public static void sendToServer(Object msg) {
        INSTANCE.send(PacketDistributor.SERVER.noArg(), msg);
    }

    public static void sendToPlayer(ServerPlayer player, Object... messages) {
        for (Object packet : messages) {
            INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
        }
    }
}