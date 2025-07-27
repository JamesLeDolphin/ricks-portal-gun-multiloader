package com.jdolphin.ricksportalgun.common.compat.jei;

import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.client.screen.workbench.WorkbenchCraftingScreen;
import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.recipe.PortalGunWorkbenchRecipe;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class PGJeiPlugin implements IModPlugin {
    public static final IRecipeType<PortalGunWorkbenchRecipe> TYPE = IRecipeType.create(PGConstants.MODID, "workbench", PortalGunWorkbenchRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return PGHelper.id("jei_plugin");
    }

    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new WorkbenchRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<PortalGunWorkbenchRecipe> recipes = new ArrayList<>();
        PGRecipeReceivedEvent.recipeHolders.forEach(recipeHolder -> {
            if (recipeHolder.value() instanceof PortalGunWorkbenchRecipe recipe) {
                recipes.add(recipe);
            }
        });
        if (!recipes.isEmpty()) registration.addRecipes(TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(TYPE, PGItems.PORTAL_GUN_WORKBENCH);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(WorkbenchCraftingScreen.class, 88, 36, 34, 46, TYPE);
    }
}
