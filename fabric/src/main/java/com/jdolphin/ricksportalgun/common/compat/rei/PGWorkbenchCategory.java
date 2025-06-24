package com.jdolphin.ricksportalgun.common.compat.rei;

import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;

public class PGWorkbenchCategory implements DisplayCategory<BasicDisplay> {

    public static final CategoryIdentifier<PGWorkbenchDisplay> WORKBENCH =
            CategoryIdentifier.of(PGHelper.createLocation("workbench"));

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return WORKBENCH;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("menu.ricksportalgun.workbench.craft");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(PGItems.PORTAL_GUN_WORKBENCH);
    }
}
