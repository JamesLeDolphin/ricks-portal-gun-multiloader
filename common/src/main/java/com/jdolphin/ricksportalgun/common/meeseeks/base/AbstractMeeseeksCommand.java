package com.jdolphin.ricksportalgun.common.meeseeks.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractMeeseeksCommand {
    protected String name;
    protected List<AbstractMeeseeksCommand> children = new ArrayList<>();
    protected Optional<AbstractMeeseeksCommand> parent = Optional.empty();
    protected Optional<MeeseeksCommandResult> result = Optional.empty();

    public AbstractMeeseeksCommand(String name) {
        this.name = name;
    }

    public Optional<MeeseeksCommandResult> getResult() {
        return result;
    }

    public void setResult(MeeseeksCommandResult result) {
        this.result = Optional.of(result);
    }

    public List<AbstractMeeseeksCommand> getChildren() {
        return children;
    }

    public void setParent(AbstractMeeseeksCommand cmd) {
        parent = Optional.of(cmd);
    }

    public Optional<AbstractMeeseeksCommand> getParent() {
        return parent;
    }

    public AbstractMeeseeksCommand addArgument(AbstractMeeseeksCommand argument) {
        argument.setParent(this);
        this.children.add(argument);
        return argument;
    }

    public boolean completesCommand() {
        return children.isEmpty() || result.isPresent();
    }

    public String getName() {
        return name;
    }

    public int getChildDepth() {
        return getChildDepth(0);
    }

    private int getChildDepth(int i) {
        int max = 0;
        for (AbstractMeeseeksCommand c : getChildren()) {

            max = Math.max(max, 1 + c.getChildDepth(i));
        }
        return max;
    }
}
