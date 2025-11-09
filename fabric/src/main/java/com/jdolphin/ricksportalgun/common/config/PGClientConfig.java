package com.jdolphin.ricksportalgun.common.config;


import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class PGClientConfig {

    public static final ClientConfig CLIENT_CONFIG;
    public static final ForgeConfigSpec SPEC;

    static {
        Pair<ClientConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder()
                .configure(ClientConfig::new);
        SPEC = pair.getRight();
        CLIENT_CONFIG = pair.getLeft();
    }

    public static class ClientConfig {
        private final ForgeConfigSpec.ConfigValue<Boolean> disable_portal_gun_color_tint;

        public ClientConfig(ForgeConfigSpec.Builder builder) {
            builder.push("Rick's Portal Gun client config");

            disable_portal_gun_color_tint = builder.comment("Disable showing portal color tint on portal guns").define("disable_portal_gun_color_tint", false);

            builder.pop();
        }

        public boolean disablePortalGunColorTint() {
            return disable_portal_gun_color_tint.get();
        }
    }
}
