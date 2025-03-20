package com.jdolphin.ricksportalgun.common.config;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PGCommonConfig extends ConfigBase {
    public static PGCommonConfig INSTANCE;

    public PGCommonConfig() {
        super("ricksportalgun-common");
        addConfig("disable_locating", "false");
        addConfig("randomizer_max", "1000");
        addConfig("disabled_dimensions", "");
        getOrCreateConfig();
    }

    public boolean disableLocating() {
        return Boolean.getBoolean(get("disable_locating"));
    }

    public int getRandomizerMax() {
        return Integer.parseInt(get("randomizer_max"));
    }

    public List<String> getDisabledDimensions() {
        String s = get("disabled_dimensions");
        List<String> strings = Stream.of(s.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        return strings;
    }

}
