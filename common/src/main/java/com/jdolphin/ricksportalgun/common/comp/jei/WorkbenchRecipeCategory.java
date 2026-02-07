package com.jdolphin.ricksportalgun.common.comp.jei;

import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.client.screen.workbench.WorkbenchCraftingScreen;
import com.jdolphin.ricksportalgun.common.init.PGBlocks;
import com.jdolphin.ricksportalgun.common.recipe.PortalGunWorkbenchRecipe;
import com.jdolphin.ricksportalgun.common.util.PGIngredient;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class WorkbenchRecipeCategory implements IRecipeCategory<PortalGunWorkbenchRecipe> {
    public static final ResourceLocation CRAFT_BG = PGHelper.id("textures/gui/workbench/crafting_jei.png");
    public static final RecipeType<PortalGunWorkbenchRecipe> TYPE = RecipeType.create(PGConstants.MODID, "workbench", PortalGunWorkbenchRecipe.class);
    private final IDrawable icon;
    private final IDrawable bg;
    private final IDrawableAnimated arrow;

    public WorkbenchRecipeCategory(IGuiHelper helper) {
        helper.createDrawable(WorkbenchCraftingScreen.CRAFT_BG, 0, 0, 124, 95);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, PGBlocks.GUN_WORKBENCH.asItem().getDefaultInstance());
        this.bg = helper.createDrawable(CRAFT_BG, 0, 0, 90, 97);
        this.arrow = helper.createAnimatedDrawable(helper.createDrawable(WorkbenchCraftingScreen.CRAFT_BG, 222, 210, 34, 46),
                400, IDrawableAnimated.StartDirection.TOP, false);
    }

    public void draw(PortalGunWorkbenchRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.arrow.draw(guiGraphics, 28, 19);
    }

    @SuppressWarnings("removal")
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public RecipeType<PortalGunWorkbenchRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("item.ricksportalgun.portal_gun_workbench");
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PortalGunWorkbenchRecipe recipe, IFocusGroup focuses) {
        List<PGIngredient> ingredients = recipe.getInputs();
        int size = ingredients.size();
        List<ItemStack> emptyStack = List.of(ItemStack.EMPTY);
        if (!ingredients.isEmpty()) {
            PGIngredient ingredient0 = ingredients.get(0);
            List<ItemStack> stacks0 = List.of(ingredient0.ingredient().getItems());
            stacks0.forEach(stack -> {
                stack.setCount(ingredient0.count());
                stack.setTag(ingredient0.tag());
            });
            builder.addSlot(RecipeIngredientRole.INPUT, 11, 14).addItemStacks(ingredients.get(0).ingredient().isEmpty() ? emptyStack : stacks0);

            if (size > 1) {
                PGIngredient ingredient = ingredients.get(1);
                List<ItemStack> stacks1 = List.of(ingredient.ingredient().getItems());
                stacks1.forEach(stack -> {
                    stack.setCount(ingredient.count());
                    stack.setTag(ingredient.tag());
                });
                builder.addSlot(RecipeIngredientRole.INPUT, 63, 14).addItemStacks(ingredients.get(1).ingredient().isEmpty() ? emptyStack : stacks1);
            }
            if (size > 2) {
                PGIngredient ingredient = ingredients.get(2);
                List<ItemStack> stacks2 = List.of(ingredient.ingredient().getItems());
                stacks2.forEach(stack -> {
                    stack.setCount(ingredient.count());
                    stack.setTag(ingredient.tag());
                });
                builder.addSlot(RecipeIngredientRole.INPUT, 11, 37).addItemStacks(ingredient.ingredient().isEmpty() ? emptyStack : stacks2);
            }
            if (size > 3) {
                PGIngredient ingredient = ingredients.get(3);
                List<ItemStack> stacks3 = List.of(ingredient.ingredient().getItems());
                stacks3.forEach(stack -> {
                    stack.setCount(ingredient.count());
                    stack.setTag(ingredient.tag());
                });
                builder.addSlot(RecipeIngredientRole.INPUT, 63, 37).addItemStacks(ingredients.get(3).ingredient().isEmpty() ? emptyStack : stacks3);
            }

            builder.addSlot(RecipeIngredientRole.OUTPUT, 37, 67).addItemStack(recipe.getResult());
        }
    }
}