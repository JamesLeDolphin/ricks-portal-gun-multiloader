package com.jdolphin.ricksportalgun.common.meeseeks.util;

public final class LiteralNode extends CommandNode {

    public LiteralNode(String name) {
        super(name);
    }

    @Override
    public boolean matches(String token) {
        return name.equals(token);
    }
}
