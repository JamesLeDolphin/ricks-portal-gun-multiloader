package com.jdolphin.ricksportalgun.common.meeseeks.util;

public final class ArgumentNode extends CommandNode {

    public ArgumentNode(String name) {
        super(name);
    }

    @Override
    public boolean matches(String token) {
        return true; // accepts anything
    }
}
