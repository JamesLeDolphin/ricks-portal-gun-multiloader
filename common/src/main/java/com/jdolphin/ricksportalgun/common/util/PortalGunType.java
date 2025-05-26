package com.jdolphin.ricksportalgun.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

import java.awt.*;

public record PortalGunType(Component name, ResourceLocation id, int color, ResourceLocation model) {
    public static Codec<PortalGunType> CODEC;
    public static final StreamCodec<ByteBuf, PortalGunType> PACKET_CODEC;

    public PortalGunType(Component name, ResourceLocation id, int color, ResourceLocation model) {
        this.id = id;
        this.model = model;
        this.color = color;
        this.name = name;
    }


    public PortalGunType(Component name, ResourceLocation id, ResourceLocation model) {
        this(name, id, Color.GREEN.getRGB(), model);
    }

    static {
        CODEC = RecordCodecBuilder.create(instance ->
                instance.group(ComponentSerialization.CODEC.fieldOf("name").forGetter(PortalGunType::name),
                        ResourceLocation.CODEC.fieldOf("id").forGetter(PortalGunType::id),
                                ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("color", Color.GREEN.getRGB()).forGetter(PortalGunType::color),
                                ResourceLocation.CODEC.fieldOf("model").forGetter(PortalGunType::model))
                        .apply(instance, PortalGunType::new));

        PACKET_CODEC = ByteBufCodecs.fromCodec(CODEC);
    }
}