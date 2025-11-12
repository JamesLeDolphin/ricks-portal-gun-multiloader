package com.jdolphin.ricksportalgun.common.comp.emi;

import com.jdolphin.ricksportalgun.client.screen.workbench.WorkbenchCraftingScreen;
import com.jdolphin.ricksportalgun.common.recipe.PortalGunWorkbenchRecipe;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PGEmiRecipe implements EmiRecipe {
    private final ResourceLocation id;
    private final List<EmiIngredient> input;
    private final List<EmiStack> output;

    public PGEmiRecipe(PortalGunWorkbenchRecipe recipe) {
        ResourceLocation resultID = BuiltInRegistries.ITEM.getKey(recipe.getResult().getItem());
        this.id = PGHelper.id("/workbench/" + resultID.toString().replace(":", "/"));
        List<EmiIngredient> stacks = new ArrayList<>();
        for (ItemStack stack : recipe.getInputs()) {
            stacks.add(EmiStack.of(stack));
        }
        this.input = stacks;
        this.output = List.of(EmiStack.of(recipe.getResult()));
    }
    @Override
    public EmiRecipeCategory getCategory() {
        return PGEmiPlugin.WORKBENCH_CAT;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return this.input;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return this.output;
    }

    @Override
    public int getDisplayWidth() {
        return 124;
    }

    @Override
    public int getDisplayHeight() {
        return 95;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(WorkbenchCraftingScreen.CRAFT_BG, 49, 20, 34, 46, 76, 36);
        if (!input.isEmpty()) {
            int size = input.size();
            widgets.addSlot(input.get(0), 31, 14);
            widgets.addSlot(size > 1 ? input.get(1) : EmiIngredient.of(EmiStack.EMPTY.getEmiStacks()), 83, 14);
            widgets.addSlot(size > 2 ? input.get(2) : EmiIngredient.of(EmiStack.EMPTY.getEmiStacks()), 31, 37);
            widgets.addSlot(size > 3 ? input.get(3) : EmiIngredient.of(EmiStack.EMPTY.getEmiStacks()), 83, 37);
        }

        widgets.addSlot(output.getFirst(), 57, 67).recipeContext(this);
    }
}