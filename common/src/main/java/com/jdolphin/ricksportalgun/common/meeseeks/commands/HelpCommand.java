package com.jdolphin.ricksportalgun.common.meeseeks.commands;

import com.jdolphin.ricksportalgun.common.meeseeks.util.CommandNode;

import static com.jdolphin.ricksportalgun.common.meeseeks.util.CommandDispatcher.literal;

public class HelpCommand {

    public static CommandNode register() {
        return literal("how")
                .addLiteral(literal("to")
                        .addLiteral(literal("refuel")
                                .setArgumentChild(literal("portal gun?")
                                        .setExecutor((meeseeks, arguments) -> {
                                            System.out.println(arguments);
                                        }))));
    }
}
