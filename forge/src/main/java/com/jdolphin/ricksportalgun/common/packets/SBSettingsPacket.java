package com.jdolphin.ricksportalgun.common.packets;

import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class SBSettingsPacket {
    protected boolean lock;
    protected String  owner;
    protected boolean big;

    public SBSettingsPacket(boolean lock, String owner) {
        this.lock = lock;
        this.owner = owner;
    }

    public SBSettingsPacket(FriendlyByteBuf buf) {
        this.lock = buf.readBoolean();
        this.owner = buf.readUtf();
        this.big = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.lock);
        buf.writeUtf(this.owner);
        buf.writeBoolean(this.big);
    }

    public boolean handle(CustomPayloadEvent.Context context) {

        ServerPlayer player = context.getSender();
        assert player != null;

        try {
            ServerLevel world = player.serverLevel();
            MinecraftServer server = world.getServer();
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            Player newOwner = server.getPlayerList().getPlayerByName(this.owner);
            if (newOwner != null) {
                stack.set(PGDataComponents.OWNER, newOwner.getUUID().toString());
                stack.set(PGDataComponents.LOCK, this.lock);
                player.sendSystemMessage(Component.translatable("notice.ricksportalgun.settings.applied").withStyle(ChatFormatting.GREEN));
                return true;
            } else player.sendSystemMessage(Component.translatable("notice.ricksportalgun.settings.player_not_found").withStyle(ChatFormatting.RED));
        } catch (NullPointerException err) {
            err.printStackTrace();
            return false;
        }
        return false;
    }
}