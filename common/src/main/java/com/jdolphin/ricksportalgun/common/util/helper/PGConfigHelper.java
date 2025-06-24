package com.jdolphin.ricksportalgun.common.util.helper;

import com.jdolphin.ricksportalgun.common.util.platform.PGServices;

import java.util.List;

public class PGConfigHelper {

    public static int getRandomizerMax() {
        return PGServices.PLATFORM.getRandomizerMax();
    }

    public static boolean disablePlayerLocating() {
        return PGServices.PLATFORM.disablePlayerLocating();
    }

    public static boolean disableBiomeLocating() {
        return PGServices.PLATFORM.disableBiomeLocating();
    }

    public static boolean disableStructureLocating() {
        return PGServices.PLATFORM.disableStructureLocating();
    }

    public static List<? extends String> getDisabledDimensions() {
        return PGServices.PLATFORM.getDisabledDimensions();
    }

    public static List<? extends String> getDisabledEntities() {
        return PGServices.PLATFORM.getDisabledEntities();
    }

    public static boolean disablePortalColourTint() {
        return PGServices.PLATFORM.disablePortalGunColorTint();
    }
}
