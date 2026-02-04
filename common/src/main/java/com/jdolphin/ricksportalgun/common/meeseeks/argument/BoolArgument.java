package com.jdolphin.ricksportalgun.common.meeseeks.argument;

import com.jdolphin.ricksportalgun.common.meeseeks.base.AbstractMeeseeksArgument;

import java.util.List;
import java.util.Locale;

public class BoolArgument extends AbstractMeeseeksArgument<Boolean> {

    public BoolArgument(String name) {
        super(name, Boolean.class);
    }

    @Override
    public Boolean fromString(String string) {
        if (isValid(string)) return Boolean.parseBoolean(string);
        return null;
    }

    @Override
    public boolean isValid(String string) {
        return string.toLowerCase(Locale.ROOT).equals("true") || string.toLowerCase(Locale.ROOT).equals("false");
    }

    @Override
    public List<String> values() {
        return List.of("true", "false");
    }
}
