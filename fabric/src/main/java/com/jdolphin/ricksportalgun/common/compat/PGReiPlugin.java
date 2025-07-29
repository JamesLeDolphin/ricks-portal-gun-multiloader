package com.jdolphin.ricksportalgun.common.compat;

import com.jdolphin.ricksportalgun.RicksPortalGunFabricMain;
import com.jdolphin.ricksportalgun.client.screen.workbench.WorkbenchCraftingScreen;
import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.recipe.PortalGunWorkbenchRecipe;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;

import java.util.List;

public class PGReiPlugin implements REIClientPlugin {

    public void registerCategories(CategoryRegistry registry) {
        registry.add(new WorkbenchRecipeCategory(), configuration -> configuration.addWorkstations(EntryStacks.of(PGItems.PORTAL_GUN_WORKBENCH)));
    }

    public void registerDisplays(DisplayRegistry registry) {
        System.out.println(RicksPortalGunFabricMain.recipes);
        if (!RicksPortalGunFabricMain.recipes.isEmpty()) {
            RicksPortalGunFabricMain.recipes.forEach(recipeHolder -> {
                if (recipeHolder.value() instanceof PortalGunWorkbenchRecipe recipe) {
                    registry.add(new WorkbenchDisplay(List.of(EntryIngredients.ofItemStacks(recipe.getInputs())), List.of(EntryIngredients.of(recipe.getResult()))));
                }
            });
        }
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 59, ((screen.height - 166) / 2) + 27, 22, 15), WorkbenchCraftingScreen.class,
                WorkbenchDisplay.CATEGORY_IDENTIFIER);
    }

    @Override
    public void registerExclusionZones(ExclusionZones zones) {
        zones.register(WorkbenchCraftingScreen.class, screen -> List.of(
                new Rectangle(0, 0, screen.width, screen.height)
        ));
    }
}
