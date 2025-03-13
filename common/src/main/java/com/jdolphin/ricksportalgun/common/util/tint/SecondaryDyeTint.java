package com.jdolphin.ricksportalgun.common.util.tint;

import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public record SecondaryDyeTint(int defaultColor) implements ItemTintSource {

    public static MapCodec<SecondaryDyeTint> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(Codec.INT.optionalFieldOf("default", Color.WHITE.getRGB()).forGetter(SecondaryDyeTint::defaultColor)).apply(instance, SecondaryDyeTint::new));

    public SecondaryDyeTint(int defaultColor) {
        this.defaultColor = defaultColor;
    }

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        return ARGB.opaque(stack.getOrDefault(PGDataComponents.SECONDARY_DYE, defaultColor));
    }

    @Override
    public @NotNull MapCodec<? extends ItemTintSource> type() {
        return CODEC;
    }
}
