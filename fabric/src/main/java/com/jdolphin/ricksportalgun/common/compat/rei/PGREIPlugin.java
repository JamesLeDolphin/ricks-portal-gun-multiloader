package com.jdolphin.ricksportalgun.common.compat.rei;

import com.jdolphin.ricksportalgun.client.screen.workbench.WorkbenchCraftingScreen;
import com.jdolphin.ricksportalgun.common.init.PGBlocks;
import com.jdolphin.ricksportalgun.common.recipe.PortalGunWorkbenchRecipe;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;

public class PGREIPlugin implements REIClientPlugin {

    public void registerCategories(CategoryRegistry registry) {
        registry.add(new PGWorkbenchCategory());

        registry.addWorkstations(PGWorkbenchCategory.WORKBENCH, EntryStacks.of(PGBlocks.GUN_WORKBENCH));
    }

    public void registerDisplays(DisplayRegistry registry) {
        registry.beginFiller(PortalGunWorkbenchRecipe.class).fill((PGWorkbenchDisplay::new));
    }

    public void registerScreens(ScreenRegistry registry) {
        registry.registerContainerClickArea(new Rectangle(128, 64, 128, 64), WorkbenchCraftingScreen.class, PGWorkbenchCategory.WORKBENCH);
    }
}
