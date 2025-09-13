package com.jdolphin.ricksportalgun.common.init;

import net.minecraft.world.food.FoodProperties;

public class PGFoods {
    public static FoodProperties PORTAL_FLUID = new FoodProperties.Builder().nutrition(3).saturationMod(0.1F)
            .alwaysEat().build();
}
