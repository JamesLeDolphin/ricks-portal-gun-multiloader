package com.jdolphin.ricksportalgun.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class PortalGunWorkbenchRecipe implements Recipe<WorkbenchRecipeInput> {
    private final List<Ingredient> ingredients;
    final ItemStack result;
    private PlacementInfo placementInfo;

    public PortalGunWorkbenchRecipe(List<Ingredient> ingredients, ItemStack result) {
        this.ingredients = ingredients;
        this.result = result;
    }


    @Override
    public boolean matches(WorkbenchRecipeInput input, Level level) {
        if (input.ingredientCount() != this.ingredients.size()) return false;
        else {
            return input.size() == 1 && this.ingredients.size() == 1 ?
                    this.ingredients.getFirst().test(input.getItem(0)) : input.stackedContents().canCraft(this, null);
        }
    }

    @Override
    public ItemStack assemble(WorkbenchRecipeInput workbenchRecipeInput, HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<WorkbenchRecipeInput>> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<? extends Recipe<WorkbenchRecipeInput>> getType() {
        return Type.INSTANCE;
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.create(this.ingredients);
        }

        return this.placementInfo;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }


    public static class Type implements RecipeType<PortalGunWorkbenchRecipe> {
        public static final Type INSTANCE = new Type();
    }

    public static class Serializer implements RecipeSerializer<PortalGunWorkbenchRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        private static final MapCodec<PortalGunWorkbenchRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(Ingredient.CODEC.listOf(1, 4).fieldOf("ingredients").forGetter((recipe) -> recipe.ingredients),
                                ItemStack.STRICT_CODEC.fieldOf("result").forGetter((recipe) -> recipe.result))
                        .apply(instance, PortalGunWorkbenchRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, PortalGunWorkbenchRecipe> STREAM_CODEC;

        public Serializer() {}

        @Override
        public MapCodec<PortalGunWorkbenchRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PortalGunWorkbenchRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        static {
            STREAM_CODEC = StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), (recipe) -> recipe.ingredients,
                    ItemStack.STREAM_CODEC, (recipe) -> recipe.result, PortalGunWorkbenchRecipe::new);

        }
    }
}
