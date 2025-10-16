package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record SBSecuritySettingsPacket(boolean lock, String name, String code, boolean selfDestruct) implements PGServerPayload {

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;

        ItemStack stack = player.getItemInHand(PGHelper.getPortalGunHand(player));
        CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean(PGNbtKeys.TAG_LOCK, lock);
        tag.putBoolean(PGNbtKeys.SELF_DESTRUCT, selfDestruct);

        if (!code.isEmpty()) {
            PortalGunItem.setCode(stack, this.code);
        }

        if (!name.isEmpty()) {
            Player newOwner = server.getPlayerList().getPlayerByName(name);
            if (newOwner != null) {
                tag.putUUID(PGNbtKeys.TAG_OWNER, newOwner.getUUID());
            } else {
                PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.locating.player.not_found", name));
            }
        }
        PGHelper.sendSuccessMsg(player, "notice.ricksportalgun.settings.applied");
    }

    public static SBSecuritySettingsPacket decode(FriendlyByteBuf buf) {
        boolean lock = buf.readBoolean();
        String name = buf.readUtf();
        String code = buf.readUtf();
        boolean selfDestruct = buf.readBoolean();
        return new SBSecuritySettingsPacket(lock, name, code, selfDestruct);
    }

    @Override
    public ResourceLocation getId() {
        return getID();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(lock);
        buf.writeUtf(name);
        buf.writeUtf(code);
        buf.writeBoolean(selfDestruct);
    }

    public static ResourceLocation getID() {
        return PGHelper.id("settings");
    }
}
