package com.jdolphin.ricksportalgun.common.util.helper;

import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.common.util.platform.PGServices;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class PGHelper {
    public static MutableComponent COORDS_SET = Component.translatable("notice.ricksportalgun.destination.set");

    public static ResourceLocation createLocation(String string) {
        return ResourceLocation.fromNamespaceAndPath(PGConstants.MODID, string);
    }

    public static int getRandomizerMax() {
        return PGServices.PLATFORM.getRandomizerMax();
    }

    public static boolean disablePlayerLocating() {
        return PGServices.PLATFORM.disablePlayerLocating();
    }

    public static boolean disableBiomeLocating() {
        return PGServices.PLATFORM.disableBiomeLocating();
    }

    public static boolean disableStructureLocating() {
        return PGServices.PLATFORM.disableStructureLocating();
    }

    public static List<? extends String> getDisabledDimensions() {
        return PGServices.PLATFORM.getDisabledDimensions();
    }

    public static void sendSuccessMsg(Player player, String msg) {
        sendSuccessMsg(player, Component.translatable(msg));
    }

    public static void sendSuccessMsg(Player player, MutableComponent msg) {
        player.displayClientMessage(msg.withStyle(ChatFormatting.GREEN), false);
    }

    public static void sendFailMsg(Player player, MutableComponent msg) {
        player.displayClientMessage(msg.withStyle(ChatFormatting.RED), false);
    }

    public static void sendFailMsg(Player player, String msg) {
        sendFailMsg(player, Component.translatable(msg));
    }

    public static <P extends CustomPacketPayload> void sendPacketToServer(P packet) {
        PGServices.PLATFORM.sendPacketToServer(packet);
    }

    public static <P extends CustomPacketPayload> void sendPacketToClient(ServerPlayer player, P packet) {
        PGServices.PLATFORM.sendPacketToClient(player, packet);
    }

    public static <T> T getRandomFromList(List<T> list) {
        int i = list.size();
        int index = PGConstants.RANDOM.nextInt(i);
        return list.get(index);
    }
}
