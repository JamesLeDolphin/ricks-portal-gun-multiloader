package com.jdolphin.ricksportalgun.common.util.tints;

import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public record PortalColourTint(int defaultColor) implements ItemTintSource {

    public static MapCodec<PortalColourTint> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("default", Color.GREEN.getRGB()).forGetter(PortalColourTint::defaultColor)).apply(instance, PortalColourTint::new));

    public PortalColourTint(int defaultColor) {
        this.defaultColor = defaultColor;
    }

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        int i = stack.getOrDefault(PGDataComponents.PORTAL_COLOUR, this.defaultColor);
        System.out.println("Colour " + i);
        return i;
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return CODEC;
    }
}
