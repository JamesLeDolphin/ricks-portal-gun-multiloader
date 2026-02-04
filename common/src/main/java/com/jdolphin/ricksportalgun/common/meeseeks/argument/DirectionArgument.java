package com.jdolphin.ricksportalgun.common.meeseeks.argument;

import com.jdolphin.ricksportalgun.common.meeseeks.base.AbstractMeeseeksArgument;
import net.minecraft.core.Direction;

import java.util.Arrays;
import java.util.List;

public class DirectionArgument extends AbstractMeeseeksArgument<Direction> {

    public DirectionArgument(String name) {
        super(name, Direction.class);
    }

    @Override
    public Direction fromString(String string) {
        if (isValid(string)) return Direction.byName(string);
        return null;
    }

    @Override
    public boolean isValid(String s) {
        try {
            return Direction.byName(s) != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<String> values() {
        return Arrays.stream(Direction.values()).map(Direction::toString).toList();
    }
}
