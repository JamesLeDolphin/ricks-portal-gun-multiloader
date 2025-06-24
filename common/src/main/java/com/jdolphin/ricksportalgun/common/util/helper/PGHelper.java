package com.jdolphin.ricksportalgun.common.util.helper;

import com.google.common.collect.Lists;
import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.init.PGEntities;
import com.jdolphin.ricksportalgun.common.util.platform.PGServices;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PGHelper {
    public static MutableComponent COORDS_SET = Component.translatable("notice.ricksportalgun.destination.set");

    public static ResourceLocation createLocation(String string) {
        return ResourceLocation.fromNamespaceAndPath(PGConstants.MODID, string);
    }

    public static int seconds(int amount) {
        return 20 * amount;
    }

    public static int minutes(int amount) {
        return 60 * seconds(amount);
    }

    public static boolean canPlayerAccessGun(Player player, ItemStack stack) {
        boolean locked = stack.getOrDefault(PGDataComponents.LOCK, false);
        String uuid = stack.getOrDefault(PGDataComponents.OWNER, "");
        if (locked) {
            return player.getStringUUID().equals(uuid);
        }
        return true;
    }

    public static String getEntityAsString(EntityType<?> type) {
        ResourceLocation rl = BuiltInRegistries.ENTITY_TYPE.getKey(type);
        return  rl.toString();
    }

    public static List<String> defaultDisabledEntities() {
        return Lists.newArrayList(
                PGHelper.getEntityAsString(EntityType.ENDER_DRAGON),
                PGHelper.getEntityAsString(EntityType.WITHER),
                PGHelper.getEntityAsString(EntityType.WARDEN),
                PGHelper.getEntityAsString(PGEntities.PORTAL)
        );
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

    public static <P extends CustomPacketPayload> void sendPacketToClient(ServerPlayer player, P... packet) {
        PGServices.PLATFORM.sendPacketToClient(player, packet);
    }

    public static <T> T getRandomFromList(List<T> list) {
        int i = list.size();
        int index = PGConstants.RANDOM.nextInt(i);
        return list.get(index);
    }
}
