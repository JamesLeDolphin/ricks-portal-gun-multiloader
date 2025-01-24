package com.jdolphin.ricksportalgun.common.packets;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record SBColourPacket(int colour) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, SBColourPacket> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, SBColourPacket::colour, SBColourPacket::new);
    public static final Type<SBColourPacket> ID = new Type<>(Helper.createLocation("color"));


    public void handle(ServerPlayNetworking.Context context) {
        ServerPlayer player = context.player();

        ItemStack stack = player.getMainHandItem();
        PortalGunItem item = Helper.getPortalGun(stack);
        item.setColor(stack, this.colour);
    }



    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
