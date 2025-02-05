package com.jdolphin.ricksportalgun.common.util.tint;

import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public record PortalGunDyeTint(int defaultColor) implements ItemTintSource {

    public static MapCodec<PortalGunDyeTint> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("default", Color.GREEN.getRGB()).forGetter(PortalGunDyeTint::defaultColor)).apply(instance, PortalGunDyeTint::new));

    public PortalGunDyeTint(int defaultColor) {
        this.defaultColor = defaultColor;
    }

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        return stack.has(PGDataComponents.GUN_DYE) ? stack.get(PGDataComponents.GUN_DYE) : defaultColor;
    }

    @Override
    public @NotNull MapCodec<? extends ItemTintSource> type() {
        return CODEC;
    }
}
