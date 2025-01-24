package com.jdolphin.ricksportalgun.common.packets;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record SBChangePortalGunTypePacket(PortalGunType gunType) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, SBChangePortalGunTypePacket> CODEC = StreamCodec.composite(PortalGunType.PACKET_CODEC,
            SBChangePortalGunTypePacket::gunType, SBChangePortalGunTypePacket::new);

    public static final Type<SBChangePortalGunTypePacket> ID = new Type<>(Helper.createLocation("portal_gun_type"));

    public void handle(ServerPlayNetworking.Context context) {
        ServerPlayer player = context.player();

        ItemStack stack = player.getMainHandItem();
        PortalGunItem item = Helper.getPortalGun(stack);
        item.setPortalGunType(stack, gunType);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
