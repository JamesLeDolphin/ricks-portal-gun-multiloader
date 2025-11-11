package com.jdolphin.ricksportalgun.common.comp.immersive_portals;

import net.minecraft.world.entity.EntityType;
import qouteall.imm_ptl.core.platform_specific.IPRegistry;
import qouteall.imm_ptl.core.portal.Portal;

public class PortalHolder {

    public static EntityType<Portal> TYPE = IPRegistry.PORTAL.get();
}
