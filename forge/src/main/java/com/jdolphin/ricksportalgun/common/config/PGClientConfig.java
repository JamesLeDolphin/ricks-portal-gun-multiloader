package com.jdolphin.ricksportalgun.common.config;


import net.minecraftforge.common.ForgeConfigSpec;

public class PGClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> disable_portal_gun_color_tint;


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
