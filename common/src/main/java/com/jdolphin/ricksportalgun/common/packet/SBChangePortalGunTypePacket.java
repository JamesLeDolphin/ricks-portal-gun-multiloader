package com.jdolphin.ricksportalgun.common.packet;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import com.jdolphin.ricksportalgun.common.util.helper.Helper;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record SBChangePortalGunTypePacket(PortalGunType gunType) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, SBChangePortalGunTypePacket> CODEC = StreamCodec.composite(PortalGunType.PACKET_CODEC,
            SBChangePortalGunTypePacket::gunType, SBChangePortalGunTypePacket::new);
    public static final Type<SBChangePortalGunTypePacket> ID = new Type<>(Helper.createLocation("portal_gun_type"));

    public SBChangePortalGunTypePacket(PortalGunType gunType) {
        this.gunType = gunType;
    }

    public SBChangePortalGunTypePacket(FriendlyByteBuf buf) {
        this(PortalGunType.PACKET_CODEC.decode(buf));
    }

    public void encode(FriendlyByteBuf buf) {
        PortalGunType.PACKET_CODEC.encode(buf, this.gunType);
    }

    public void handle(ServerPlayer player) {
        ItemStack stack = player.getMainHandItem();
        PortalGunItem item = Helper.getPortalGun(stack);
        item.setPortalGunType(stack, gunType);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
