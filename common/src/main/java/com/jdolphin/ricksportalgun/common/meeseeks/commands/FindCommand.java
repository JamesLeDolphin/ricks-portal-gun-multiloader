package com.jdolphin.ricksportalgun.common.meeseeks.commands;

import com.jdolphin.ricksportalgun.common.meeseeks.argument.BlockArgument;
import com.jdolphin.ricksportalgun.common.meeseeks.argument.EntityTypeArgument;
import com.jdolphin.ricksportalgun.common.meeseeks.argument.ItemArgument;
import com.jdolphin.ricksportalgun.common.meeseeks.argument.StringArgument;
import com.jdolphin.ricksportalgun.common.meeseeks.base.AbstractMeeseeksCommand;

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
