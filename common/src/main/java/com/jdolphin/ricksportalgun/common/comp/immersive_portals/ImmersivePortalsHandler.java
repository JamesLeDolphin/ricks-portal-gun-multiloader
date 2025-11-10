package com.jdolphin.ricksportalgun.common.comp.immersive_portals;

import com.jdolphin.ricksportalgun.common.util.platform.PGServices;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import qouteall.imm_ptl.core.portal.Portal;

public class ImmersivePortalsHandler {

    public static void spawnPortal(Level level, Vec3 origin, ResourceKey<Level> destinationLevel, Vec3 destinationCoord, float size) {
        Portal portal = (Portal) PGServices.PLATFORM.getPortalEntityType().create(level);
        if (portal != null) {
            portal.setOriginPos(origin);
            portal.setDestinationDimension(destinationLevel);
            portal.setDestination(destinationCoord.add(0, 0.5, 0));
            portal.setOrientationAndSize(new Vec3(1, 0, 0), // axisW
                    new Vec3(0, 1, 0), // axisH
                    size, // width
                    Math.max(size, 2));
            level.addFreshEntity(portal);
        } else System.out.println("AAAAAAAAAAAAAAAAAAAAA");
    }
}
