package com.jdolphin.ricksportalgun.common.meeseeks.util;

import java.util.HashMap;
import java.util.Map;

public abstract class CommandNode {
    protected final String name;
    protected final Map<String, CommandNode> literals = new HashMap<>();
    protected CommandNode argumentChild;
    protected CommandExecutor executor;

    protected CommandNode(String name) {
        this.name = name;
    }

    public CommandNode addLiteral(CommandNode node) {
        literals.put(node.name, node);
        return node;
    }

    public CommandNode setArgumentChild(CommandNode node) {
        this.argumentChild = node;
        return node;
    }

    public boolean isExecutable() {
        return executor != null;
    }

    public CommandNode setExecutor(CommandExecutor executor) {
        this.executor = executor;
        return this;
    }

    public abstract boolean matches(String token);
}
