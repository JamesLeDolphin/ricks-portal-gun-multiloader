package com.jdolphin.ricksportalgun.common.comp.emi;

import com.jdolphin.ricksportalgun.client.screen.workbench.WorkbenchCraftingScreen;
import com.jdolphin.ricksportalgun.common.recipe.PortalGunWorkbenchRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PGEmiRecipe implements EmiRecipe {
    private final ResourceLocation id;
    private final List<EmiIngredient> input;
    private final List<EmiStack> output;

    public PGEmiRecipe(PortalGunWorkbenchRecipe recipe) {
        this.id = recipe.getId();
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
    public @Nullable ResourceLocation getId() {
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

        int size = input.size();
        if (!input.isEmpty()) {
            widgets.addSlot(input.get(0), 31, 14);

            if (size > 1) {
                widgets.addSlot(input.get(1), 83, 14);
            }
            if (size > 2) {
                widgets.addSlot(input.get(2), 31, 37);
            }
            if (size > 3) {
                widgets.addSlot(input.get(3), 83, 37);
            }
        }

        widgets.addSlot(output.get(0), 57, 67).recipeContext(this);
    }
}
