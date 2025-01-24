package com.jdolphin.ricksportalgun.common.packets;

import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class SBLocatePlayerPacket {
    String name;

    public SBLocatePlayerPacket(String playerName) {
        this.name = playerName;
    }

    public SBLocatePlayerPacket(FriendlyByteBuf buf) {
        this.name = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.name);
    }
    
    public boolean handle(CustomPayloadEvent.Context context) {

        ServerPlayer player = context.getSender();
        assert player != null;
        MinecraftServer server = player.getServer();

        try {
            ServerPlayer pl1 = server.getPlayerList().getPlayerByName(this.name);
            if (pl1 != null) {
                ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
                if (stack.is(PGTags.Items.PORTAL_GUNS)) {
                    PortalGunItem item = (PortalGunItem) stack.getItem();
                    item.setHopLocation(stack, pl1.level().dimension().location(), pl1.blockPosition());
                    player.sendSystemMessage(Component.translatable("notice.ricksportalgun.destination.set").withStyle(ChatFormatting.GREEN));
                }
            } else player.sendSystemMessage(Component.translatable("notice.ricksportalgun.player_locator.player_not_found", this.name).withStyle(ChatFormatting.RED));
            return true;
        } catch (NullPointerException err) {
            err.printStackTrace();
            return false;
        }
    }
}
