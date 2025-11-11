package com.jdolphin.ricksportalgun.common.comp.immersive_portals;

import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.platform.PGServices;
import com.mojang.math.Axis;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import qouteall.imm_ptl.core.api.PortalAPI;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.PortalManipulation;
import qouteall.q_misc_util.my_util.DQuaternion;


public class ImmersivePortalsHandler {

    @SuppressWarnings("unchecked")
    public static InteractionResultHolder<ItemStack> spawnPortal(ItemStack stack, Level level, Vec3 origin, ResourceKey<Level> destinationLevel, Vec3 destinationCoord, float size, Direction playerFacing, Direction portalFacing) {
        SeeThroughPortalEntity portal = new SeeThroughPortalEntity(level, origin, portalFacing, playerFacing, size);

        portal.setDestinationDimension(destinationLevel);
        CompoundTag tag = stack.getOrCreateTag();
        portal.setLifetime(PGHelper.seconds(tag.contains(PGNbtKeys.TAG_AGE) ? tag.getInt(PGNbtKeys.TAG_AGE) : 10));

        Quaternionf quaternionf = Axis.YN.rotationDegrees(playerFacing.toYRot());
        if (portal.isFlat()) {
            quaternionf.mul(Axis.XN.rotationDegrees(portalFacing.equals(Direction.DOWN) ? -90 : 90));
            portal.setHeight(Math.max(2, size));
            if (portalFacing.equals(Direction.UP)) portal.setOriginPos(origin.add(0, 0.1, 0));
        }
        PortalAPI.setPortalOrientationQuaternion(portal, DQuaternion.fromMcQuaternion(quaternionf));
        PortalManipulation.makePortalRound(portal, 12);
        portal.setDestination(destinationCoord.add(0, size > 2 ? 1 : 0.5, 0));
        PortalManipulation.completeBiWayBiFacedPortal(portal, portal1 -> {}, portal1 -> {}, (EntityType<Portal>) PGServices.PLATFORM.getPortalEntityType());

        level.addFreshEntity(portal);
        return InteractionResultHolder.success(stack);
    }
}
