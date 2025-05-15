package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record SBChangePortalGunTypePacket(PortalGunType gunType) implements PGPayload {
    public static final StreamCodec<FriendlyByteBuf, SBChangePortalGunTypePacket> CODEC = StreamCodec.composite(PortalGunType.PACKET_CODEC,
            SBChangePortalGunTypePacket::gunType, SBChangePortalGunTypePacket::new);
    public static final Type<SBChangePortalGunTypePacket> ID = new Type<>(PGHelper.createLocation("portal_gun_type"));

    public void handle(ServerPlayer player) {
        ItemStack stack = player.getMainHandItem();
        PortalGunItem item = PGHelper.getPortalGun(stack);
        item.setPortalGunType(stack, gunType);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
