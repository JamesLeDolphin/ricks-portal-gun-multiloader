package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record SBCustomizeSettingsPacket(double size, int age) implements PGServerPayload {

    @Override
    public void handle(ServerPlayer player) {
        ItemStack stack = player.getItemInHand(PGHelper.getPortalGunHand(player));
        CompoundTag tag = stack.getOrCreateTag();
        tag.putDouble(PGNbtKeys.TAG_SIZE, size);
        tag.putInt(PGNbtKeys.TAG_AGE, age);
    }

    public static SBCustomizeSettingsPacket decode(FriendlyByteBuf buf) {
        double size = buf.readDouble();
        int age = buf.readInt();
        return new SBCustomizeSettingsPacket(size, age);
    }

    @Override
    public ResourceLocation getId() {
        return getID();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(size);
        buf.writeInt(age);
    }

    public static ResourceLocation getID() {
        return PGHelper.id("portal_settings");
    }
}
