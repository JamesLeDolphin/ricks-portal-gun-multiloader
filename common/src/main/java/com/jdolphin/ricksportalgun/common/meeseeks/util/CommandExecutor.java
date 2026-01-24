package com.jdolphin.ricksportalgun.common.meeseeks.util;

import com.jdolphin.ricksportalgun.common.entity.MeeseeksEntity;

import java.util.Map;

@FunctionalInterface
public interface CommandExecutor {
    void execute(MeeseeksEntity meeseeks, Map<String, String> arguments);
}
