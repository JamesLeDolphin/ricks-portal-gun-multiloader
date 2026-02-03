package com.jdolphin.ricksportalgun.common.meeseeks.commands;

import com.jdolphin.ricksportalgun.common.meeseeks.base.*;

public class FindCommand extends AbstractMeeseeksCommand {

    public FindCommand() {
        super("Find");
        registerArguments();
    }

    private void registerArguments() {
        AbstractMeeseeksCommand player = new StringArgument("<player>");
        this.addArgument(player);

        AbstractMeeseeksCommand biome = new StringArgument("<biome>");
        this.addArgument(biome);

        AbstractMeeseeksCommand structure = new StringArgument("<structure>");
        this.addArgument(structure);

        BlockArgument block = new BlockArgument("<block>");
        this.addArgument(block);

        ItemArgument item = new ItemArgument("<item>");
        this.addArgument(item);

        EntityTypeArgument entityType = new EntityTypeArgument("<entity>");
        this.addArgument(entityType);
    }
}
