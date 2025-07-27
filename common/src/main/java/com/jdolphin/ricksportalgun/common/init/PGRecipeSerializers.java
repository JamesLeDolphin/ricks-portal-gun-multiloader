package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.recipe.PortalGunWorkbenchRecipe;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class PGRecipeSerializers {
    private static final Map<ResourceLocation, RecipeSerializer<?>> ALL = new HashMap<>();

    public static final RecipeSerializer<PortalGunWorkbenchRecipe> WORKBENCH_SERIALIZER = register("portal_gun_workbench", new PortalGunWorkbenchRecipe.Serializer());

    private static  <T extends Recipe<?>> RecipeSerializer<T> register(String name, RecipeSerializer<T> serializer) {
        ALL.put(PGHelper.id(name), serializer);
        return serializer;
    }

    public static void init(BiConsumer<RecipeSerializer<?>, ResourceLocation> r) {
        for (var e : ALL.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }
}
