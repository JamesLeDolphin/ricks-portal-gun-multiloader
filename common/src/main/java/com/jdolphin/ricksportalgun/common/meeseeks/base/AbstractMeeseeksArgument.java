package com.jdolphin.ricksportalgun.common.meeseeks.base;

import java.util.List;

public abstract class AbstractMeeseeksArgument<T> extends AbstractMeeseeksCommand {
    protected Class<T> argumentClass;
    protected String value;

    public AbstractMeeseeksArgument(String name, Class<T> argumentClass) {
        super(name);
        this.argumentClass = argumentClass;
    }

    public Class<T> getArgumentClass() {
        return argumentClass;
    }

    public abstract T fromString(String string);

    public abstract boolean isValid(String s);

    public abstract List<String> values();

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
