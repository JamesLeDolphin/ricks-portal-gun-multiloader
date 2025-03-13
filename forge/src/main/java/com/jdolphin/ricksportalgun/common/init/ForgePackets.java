package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.client.screen.portalgun.CoordTravelScreen;
import com.jdolphin.ricksportalgun.common.packet.*;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

public class ForgePackets {

    static int index = 0;
    public static final SimpleChannel INSTANCE = ChannelBuilder.named(PGHelper.createLocation("main")).simpleChannel();

    public static void init() {
        INSTANCE.messageBuilder(SBSettingsPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBSettingsPacket::encode)
                .decoder(SBSettingsPacket::new)
                .consumerMainThread((packet, context) -> packet.handle(context.getSender()))
                .add();
        INSTANCE.messageBuilder(SBSetDestinationPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBSetDestinationPacket::encode)
                .decoder(SBSetDestinationPacket::new)
                .consumerMainThread((packet, context) -> packet.handle(context.getSender()))
                .add();
        INSTANCE.messageBuilder(SBLocatePlayerPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBLocatePlayerPacket::encode)
                .decoder(SBLocatePlayerPacket::new)
                .consumerMainThread((packet, context) -> packet.handle(context.getSender()))
                .add();
        INSTANCE.messageBuilder(SBCoordCheckerPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBCoordCheckerPacket::encode)
                .decoder(SBCoordCheckerPacket::new)
                .consumerMainThread((packet, context) -> packet.handle(context.getSender()))
                .add();
        INSTANCE.messageBuilder(SBColourPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBColourPacket::encode)
                .decoder(SBColourPacket::new)
                .consumerMainThread((packet, context) -> packet.handle(context.getSender()))
                .add();
        INSTANCE.messageBuilder(SBManageWaypointsPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBManageWaypointsPacket::encode)
                .decoder(SBManageWaypointsPacket::new)
                .consumerMainThread((packet, context) -> packet.handle(context.getSender()))
                .add();
        INSTANCE.messageBuilder(SBOpenCoordGuiPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBOpenCoordGuiPacket::encode)
                .decoder(SBOpenCoordGuiPacket::new)
                .consumerMainThread((packet, context) -> packet.handle(context.getSender()))
                .add();
        INSTANCE.messageBuilder(CBOpenCoordGuiPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(CBOpenCoordGuiPacket::encode)
                .decoder(CBOpenCoordGuiPacket::new)
                .consumerMainThread((packet, context) -> {
                    if (context.isClientSide()) Minecraft.getInstance().setScreen(new CoordTravelScreen(packet.getSuggestions()));
                }).add();
        INSTANCE.messageBuilder(SBChangePortalGunTypePacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBChangePortalGunTypePacket::encode)
                .decoder(SBChangePortalGunTypePacket::new)
                .consumerMainThread((packet, context) -> packet.handle(context.getSender()))
                .add();
    }

    public static void sendToServer(Object msg) {
        INSTANCE.send(msg, PacketDistributor.SERVER.noArg());
    }

    public static void sendToPlayer(Object msg, ServerPlayer player) {
        INSTANCE.send(msg, PacketDistributor.PLAYER.with(player));
    }

    public static void sendToAllClients(Object msg) {
        INSTANCE.send(msg, PacketDistributor.ALL.noArg());
    }
}