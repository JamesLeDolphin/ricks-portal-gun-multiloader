package com.jdolphin.ricksportalgun.common.meeseeks.argument;

public class PlayerArgument extends StringArgument {

    public PlayerArgument(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String s) {
        if (s.length() > 16 || s.length() < 3) return false;
        return s.matches("[A-Za-z0-9_]+");
    }
}
