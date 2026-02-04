package com.jdolphin.ricksportalgun.common.meeseeks.commands;

import com.jdolphin.ricksportalgun.common.meeseeks.argument.BlockArgument;
import com.jdolphin.ricksportalgun.common.meeseeks.argument.DirectionArgument;
import com.jdolphin.ricksportalgun.common.meeseeks.argument.IntArgument;
import com.jdolphin.ricksportalgun.common.meeseeks.base.AbstractMeeseeksCommand;
import com.jdolphin.ricksportalgun.common.meeseeks.base.LiteralCommand;
import com.jdolphin.ricksportalgun.common.meeseeks.base.MeeseeksCommandResult;

public class MineCommand extends AbstractMeeseeksCommand {

    public MineCommand() {
        super("Mine");
        registerArguments();
    }

    private void registerArguments() {
        IntArgument intArgument = new IntArgument("<amount>");
        BlockArgument block = new BlockArgument("<block>");
        block.setResult(new MeeseeksCommandResult((meeseeks, player) -> {
            meeseeks.sayToPlayer(player, "Can do! Mining " + intArgument.getValue() + " " + block.getValue());
        }));
        intArgument.addArgument(block);
        this.addArgument(intArgument);

        DirectionArgument direction = new DirectionArgument("<direction>");
        IntArgument distance = new IntArgument("<distance>");
        direction.addArgument(distance);
        this.addArgument(direction);

        AbstractMeeseeksCommand bedrock = new LiteralCommand("to bedrock.");
        this.addArgument(bedrock);
    }
}
