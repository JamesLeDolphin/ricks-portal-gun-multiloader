package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.recipe.PortalGunWorkbenchRecipe;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class PGRecipeTypes {

    private static final Map<ResourceLocation, RecipeType<?>> ALL = new HashMap<>();

    public static final RecipeType<PortalGunWorkbenchRecipe> WORKBENCH_TYPE = register("portal_gun_workbench");

    private static  <T extends Recipe<?>> RecipeType<T> register(String name) {
        ResourceLocation loc = PGHelper.id(name);
        RecipeType<T> type = new RecipeType<>() {
            @Override
            public String toString() {
                return loc.toString();
            }
        };
        ALL.put(loc, type);
        return type;
    }

    public static void init(BiConsumer<RecipeType<?>, ResourceLocation> r) {
        for (var e : ALL.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

}
