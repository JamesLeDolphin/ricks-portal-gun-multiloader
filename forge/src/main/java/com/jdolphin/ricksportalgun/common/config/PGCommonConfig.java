package com.jdolphin.ricksportalgun.common.config;


import com.google.common.collect.Lists;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class PGCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> randomizer_max;
    public static final ForgeConfigSpec.ConfigValue<Boolean> disable_player_locating;
    public static final ForgeConfigSpec.ConfigValue<Boolean> disable_biome_locating;
    public static final ForgeConfigSpec.ConfigValue<Boolean> disable_structure_locating;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> disabled_dimensions;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> disabled_entities;

    public static List<? extends String> getBlacklistedDims() {
        return disabled_dimensions.get();
    }

    public static List<? extends String> getBlacklistedEntities() {
        return disabled_entities.get();
    }

    public static boolean disableBiomeLocating() {
        return disable_biome_locating.get();
    }

    public static boolean disableStructureLocating() {
        return disable_structure_locating.get();
    }

    public static boolean disablePlayerLocating() {
        return disable_player_locating.get();
    }

    public static int getMaxRandomizerDistance() {
        return randomizer_max.get();
    }

    static {
        BUILDER.push("Rick's Portal Gun mod config");

        randomizer_max = BUILDER.comment("Maximum distance for randomizer. Default = 10000").define("randomizer_max", 10000);
        disable_player_locating = BUILDER.comment("Disable locating players").define("disable_player_locating", false);
        disable_biome_locating = BUILDER.comment("Disable locating biomes").define("disable_biome_locating", false);
        disable_structure_locating = BUILDER.comment("Disable locating structures").define("disable_structure_locating", false);
        disabled_dimensions = BUILDER.comment("List of Dimension IDs the portal gun can't travel to, everything else is allowed",
                "Separate every entry except the last one with commas").defineListAllowEmpty("blacklisted_dimensions", Lists.newArrayList(), String.class::isInstance);

        disabled_entities = BUILDER.comment("List of Entity IDs that cannot travel through portals, everything else is allowed",
                "Separate every entry except the last one with commas").worldRestart().defineList("blacklisted_entities", PGHelper.defaultDisabledEntities(), String.class::isInstance);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
