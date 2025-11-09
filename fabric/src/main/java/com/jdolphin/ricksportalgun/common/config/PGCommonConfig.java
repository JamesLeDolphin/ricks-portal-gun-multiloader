package com.jdolphin.ricksportalgun.common.config;


import com.google.common.collect.Lists;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class PGCommonConfig {

    public static final CommonConfig COMMON_CONFIG;
    public static final ForgeConfigSpec SPEC;

    static {
        Pair<CommonConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder()
                .configure(CommonConfig::new);
        SPEC = pair.getRight();
        COMMON_CONFIG = pair.getLeft();
    }

    public static class CommonConfig {
        private final ForgeConfigSpec.ConfigValue<Integer> randomizer_max;
        private final ForgeConfigSpec.ConfigValue<Boolean> disable_player_locating;
        private final ForgeConfigSpec.ConfigValue<Boolean> disable_biome_locating;
        private final ForgeConfigSpec.ConfigValue<Boolean> disable_structure_locating;
        private final ForgeConfigSpec.ConfigValue<List<? extends String>> disabled_dimensions;
        private final ForgeConfigSpec.ConfigValue<List<? extends String>> disabled_entities;

        public CommonConfig(ForgeConfigSpec.Builder builder) {
            builder.push("Rick's Portal Gun mod config");

            randomizer_max = builder.comment("Maximum distance for randomizer. Default = 10000").define("randomizer_max", 10000);
            disable_player_locating = builder.comment("Disable locating players").define("disable_player_locating", false);
            disable_biome_locating = builder.comment("Disable locating biomes").define("disable_biome_locating", false);
            disable_structure_locating = builder.comment("Disable locating structures").define("disable_structure_locating", false);
            disabled_dimensions = builder.comment("List of Dimension IDs the portal gun can't travel to, everything else is allowed",
                    "Separate every entry except the last one with commas").defineList("blacklisted_dimensions", Lists::newArrayList, String.class::isInstance);
            disabled_entities = builder.comment("List of Entity IDs that cannot travel through portals, everything else is allowed",
                    "Separate every entry except the last one with commas").worldRestart().defineList("blacklisted_entities", PGHelper.defaultDisabledEntities(), String.class::isInstance);
            builder.pop();
        }

        public List<? extends String> getBlacklistedDims() {
            return disabled_dimensions.get();
        }

        public List<? extends String> getBlacklistedEntities() {
            return disabled_entities.get();
        }

        public boolean disableBiomeLocating() {
            return disable_biome_locating.get();
        }

        public boolean disableStructureLocating() {
            return disable_structure_locating.get();
        }

        public boolean disablePlayerLocating() {
            return disable_player_locating.get();
        }

        public int getMaxRandomizerDistance() {
            return randomizer_max.get();
        }
    }


}
