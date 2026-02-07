package com.jdolphin.ricksportalgun.common.meeseeks.commands;

import com.jdolphin.ricksportalgun.common.meeseeks.argument.PlayerArgument;
import com.jdolphin.ricksportalgun.common.meeseeks.base.AbstractMeeseeksCommand;
import com.jdolphin.ricksportalgun.common.meeseeks.base.LiteralCommand;

public class DefendCommand extends AbstractMeeseeksCommand {

    public DefendCommand() {
        super("Defend");
        registerArguments();
    }

    private void registerArguments() {
        PlayerArgument player = new PlayerArgument("<player>");
        this.addArgument(player);

        LiteralCommand yourself = new LiteralCommand("yourself");
        this.addArgument(yourself);
    }
}
