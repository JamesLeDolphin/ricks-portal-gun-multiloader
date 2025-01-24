package com.jdolphin.ricksportalgun.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.awt.*;

public class ExtraCodecs {
    public static Codec<Color> COLOR_CODEC = RecordCodecBuilder.create(inst ->
            inst.group(Codec.INT.fieldOf("red").forGetter(Color::getRed),
                    Codec.INT.fieldOf("green").forGetter(Color::getGreen),
                    Codec.INT.fieldOf("blue").forGetter(Color::getBlue)).apply(inst, Color::new));
}
