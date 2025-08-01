package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.entity.ExplosiveItemEntity;
import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public record SBActivateSelfDestructPacket() implements PGPayload {
    public static final Type<SBActivateSelfDestructPacket> ID = new Type<>(PGHelper.id("activate_self_destruct"));
    public static final StreamCodec<FriendlyByteBuf, SBActivateSelfDestructPacket> CODEC = StreamCodec.unit(new SBActivateSelfDestructPacket());

    @Override
    public void handle(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        InteractionHand hand = PGHelper.getPortalGunHand(player);
        ItemStack stack = player.getItemInHand(hand);
        ItemStack copy = stack.copy();
        if (!player.isCreative()) stack.shrink(1);

        ItemEntity dropped = player.drop(copy, false);
        if (dropped != null) {
            ExplosiveItemEntity kaboom = new ExplosiveItemEntity(level, dropped.blockPosition(), copy);
            dropped.discard();
            level.addFreshEntity(kaboom);
            kaboom.setNeverPickUp();

        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
