package com.jdolphin.ricksportalgun.common.meeseeks.argument;

import com.jdolphin.ricksportalgun.common.meeseeks.base.AbstractMeeseeksArgument;

import java.util.List;

public class IntArgument extends AbstractMeeseeksArgument<Integer> {

    public IntArgument(String name) {
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

    @Override
    public List<String> values() {
        return List.of();
    }
}
