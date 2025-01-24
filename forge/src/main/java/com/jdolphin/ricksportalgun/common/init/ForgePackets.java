package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.packets.*;
import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

public class ForgePackets {

    static int index = 0;
    public static final SimpleChannel INSTANCE = ChannelBuilder.named(Helper.createLocation("main")).simpleChannel();

    public static void init() {
        INSTANCE.messageBuilder(SBSettingsPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBSettingsPacket::encode)
                .decoder(SBSettingsPacket::new)
                .consumerMainThread(SBSettingsPacket::handle)
                .add();
        INSTANCE.messageBuilder(SBSetDestinationPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBSetDestinationPacket::encode)
                .decoder(SBSetDestinationPacket::new)
                .consumerMainThread(SBSetDestinationPacket::handle)
                .add();
        INSTANCE.messageBuilder(SBLocatePlayerPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBLocatePlayerPacket::encode)
                .decoder(SBLocatePlayerPacket::new)
                .consumerMainThread(SBLocatePlayerPacket::handle)
                .add();
        INSTANCE.messageBuilder(SBCoordCheckerPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBCoordCheckerPacket::encode)
                .decoder(SBCoordCheckerPacket::new)
                .consumerMainThread(SBCoordCheckerPacket::handle)
                .add();
        INSTANCE.messageBuilder(SBColourPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBColourPacket::encode)
                .decoder(SBColourPacket::new)
                .consumerMainThread(SBColourPacket::handle)
                .add();
        INSTANCE.messageBuilder(SBManageWaypointsPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBManageWaypointsPacket::encode)
                .decoder(SBManageWaypointsPacket::new)
                .consumerMainThread(SBManageWaypointsPacket::handle)
                .add();
        INSTANCE.messageBuilder(SBOpenGuiPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SBOpenGuiPacket::encode)
                .decoder(SBOpenGuiPacket::new)
                .consumerMainThread(SBOpenGuiPacket::handle)
                .add();
        INSTANCE.messageBuilder(CBOpenGuiPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(CBOpenGuiPacket::encode)
                .decoder(CBOpenGuiPacket::new)
                .consumerMainThread(CBOpenGuiPacket::handle)
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