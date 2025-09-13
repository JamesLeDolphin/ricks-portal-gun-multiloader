package com.jdolphin.ricksportalgun.common.util.helper;

import com.google.common.collect.Lists;
import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.platform.PGServices;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PGHelper {
    public static MutableComponent COORDS_SET = Component.translatable("notice.ricksportalgun.destination.set");

    public static ResourceLocation id(String string) {
        return new ResourceLocation(PGConstants.MODID, string);
    }

    public static ResourceLocation vanilla(String string) {
        return new ResourceLocation("minecraft", string);
    }

    public static boolean checkTagBoolean(CompoundTag tag, String key) {
        return tag.contains(key) && tag.getBoolean(key);
    }

    public static int seconds(int amount) {
        return 20 * amount;
    }

    public static int minutes(int amount) {
        return 60 * seconds(amount);
    }

    public static boolean canPlayerAccessGun(Player player, ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        boolean locked = tag.contains(PGNbtKeys.TAG_LOCK) && tag.getBoolean(PGNbtKeys.TAG_LOCK);
        String uuid = tag.contains(PGNbtKeys.TAG_OWNER) ? tag.getUUID(PGNbtKeys.TAG_OWNER).toString() : "";

        if (locked) {
            return player.getStringUUID().equals(uuid);
        }
        return true;
    }

    public static String getEntityAsString(EntityType<?> type) {
        ResourceLocation rl = BuiltInRegistries.ENTITY_TYPE.getKey(type);
        return  rl.toString();
    }

    public static InteractionHand getPortalGunHand(Player player) {
        InteractionHand hand;
        if (!player.getMainHandItem().is(PGTags.Items.PORTAL_GUNS)) {
            hand = InteractionHand.OFF_HAND;
        } else hand = InteractionHand.MAIN_HAND;
        return hand;
    }

    public static InteractionHand getOppositeHand(InteractionHand hand) {
        return hand.equals(InteractionHand.MAIN_HAND) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
    }

    public static List<String> defaultDisabledEntities() {
        return Lists.newArrayList("minecraft:ender_dragon",
                "minecraft:wither",
                "minecraft:warden",
                "ricksportalgun:portal"
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

    public static <P extends PGPayload> void sendPacketToServer(P packet) {
        PGServices.PLATFORM.sendPacketToServer(packet);
    }

    public static <P extends PGPayload> void sendPacketToClient(ServerPlayer player, P... packet) {
        PGServices.PLATFORM.sendPacketToClient(player, packet);
    }

    public static <T> T getRandomFromList(List<T> list) {
        int i = list.size();
        int index = PGConstants.RANDOM.nextInt(i);
        return list.get(index);
    }
}