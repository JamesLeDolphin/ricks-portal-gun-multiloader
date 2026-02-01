package com.jdolphin.ricksportalgun.common.meeseeks.base;

import java.util.Locale;

public class BoolArgumentCommand extends AbstractArgumentCommand<Boolean> {

    public BoolArgumentCommand(String name) {
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
}
