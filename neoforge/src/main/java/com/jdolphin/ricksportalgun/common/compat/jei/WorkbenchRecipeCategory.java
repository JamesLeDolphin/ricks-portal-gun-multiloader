package com.jdolphin.ricksportalgun.common.compat.jei;

import com.jdolphin.ricksportalgun.client.screen.workbench.WorkbenchCraftingScreen;
import com.jdolphin.ricksportalgun.common.init.PGBlocks;
import com.jdolphin.ricksportalgun.common.recipe.PortalGunWorkbenchRecipe;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class WorkbenchRecipeCategory implements IRecipeCategory<PortalGunWorkbenchRecipe> {
    public static final ResourceLocation UID = PGHelper.id("workbench");
    public static final IRecipeType<PortalGunWorkbenchRecipe> TYPE = IRecipeType.create(UID, PortalGunWorkbenchRecipe.class);
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public WorkbenchRecipeCategory(IGuiHelper helper) {
        helper.createDrawable(WorkbenchCraftingScreen.CRAFT_BG, 0, 0, 124, 95);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, PGBlocks.GUN_WORKBENCH.asItem().getDefaultInstance());
        this.arrow = helper.createAnimatedDrawable(helper.createDrawable(WorkbenchCraftingScreen.CRAFT_BG, 0, 223, 66, 33),
                400, IDrawableAnimated.StartDirection.TOP, false);
    }

    public void draw(PortalGunWorkbenchRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.arrow.draw(guiGraphics, 11, 31);
    }

    @Override
    public IRecipeType<PortalGunWorkbenchRecipe> getRecipeType() {
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
        List<ItemStack> ingredients = recipe.getInputs();
        builder.addSlot(RecipeIngredientRole.INPUT, 6, 13).add(ingredients.get(0).isEmpty() ? ItemStack.EMPTY : ingredients.get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 36, 13).add(ingredients.get(1).isEmpty() ? ItemStack.EMPTY : ingredients.get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 66, 13).add(ingredients.get(2).isEmpty() ? ItemStack.EMPTY : ingredients.get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 96, 13).add(ingredients.get(3).isEmpty() ? ItemStack.EMPTY : ingredients.get(3));

         builder.addSlot(RecipeIngredientRole.OUTPUT, 36, 66).add(recipe.getResult());
    }
}