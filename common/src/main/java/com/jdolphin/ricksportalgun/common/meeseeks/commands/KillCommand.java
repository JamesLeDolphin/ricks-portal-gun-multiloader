package com.jdolphin.ricksportalgun.common.meeseeks.commands;

import com.jdolphin.ricksportalgun.common.meeseeks.argument.EntityTypeArgument;
import com.jdolphin.ricksportalgun.common.meeseeks.argument.IntArgument;
import com.jdolphin.ricksportalgun.common.meeseeks.argument.PlayerArgument;
import com.jdolphin.ricksportalgun.common.meeseeks.argument.StringArgument;
import com.jdolphin.ricksportalgun.common.meeseeks.base.AbstractMeeseeksCommand;
import com.jdolphin.ricksportalgun.common.meeseeks.base.LiteralCommand;
import com.jdolphin.ricksportalgun.common.meeseeks.base.MeeseeksCommandResult;
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

        StringArgument playerArg = new PlayerArgument("<player>");
        this.addArgument(playerArg);

        AbstractMeeseeksCommand everyone = new LiteralCommand("everyone.");
        this.addArgument(everyone);

        AbstractMeeseeksCommand suicide = new LiteralCommand("yourself.");
        suicide.setResult(new MeeseeksCommandResult((meeseeks, player) -> {
            meeseeks.kill();
        }));
        this.addArgument(suicide);
    }
}
