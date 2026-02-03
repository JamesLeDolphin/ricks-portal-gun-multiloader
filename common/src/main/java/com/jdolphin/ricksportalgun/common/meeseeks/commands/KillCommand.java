package com.jdolphin.ricksportalgun.common.meeseeks.commands;

import com.jdolphin.ricksportalgun.common.meeseeks.base.*;
import net.minecraft.world.entity.EntityType;

public class KillCommand extends AbstractMeeseeksCommand {

    public KillCommand() {
        super("Kill");
        registerArguments();
    }

    private void registerArguments() {
        EntityTypeArgument entity = new EntityTypeArgument("<entity>", EntityType.PLAYER);
        IntArgument intArgument = new IntArgument("<amount>");
        entity.setResult(new MeeseeksCommandResult((meeseeks, player) ->
                meeseeks.sayToPlayer(player, "Can do! Killing " + intArgument.getValue() + " " + entity.getValue())));
        entity.addArgument(intArgument);
        this.addArgument(entity);

        StringArgument player = new PlayerArgument("<player>");
        this.addArgument(player);

        AbstractMeeseeksCommand everyone = new LiteralCommand("everyone.");
        this.addArgument(everyone);

        AbstractMeeseeksCommand suicide = new LiteralCommand("yourself.");
        this.addArgument(suicide);
    }
}
