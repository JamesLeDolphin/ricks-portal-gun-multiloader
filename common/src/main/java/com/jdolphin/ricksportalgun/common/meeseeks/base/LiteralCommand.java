package com.jdolphin.ricksportalgun.common.meeseeks.base;

public class LiteralCommand extends AbstractMeeseeksCommand {

    public LiteralCommand(String name) {
        super(name);
    }

    public LiteralCommand literal(String string) {
        return new LiteralCommand(string);
    }
}
