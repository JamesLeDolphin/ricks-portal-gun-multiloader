package com.jdolphin.ricksportalgun.common.config;


import net.neoforged.neoforge.common.ModConfigSpec;

public class PGClientConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Boolean> disable_portal_gun_color_tint;


    public static boolean disablePortalGunColorTint() {
        return disable_portal_gun_color_tint.get();
    }

    static {
        BUILDER.push("Rick's Portal Gun client config");

        disable_portal_gun_color_tint = BUILDER.comment("Disable showing portal color tint on portal guns").define("disable_portal_gun_color_tint", false);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
