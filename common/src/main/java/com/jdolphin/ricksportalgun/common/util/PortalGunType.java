package com.jdolphin.ricksportalgun.common.util;

import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

import java.awt.*;

public record PortalGunType(Component name, ResourceLocation id, int color, int tints, ResourceLocation model) {
    public static Codec<PortalGunType> CODEC;
    public static final StreamCodec<ByteBuf, PortalGunType> PACKET_CODEC;
    public static final PortalGunType DEFAULT = new PortalGunType(Component.translatable("item.ricksportalgun.portal_gun"), PGHelper.createLocation("portal_gun"),
            Color.GREEN.getRGB(), 3,
            PGHelper.createLocation("portal_gun"));

    public PortalGunType(Component name, ResourceLocation id, int color, int tints, ResourceLocation model) {
        this.id = id;
        this.model = model;
        this.color = color;
        this.name = name;
        this.tints = tints;
    }


    public PortalGunType(Component name, ResourceLocation id, int tints, ResourceLocation model) {
        this(name, id, Color.GREEN.getRGB(), tints, model);
    }

    public PortalGunType(Component name, ResourceLocation id, ResourceLocation model) {
        this(name, id, Color.GREEN.getRGB(), 2, model);
    }

    static {
        CODEC = RecordCodecBuilder.create(instance ->
                instance.group(ComponentSerialization.CODEC.fieldOf("name").forGetter(PortalGunType::name),
                        ResourceLocation.CODEC.fieldOf("id").forGetter(PortalGunType::id),
                                ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("color", Color.GREEN.getRGB()).forGetter(PortalGunType::color),
                                Codec.INT.optionalFieldOf("tint_amount", 3)
                                        .validate(integer -> (integer >= 0 && integer <= 3) ? DataResult.success(integer) : DataResult.error(() -> "Tint amount must be between 0 and 3 inclusive!"))
                                        .forGetter(PortalGunType::tints),
                                ResourceLocation.CODEC.fieldOf("model").forGetter(PortalGunType::model))
                        .apply(instance, PortalGunType::new));

        PACKET_CODEC = ByteBufCodecs.fromCodec(CODEC);
    }
}