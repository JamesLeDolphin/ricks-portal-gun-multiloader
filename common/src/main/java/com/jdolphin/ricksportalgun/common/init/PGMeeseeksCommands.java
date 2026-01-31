package com.jdolphin.ricksportalgun.common.init;


import com.jdolphin.ricksportalgun.common.meeseeks.base.AbstractMeeseeksCommand;
import com.jdolphin.ricksportalgun.common.meeseeks.commands.HelpCommand;

import java.util.ArrayList;
import java.util.List;

public class PGMeeseeksCommands {
    public static final List<AbstractMeeseeksCommand> COMMANDS = new ArrayList<>();

    public static AbstractMeeseeksCommand HELP;

    public static void init() {
        HELP = register(new HelpCommand());
    }

    private static AbstractMeeseeksCommand register(AbstractMeeseeksCommand cmd) {
        COMMANDS.add(cmd);
        return cmd;
    }
}
