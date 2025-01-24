package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import net.minecraft.sounds.SoundEvent;

public class PGSounds {

    public static final SoundEvent PORTAL_SHOOT = registerSoundEvent("item.portal_gun.shoot");

    private static SoundEvent registerSoundEvent(String name) {
        return SoundEvent.createVariableRangeEvent(Helper.createLocation(name));
    }

    public static void register() {}

}