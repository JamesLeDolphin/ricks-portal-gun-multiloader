package com.jdolphin.ricksportalgun.common.meeseeks.base;

import com.jdolphin.ricksportalgun.common.entity.MeeseeksEntity;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiConsumer;

public class MeeseeksCommandResult {
    protected BiConsumer<MeeseeksEntity, Player> consumer;

    public MeeseeksCommandResult(BiConsumer<MeeseeksEntity, Player> consumer) {
        this.consumer = consumer;
    }

    public void runCommand(MeeseeksEntity entity, Player player) {
        consumer.accept(entity, player);
    }
}
