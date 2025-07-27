package com.jdolphin.ricksportalgun.common.compat.jei;

import com.jdolphin.ricksportalgun.client.screen.workbench.WorkbenchCraftingScreen;
import com.jdolphin.ricksportalgun.common.recipe.PortalGunWorkbenchRecipe;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.List;

@JeiPlugin
public class PGJeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return PGHelper.id("jei_plugin");
    }

    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new WorkbenchRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(WorkbenchRecipeCategory.TYPE, List.of(new PortalGunWorkbenchRecipe(List.of(Items.APPLE.getDefaultInstance()), Items.CRAFTING_TABLE.getDefaultInstance())));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(WorkbenchCraftingScreen.class, 16, 49, 66, 33, WorkbenchRecipeCategory.TYPE);
    }
}
