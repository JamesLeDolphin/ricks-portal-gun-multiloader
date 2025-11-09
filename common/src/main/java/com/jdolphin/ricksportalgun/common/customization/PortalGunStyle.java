package com.jdolphin.ricksportalgun.common.customization;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.FastColor;

import java.awt.*;

public record PortalGunStyle(int highlightColor, int bgColor, int textColor) {
    public static Codec<PortalGunStyle> CODEC;
    public static final PortalGunStyle DEFAULT;

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("HighlightColor", highlightColor);
        tag.putInt("bgColor", bgColor);
        tag.putInt("textColor", textColor);
        return tag;
    }

    public static PortalGunStyle fromNBT(CompoundTag tag) {
        return new PortalGunStyle(tag.getInt("HighlightColor"), tag.getInt("bgColor"), tag.getInt("textColor"));
    }

    public ByteBuf toNetwork(ByteBuf buf) {
        return buf.writeInt(highlightColor).writeInt(bgColor).writeInt(textColor);
    }

    public static PortalGunStyle fromNetwork(ByteBuf buf) {
        return new PortalGunStyle(buf.readInt(), buf.readInt(), buf.readInt());
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("highlightColor").forGetter(PortalGunStyle::highlightColor),
                Codec.INT.fieldOf("bgColor").forGetter(PortalGunStyle::bgColor),
                Codec.INT.fieldOf("textColor").forGetter(PortalGunStyle::textColor))
                .apply(instance, PortalGunStyle::new));


        DEFAULT = new PortalGunStyle(FastColor.ARGB32.color(255,200, 0, 0), FastColor.ARGB32.color(255,100, 0, 0), Color.WHITE.getRGB());
    }
}
