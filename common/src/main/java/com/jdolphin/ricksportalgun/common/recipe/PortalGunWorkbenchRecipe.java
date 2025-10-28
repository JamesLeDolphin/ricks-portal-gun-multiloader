package com.jdolphin.ricksportalgun.common.recipe;

import com.jdolphin.ricksportalgun.common.init.PGRecipeSerializers;
import com.jdolphin.ricksportalgun.common.init.PGRecipeTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public class PortalGunWorkbenchRecipe implements Recipe<WorkbenchRecipeInput> {
    private final List<ItemStack> items;
    final ItemStack result;

    public PortalGunWorkbenchRecipe(List<ItemStack> itemStacks, ItemStack result) {
        this.items = itemStacks; //Temp
        this.result = result;
    }

    public ItemStack getResult() {
        return result;
    }

    public List<ItemStack> getInputs() {
        return this.items;
    }

    @Override
    public boolean matches(WorkbenchRecipeInput input, Level level) {
        if (!level.isClientSide) {
            if (input.ingredientCount() == this.items.size()) {
                for (int i = 0; i < this.items.size(); i++) {
                    ItemStack stack = input.getItem(i);
                    ItemStack ingredient = this.items.get(i);;
                    if (stack.getCount() < ingredient.getCount()) {
                        return false;
                    }
                    if (!ItemStack.isSameItemSameComponents(stack, ingredient)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(WorkbenchRecipeInput workbenchRecipeInput, HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result;
    }

    @Override
    public RecipeSerializer<? extends Recipe<WorkbenchRecipeInput>> getSerializer() {
        return PGRecipeSerializers.WORKBENCH_SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<WorkbenchRecipeInput>> getType() {
        return PGRecipeTypes.WORKBENCH_TYPE;
    }

    public static class Serializer implements RecipeSerializer<PortalGunWorkbenchRecipe> {

        private static final MapCodec<PortalGunWorkbenchRecipe> CODEC;

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
            CODEC = RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(ItemStack.STRICT_CODEC.listOf(1, 4).fieldOf("ingredients").forGetter((recipe) -> recipe.items),
                                    ItemStack.CODEC.fieldOf("result").forGetter((recipe) -> recipe.result))
                            .apply(instance, PortalGunWorkbenchRecipe::new));

            STREAM_CODEC = StreamCodec.composite(ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), (recipe) -> recipe.items,
                    ItemStack.STREAM_CODEC, (recipe) -> recipe.result, PortalGunWorkbenchRecipe::new);

        }
    }
}
