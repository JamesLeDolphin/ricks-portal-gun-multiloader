package com.jdolphin.ricksportalgun.common.meeseeks.base;

public class BoolArgumentCommand extends AbstractArgumentCommand<Boolean> {

    public BoolArgumentCommand(String name) {
        super(name, Boolean.class);
    }

    @Override
    public Boolean fromString(String string) {
        return Boolean.parseBoolean(string);
    }
}
