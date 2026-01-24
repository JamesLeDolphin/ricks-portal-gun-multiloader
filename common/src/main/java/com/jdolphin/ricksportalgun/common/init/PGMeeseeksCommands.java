package com.jdolphin.ricksportalgun.common.init;


import com.jdolphin.ricksportalgun.common.meeseeks.commands.HelpCommand;

import static com.jdolphin.ricksportalgun.common.meeseeks.util.CommandDispatcher.literal;

public class PGMeeseeksCommands {

    public static void init() {

        HelpCommand.register(literal(""));
    }
}
