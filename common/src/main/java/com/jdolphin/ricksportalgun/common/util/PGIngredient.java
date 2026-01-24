package com.jdolphin.ricksportalgun.common.util;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record PGIngredient(Ingredient ingredient, int count, CompoundTag tag) {
    public static final PGIngredient EMPTY = new PGIngredient(Ingredient.EMPTY, 1, null);

    public boolean test(ItemStack stack) {
        if (!ingredient.test(stack)) return false;
        if (stack.getCount() < count) return false;

        if (tag != null) {
            CompoundTag stackTag = stack.getTag();
            if (stackTag == null) return false;

            for (String key : tag.getAllKeys()) {
                if (!stackTag.contains(key)) {
                    System.out.println(key);
                    return false;
                }
                if (!stackTag.get(key).equals(tag.get(key))) {
                    System.out.println(2);
                    return false;
                }
            }
        }

        return true;
    }

    public static PGIngredient fromJson(JsonObject json) {
        try {
            Ingredient ingredient = Ingredient.fromJson(json);
            int count = GsonHelper.getAsInt(json, "count", 1);
            CompoundTag nbt = null;
            if (json.has("nbt")) {
                nbt = TagParser.parseTag(json.get("nbt").toString());
            }
            return new PGIngredient(ingredient, count, nbt);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(json);
        }
        return PGIngredient.EMPTY;
    }

    public static PGIngredient fromNetwork(FriendlyByteBuf buf) {
        Ingredient ingredient = Ingredient.fromNetwork(buf);
        int count = buf.readVarInt();
        boolean hasTag = buf.readBoolean();
        CompoundTag nbt = null;
        if (hasTag) {
            nbt = buf.readNbt();
        }
        return new PGIngredient(ingredient, count, nbt);
    }

    public void toNetwork(FriendlyByteBuf buf) {
        ingredient.toNetwork(buf);
        buf.writeVarInt(count);
        boolean hasTag = tag != null;
        buf.writeBoolean(hasTag);
        if (hasTag) {
            buf.writeNbt(tag);
        }
    }
}
