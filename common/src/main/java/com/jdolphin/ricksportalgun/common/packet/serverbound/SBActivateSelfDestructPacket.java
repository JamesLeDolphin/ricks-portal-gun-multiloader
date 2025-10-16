package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.entity.ExplosiveItemEntity;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public record SBActivateSelfDestructPacket() implements PGServerPayload {

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
    public ResourceLocation getId() {
        return getID();
    }

    public static SBActivateSelfDestructPacket decode(FriendlyByteBuf buf) {
        return new SBActivateSelfDestructPacket();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {}

    public static ResourceLocation getID() {
        return PGHelper.id("activate_self_destruct");
    }
}
