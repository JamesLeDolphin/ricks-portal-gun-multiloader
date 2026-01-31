package com.jdolphin.ricksportalgun.common.entity;

import com.jdolphin.ricksportalgun.common.init.PGEntities;
import com.jdolphin.ricksportalgun.common.packet.clientbound.CBOpenMeeseeksGuiPacket;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class MeeseeksEntity extends PathfinderMob {

    public MeeseeksEntity(EntityType<? extends MeeseeksEntity> entityType, Level level) {
        super(entityType, level);
    }

    public MeeseeksEntity(Level level) {
        super(PGEntities.MEESEEKS, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            PGHelper.sendPacketToClient(serverPlayer, new CBOpenMeeseeksGuiPacket(this.uuid));
        }
        return super.mobInteract(player, hand);
    }

    public void sayToPlayer(Player player, String msg) {
        player.displayClientMessage(Component.literal("<Meeseeks> " + msg), false);
    }

    public void setTaskCompleted() {
        System.out.println("Mission successful");
    }
}
