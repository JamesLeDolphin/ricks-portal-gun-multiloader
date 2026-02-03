package com.jdolphin.ricksportalgun.common.init;


import com.jdolphin.ricksportalgun.common.meeseeks.base.AbstractMeeseeksCommand;
import com.jdolphin.ricksportalgun.common.meeseeks.commands.*;

import java.util.ArrayList;
import java.util.List;

public class PGMeeseeksCommands {
    public static final List<AbstractMeeseeksCommand> COMMANDS = new ArrayList<>();

    //TODO Make meeseeks box have in game config for what they're allowed to pick up

    //TODO: Check all command args have results
    public static AbstractMeeseeksCommand HELP = register(new HelpCommand());
    public static AbstractMeeseeksCommand MINE = register(new MineCommand());
    public static AbstractMeeseeksCommand FIND = register(new FindCommand());
    public static AbstractMeeseeksCommand BUILD = register(new BuildCommand());
    public static AbstractMeeseeksCommand KILL = register(new KillCommand());

    public static void init() {}

    private static AbstractMeeseeksCommand register(AbstractMeeseeksCommand cmd) {
        COMMANDS.add(cmd);
        return cmd;
    }
}
