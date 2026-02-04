package com.jdolphin.ricksportalgun.common.meeseeks.argument;

import com.jdolphin.ricksportalgun.common.meeseeks.base.AbstractMeeseeksArgument;

import java.util.List;

public class StringArgument extends AbstractMeeseeksArgument<String> {

    public StringArgument(String name) {
        super(name, String.class);
    }

    @Override
    public String fromString(String string) {
        if (isValid(string)) return string;
        return null;
    }

    @Override
    public boolean isValid(String s) {
        return !s.isEmpty();
    }

    @Override
    public List<String> values() {
        return List.of();
    }
}
