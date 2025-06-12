package com.jdolphin.ricksportalgun.common.util;

import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;

import java.awt.*;

public record PortalGunStyle(int highlightColor, int bgColor, int textColor) {
    public static Codec<PortalGunStyle> CODEC;
    public static StreamCodec<ByteBuf, PortalGunStyle> PACKET_CODEC;
    public static final PortalGunStyle DEFAULT;

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("highlightColor").forGetter(PortalGunStyle::highlightColor),
                Codec.INT.fieldOf("bgColor").forGetter(PortalGunStyle::bgColor),
                Codec.INT.fieldOf("textColor").forGetter(PortalGunStyle::textColor))
                .apply(instance, PortalGunStyle::new));

        PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.INT, PortalGunStyle::highlightColor, ByteBufCodecs.INT, PortalGunStyle::bgColor,
                ByteBufCodecs.INT, PortalGunStyle::textColor, PortalGunStyle::new);

        DEFAULT = new PortalGunStyle(ARGB.color(200, 0, 0), ARGB.color(100, 0, 0), Color.WHITE.getRGB());
    }
}
