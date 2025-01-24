package com.jdolphin.ricksportalgun.common.init;

import com.google.common.collect.Lists;
import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PortalGunTypeRegistry {
    public static final ArrayList<PortalGunType> PORTAL_GUN_TYPES = Lists.newArrayList();

    public static PortalGunType getPortalGunType(ItemStack stack) {
        return stack.getOrDefault(PGDataComponents.PORTAL_GUN_TYPE, PORTAL_GUN_TYPES.getFirst());
    }

    public static void add(PortalGunType type) {
        PORTAL_GUN_TYPES.add(type);
    }

    public static PortalGunType getPortalGunType(ResourceLocation id) {
        for (PortalGunType portalGunType : PORTAL_GUN_TYPES) {
            if (id.equals(portalGunType.id())) {
                return portalGunType;
            }
        }
        return PORTAL_GUN_TYPES.getFirst();
    }
}
