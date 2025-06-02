package com.jdolphin.ricksportalgun.common.recipe;

import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorkbenchRecipeInput implements RecipeInput {
    private final int ingredientCount;
    private final List<ItemStack> items;
    private final StackedItemContents stackedContents = new StackedItemContents();

    public WorkbenchRecipeInput(List<ItemStack> stacks) {
        this.items = stacks;
        int i = 0;

        for (ItemStack itemstack : stacks) {
            if (!itemstack.isEmpty()) {
                ++i;

                this.stackedContents.accountStack(itemstack);
            }
        }
        ingredientCount = i;
    }

    public StackedItemContents stackedContents() {
        return this.stackedContents;
    }

    @Override
    public @NotNull ItemStack getItem(int i) {
        return this.items.get(i);
    }

    public int ingredientCount() {
        return this.ingredientCount;
    }

    @Override
    public int size() {
        return this.items.size();
    }
}
