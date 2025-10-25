package com.jdolphin.ricksportalgun.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jdolphin.ricksportalgun.common.blockentity.GunWorkbenchBlockEntity;
import com.jdolphin.ricksportalgun.common.init.PGRecipeSerializers;
import com.jdolphin.ricksportalgun.common.init.PGRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class PortalGunWorkbenchRecipe implements Recipe<Container> {
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
    public boolean matches(Container container, Level level) {
        if (!level.isClientSide && container instanceof GunWorkbenchBlockEntity workbench) {
            List<ItemStack> ingredients = workbench.ingredients();
            int count = ingredients.stream().toList().size();
            if (count == this.items.size()) {
                for (int i = 0; i < this.items.size(); i++) {
                    ItemStack stack = ingredients.get(i);
                    ItemStack ingredient = this.items.get(i);
                    if (stack.getCount() < ingredient.getCount()) {
                        return false;
                    }
                    if (!ItemStack.matches(stack, ingredient)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return null;
    }

    @Override
    public RecipeSerializer<? extends Recipe<Container>> getSerializer() {
        return PGRecipeSerializers.WORKBENCH_SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<Container>> getType() {
        return PGRecipeTypes.WORKBENCH_TYPE;
    }

    public static class Serializer implements RecipeSerializer<PortalGunWorkbenchRecipe> {

        public Serializer() {}

        @Override
        public PortalGunWorkbenchRecipe fromJson(ResourceLocation resourceLocation, JsonObject json) {
            ItemStack output = itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");

            NonNullList<ItemStack> inputs = NonNullList.withSize(4, ItemStack.EMPTY);
            for (int i = 0; i < ingredients.size(); i++) {
                inputs.set(i, itemStackFromJson(ingredients.get(i).getAsJsonObject()));
            }
            return new PortalGunWorkbenchRecipe(inputs, output);
        }

        public static ItemStack itemStackFromJson(JsonObject stackObject) {
            Item item = ShapedRecipe.itemFromJson(stackObject);
            ItemStack stack = item.getDefaultInstance();
            if (stackObject.has("data")) {
                JsonObject dataObject = stackObject.getAsJsonObject("data");
                if (dataObject.has("potion")) {
                    Potion potion = Potion.byName(GsonHelper.getAsString(dataObject, "potion"));
                    PotionUtils.setPotion(stack, potion);
                }
            } else {
                int i = GsonHelper.getAsInt(stackObject, "count", 1);
                if (i < 1) {
                    throw new JsonSyntaxException("Invalid output count: " + i);
                } else {
                    stack.setCount(i);
                }
            }
            return stack;
        }

        @Override
        public PortalGunWorkbenchRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buf) {
            NonNullList<ItemStack> inputs = NonNullList.withSize(buf.readInt(), ItemStack.EMPTY);

            inputs.replaceAll(ignored -> buf.readItem());

            ItemStack output = buf.readItem();
            return new PortalGunWorkbenchRecipe(inputs, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, PortalGunWorkbenchRecipe recipe) {
            buf.writeInt(recipe.items.size() - 1);

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buf);
            }
            buf.writeItem(recipe.getResultItem(null));
        }
    }
}
