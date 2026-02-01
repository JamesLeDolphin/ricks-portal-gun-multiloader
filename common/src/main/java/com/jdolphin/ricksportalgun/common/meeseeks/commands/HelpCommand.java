package com.jdolphin.ricksportalgun.common.meeseeks.commands;

import com.jdolphin.ricksportalgun.common.meeseeks.base.BoolArgumentCommand;
import com.jdolphin.ricksportalgun.common.meeseeks.base.IntArgumentCommand;
import com.jdolphin.ricksportalgun.common.meeseeks.base.LiteralCommand;
import com.jdolphin.ricksportalgun.common.meeseeks.base.MeeseeksCommandResult;

public class HelpCommand extends LiteralCommand {

    public HelpCommand() {
        super("How to");
        registerArguments();
    }

    public void registerArguments() {
        LiteralCommand cmd = new LiteralCommand("refuel portal gun?");
        cmd.setResult(new MeeseeksCommandResult((meeseeks, player) -> {
            meeseeks.sayToPlayer(player, "Your portal device can be refueled when you hold it in one hand, portal fluid in other hand and right click!");
            meeseeks.setTaskCompleted();
        }));
        this.addArgument(cmd);

        IntArgumentCommand argument = new IntArgumentCommand("<int>");
        BoolArgumentCommand bool = new BoolArgumentCommand("<bool>");

        bool.setResult(new MeeseeksCommandResult((meeseeks, player) ->
                meeseeks.sayToPlayer(player, String.format("%s and %s", argument.getValue(), bool.getValue()))));
        argument.addArgument(bool);
        this.addArgument(argument);


        this.addArgument(new LiteralCommand("test2"));
        this.addArgument(new LiteralCommand("test3"));
        this.addArgument(new LiteralCommand("test4"));
        this.addArgument(new LiteralCommand("tes5"));
        this.addArgument(new LiteralCommand("test6"));
        this.addArgument(new LiteralCommand("test7"));
        this.addArgument(new LiteralCommand("test6"));
        this.addArgument(new LiteralCommand("test9"));
        this.addArgument(new LiteralCommand("test10"));
    }
}
