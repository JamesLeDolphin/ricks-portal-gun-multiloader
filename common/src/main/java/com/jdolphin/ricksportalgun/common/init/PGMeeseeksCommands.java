package com.jdolphin.ricksportalgun.common.init;


import com.jdolphin.ricksportalgun.common.meeseeks.commands.HelpCommand;
import com.jdolphin.ricksportalgun.common.meeseeks.util.CommandNode;

import java.util.ArrayList;
import java.util.List;

public class PGMeeseeksCommands {
    public static final List<CommandNode> COMMANDS = new ArrayList<>();

    public static void init() {
        COMMANDS.add(HelpCommand.register());
    }
}
