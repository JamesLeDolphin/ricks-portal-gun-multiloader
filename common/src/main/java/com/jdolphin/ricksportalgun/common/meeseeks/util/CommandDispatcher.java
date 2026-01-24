package com.jdolphin.ricksportalgun.common.meeseeks.util;

import com.jdolphin.ricksportalgun.common.entity.MeeseeksEntity;

import java.util.HashMap;
import java.util.Map;

public final class CommandDispatcher {

    private final CommandNode root = new LiteralNode("<root>");

    public CommandNode getRoot() {
        return root;
    }

    public void execute(String input, MeeseeksEntity entity) throws CommandSyntaxException {
        String[] tokens = input.split("\\s+");
        Map<String, String> arguments = new HashMap<>();

        CommandNode current = root;

        for (String token : tokens) {
            CommandNode next = current.literals.get(token);

            if (next != null) {
                current = next;
                continue;
            }

            if (current.argumentChild != null) {
                arguments.put(current.argumentChild.name, token);
                current = current.argumentChild;
                continue;
            }

            throw new CommandSyntaxException("Unknown or invalid command");
        }

        if (!current.isExecutable()) {
            throw new CommandSyntaxException("Incomplete command");
        }

        current.executor.execute(entity, arguments);
    }

    public static LiteralNode literal(String name) {
        return new LiteralNode(name);
    }

    public static ArgumentNode argument(String name) {
        return new ArgumentNode(name);
    }

}
