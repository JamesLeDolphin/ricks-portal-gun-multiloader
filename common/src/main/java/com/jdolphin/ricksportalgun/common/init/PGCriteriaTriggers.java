package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.advancement.PortalGunTrigger;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class PGCriteriaTriggers {

    public static Map<ResourceLocation, CriterionTrigger<?>> ALL = new HashMap<>();

    public static PortalGunTrigger PORTAL_GUN_TRIGGER = register("portal_gun", new PortalGunTrigger());

    private static <T extends CriterionTrigger<?>> T register(String name, T trigger) {
        ALL.put(PGHelper.id(name), trigger);
        return trigger;
    }
}
