package com.jdolphin.ricksportalgun.common.comp.immersive_portals;

import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.math.Axis;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import qouteall.imm_ptl.core.api.PortalAPI;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.PortalManipulation;
import qouteall.q_misc_util.my_util.DQuaternion;

import java.awt.*;
import java.util.Map;


public class ImmersivePortalsHandler {
    public static EntityType<Portal> ENTITY_TYPE;

    public static void registerPortal(Map<ResourceLocation, EntityType<?>> registrationMap) {
        EntityType<Portal> portalType = EntityType.Builder.<Portal>of(SeeThroughPortalEntity::new, MobCategory.MISC)
                .sized(1.0F, 1.0F).fireImmune().clientTrackingRange(96).updateInterval(20).build("");

        registrationMap.put(PGHelper.id("dino_portal"), portalType);
        ENTITY_TYPE = portalType;
    }

    @SuppressWarnings("unchecked")
    public static InteractionResultHolder<ItemStack> spawnPortal(ItemStack stack, Level level, Vec3 origin, ResourceKey<Level> destinationLevel, Vec3 destinationCoord, float size, Direction playerFacing, Direction portalFacing) {
        SeeThroughPortalEntity portal = new SeeThroughPortalEntity(level, origin, portalFacing, playerFacing, size);

        CompoundTag tag = stack.getOrCreateTag();
        int color = tag.contains(PGNbtKeys.TAG_COLOR) ? tag.getInt(PGNbtKeys.TAG_COLOR) : Color.GREEN.getRGB();
        int lifetime = PGHelper.seconds(tag.contains(PGNbtKeys.TAG_AGE) ? tag.getInt(PGNbtKeys.TAG_AGE) : 10);
        portal.setDestinationDimension(destinationLevel);

        portal.setLifetime(lifetime);
        portal.setColor(color);

        Quaternionf quaternionf = Axis.YN.rotationDegrees(playerFacing.toYRot());
        if (portal.isFlat()) {
            quaternionf.mul(Axis.XN.rotationDegrees(portalFacing.equals(Direction.DOWN) ? -90 : 90));
            portal.setHeight(Math.max(2, size));
            if (portalFacing.equals(Direction.UP)) portal.setOriginPos(origin.add(0, 0.1, 0));
        }
        PortalAPI.setPortalOrientationQuaternion(portal, DQuaternion.fromMcQuaternion(quaternionf));
        PortalManipulation.makePortalRound(portal, 12);
        portal.setDestination(destinationCoord.add(0, size > 2 ? 1 : 0.5, 0));
        PortalManipulation.completeBiWayBiFacedPortal(portal, portal1 -> {}, portal1 -> {
            if (portal1 instanceof SeeThroughPortalEntity dinoPortal) {
                dinoPortal.setColor(color);
                dinoPortal.setLifetime(lifetime);
            }
        }, ENTITY_TYPE);

        level.addFreshEntity(portal);
        return InteractionResultHolder.success(stack);
    }
}
