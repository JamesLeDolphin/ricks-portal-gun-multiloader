package com.jdolphin.ricksportalgun.common.comp.emi;

import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.init.PGRecipeTypes;
import com.jdolphin.ricksportalgun.common.recipe.PortalGunWorkbenchRecipe;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.world.item.crafting.RecipeManager;

@EmiEntrypoint
public class PGEmiPlugin implements EmiPlugin {
    public static final EmiStack WORKBENCH = EmiStack.of(PGItems.PORTAL_GUN_WORKBENCH);
    public static final EmiRecipeCategory WORKBENCH_CAT = new EmiRecipeCategory(PGHelper.id("workbench"),
            WORKBENCH, new EmiTexture(PGHelper.id("textures/gui/workbench/crafting_jei.png"), 0, 0, 124, 95));


    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(WORKBENCH_CAT);
        registry.addWorkstation(WORKBENCH_CAT, WORKBENCH);

        RecipeManager manager = registry.getRecipeManager();

        // Use vanilla's concept of your recipes and pass them to your EmiRecipe representation
        for (PortalGunWorkbenchRecipe recipe : manager.getAllRecipesFor(PGRecipeTypes.WORKBENCH_TYPE)) {
            registry.addRecipe(new PGEmiRecipe(recipe));
        }
    }
}
