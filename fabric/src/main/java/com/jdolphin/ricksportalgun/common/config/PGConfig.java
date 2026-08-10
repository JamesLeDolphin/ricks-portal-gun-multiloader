package com.jdolphin.ricksportalgun.common.config;

import com.google.common.collect.Lists;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class PGConfig {
    public static final ForgeConfigSpec clientSpec;
    public static final Client CLIENT;

    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static final ForgeConfigSpec commonSpec;
    public static final Common COMMON;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {
        public final ForgeConfigSpec.ConfigValue<Integer> randomizer_max;
        public final ForgeConfigSpec.ConfigValue<Boolean> disable_player_locating;
        public final ForgeConfigSpec.ConfigValue<Boolean> disable_biome_locating;
        public final ForgeConfigSpec.ConfigValue<Boolean> disable_structure_locating;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> disabled_dimensions;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> disabled_entities;

        Common(ForgeConfigSpec.Builder builder) {
            builder.push("Rick's Portal Gun mod config");

            randomizer_max = builder.comment("Maximum distance for randomizer. Default = 10000").define("randomizer_max", 10000);
            disable_player_locating = builder.comment("Disable locating players").define("disable_player_locating", false);
            disable_biome_locating = builder.comment("Disable locating biomes").define("disable_biome_locating", false);
            disable_structure_locating = builder.comment("Disable locating structures").define("disable_structure_locating", false);
            disabled_dimensions = builder.comment("List of Dimension IDs the portal gun can't travel to, everything else is allowed",
                    "Separate every entry except the last one with commas").defineListAllowEmpty("blacklisted_dimensions", Lists.newArrayList(), String.class::isInstance);
            disabled_entities = builder.comment("List of Entity IDs that cannot travel through portals, everything else is allowed",
                    "Separate every entry except the last one with commas").worldRestart().defineList("blacklisted_entities", PGHelper.defaultDisabledEntities(), String.class::isInstance);
        }
    }

    public static class Client {
        public final ForgeConfigSpec.ConfigValue<Boolean> disable_portal_gun_color_tint;

        Client(ForgeConfigSpec.Builder builder) {
            builder.push("Rick's Portal Gun client config");

            disable_portal_gun_color_tint = builder.comment("Disable showing portal color tint on portal guns").define("disable_portal_gun_color_tint", false);
        }
    }
}
