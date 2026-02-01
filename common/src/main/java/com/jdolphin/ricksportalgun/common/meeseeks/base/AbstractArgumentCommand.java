package com.jdolphin.ricksportalgun.common.meeseeks.base;

public abstract class AbstractArgumentCommand<T> extends AbstractMeeseeksCommand {
    protected Class<T> argumentClass;
    protected String value;

    public AbstractArgumentCommand(String name, Class<T> argumentClass) {
        super(name);
        this.argumentClass = argumentClass;
    }

    public Class<T> getArgumentClass() {
        return argumentClass;
    }

    public abstract T fromString(String string);

    public abstract boolean isValid(String s);

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
