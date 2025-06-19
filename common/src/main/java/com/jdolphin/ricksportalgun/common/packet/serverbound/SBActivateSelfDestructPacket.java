package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.util.PGPayload;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

import java.util.concurrent.atomic.AtomicInteger;

public record SBActivateSelfDestructPacket() implements PGPayload {
    public static final Type<SBActivateSelfDestructPacket> ID = new Type<>(PGHelper.createLocation("activate_self_destruct"));
    public static final StreamCodec<FriendlyByteBuf, SBActivateSelfDestructPacket> CODEC = StreamCodec.unit(new SBActivateSelfDestructPacket());

    @Override
    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        ServerLevel level = player.serverLevel();
        ItemStack stack = player.getMainHandItem();
       ItemEntity itemEntity = player.drop(stack, false);
       if (itemEntity != null) {
           itemEntity.setNeverPickUp();
           AtomicInteger i = new AtomicInteger();
           server.addTickable(() -> {
               i.getAndIncrement();
               if (i.get() == PGHelper.seconds(10)) {
                   BlockPos pos = itemEntity.blockPosition();
                   boolean kaboom = level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
                   Level.ExplosionInteraction interaction = kaboom ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE;
                   level.explode(itemEntity, pos.getX(), pos.getY(), pos.getZ(), 5, interaction);
                   itemEntity.kill(level);
               }
           });
       }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
