package com.jdolphin.ricksportalgun.common.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class PGFoods {
    public static FoodProperties PORTAL_FLUID = new FoodProperties.Builder().nutrition(3).saturationModifier(0.1F)
            .alwaysEdible().build();

    public static final Consumable PORTAL_FLUID_CONSUMABLE = Consumables.defaultDrink().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.POISON))).build();
}
