package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.sounds.SoundEvent;

public class PGSounds {

    public static final SoundEvent PORTAL_SHOOT = registerSoundEvent("item.portal_gun.shoot");

    @SuppressWarnings("SameParameterValue")
    private static SoundEvent registerSoundEvent(String name) {
        return SoundEvent.createVariableRangeEvent(PGHelper.id(name));
    }

    public static void register() {}

}