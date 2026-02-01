package com.jdolphin.ricksportalgun.common.meeseeks.base;

public class IntArgumentCommand extends AbstractArgumentCommand<Integer> {

    public IntArgumentCommand(String name) {
        super(name, Integer.class);
    }

    @Override
    public Integer fromString(String string) {
        if (isValid(string)) return Math.round(Float.parseFloat(string));
        return null;
    }

    @Override
    public boolean isValid(String s) {
        try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
