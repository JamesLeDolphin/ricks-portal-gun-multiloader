package com.jdolphin.ricksportalgun.common.meeseeks.base;

public class IntArgumentCommand extends AbstractArgumentCommand<Integer> {

    public IntArgumentCommand(String name) {
        super(name, Integer.class);
    }

    @Override
    public Integer fromString(String string) {
        return Integer.parseInt(string);
    }
}
