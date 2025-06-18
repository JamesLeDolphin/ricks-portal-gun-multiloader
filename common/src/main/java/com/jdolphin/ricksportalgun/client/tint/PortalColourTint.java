package com.jdolphin.ricksportalgun.client.tint;

import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public record PortalColourTint(int defaultColor) implements ItemTintSource {

    public static MapCodec<PortalColourTint> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(Codec.INT.optionalFieldOf("default", Color.GREEN.getRGB()).forGetter(PortalColourTint::defaultColor)).apply(instance, PortalColourTint::new));

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        return ARGB.opaque(stack.getOrDefault(PGDataComponents.PORTAL_COLOUR, this.defaultColor));
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return CODEC;
    }
}
