package com.jdolphin.ricksportalgun.common.comp.immersive_portals;

import com.jdolphin.ricksportalgun.common.init.PGSounds;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.platform.PGServices;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import qouteall.imm_ptl.core.portal.GeometryPortalShape;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.q_misc_util.Helper;
import qouteall.q_misc_util.dimension.DimId;
import qouteall.q_misc_util.my_util.DQuaternion;

import java.awt.*;
import java.util.stream.Collectors;

public class SeeThroughPortalEntity extends Portal {
    private static final EntityDataAccessor<Integer> DATA_COLOR_ID = SynchedEntityData.defineId(SeeThroughPortalEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Direction> DATA_DIR = SynchedEntityData.defineId(SeeThroughPortalEntity.class, EntityDataSerializers.DIRECTION);
    private static final EntityDataAccessor<Direction> DATA_FACING = SynchedEntityData.defineId(SeeThroughPortalEntity.class, EntityDataSerializers.DIRECTION);
    private static final EntityDataAccessor<Integer> LIFETIME = SynchedEntityData.defineId(SeeThroughPortalEntity.class, EntityDataSerializers.INT);

    public static final String TAG_OPEN = "Open";
    public static final String TAG_NEW = "isSpawned";
    public static final String TAG_DIR = "Direction";
    public static final String TAG_FACING = "Facing";
    public static final String TAG_ACIDIC = "Bootleg";
    public static final String TAG_COLOR = "Color";

    private boolean bootleg;
    private boolean exists;

    public SeeThroughPortalEntity(EntityType<Portal> type, Level level) {
        super(type, level);
    }

    public SeeThroughPortalEntity(Level level, Vec3 pos, Direction direction, Direction facing, float size) {
        super(PGServices.PLATFORM.getPortalEntityType(), level);
        this.setPos(pos);
        setPortalDirection(direction);
        setPortalFacing(facing);
        this.setWidth(size);
        this.setHeight(Math.max(2, size));
        this.setOrientation(Vec3.atCenterOf(direction.getNormal()), Vec3.atCenterOf(facing.getNormal()));
        this.setOriginPos(pos);
    }

    public boolean isFlat() {
        Direction direction = this.entityData.get(DATA_DIR);
        return direction.equals(Direction.UP) || direction.equals(Direction.DOWN);
    }

    protected void defineSynchedData() {
        this.entityData.define(DATA_COLOR_ID, Color.GREEN.getRGB());
        this.entityData.define(DATA_DIR, Direction.SOUTH);
        this.entityData.define(DATA_FACING, Direction.SOUTH);
        this.entityData.define(LIFETIME, PGHelper.seconds(10));
    }

    public void setPortalDirection(Direction direction) {
        this.entityData.set(DATA_DIR, direction);
    }

    public void setPortalFacing(Direction direction) {
        this.entityData.set(DATA_FACING, direction);
    }

    public Direction getPortalFacing() {
        return this.entityData.get(DATA_FACING);
    }

    public Direction getPortalDirection() {
        return this.entityData.get(DATA_DIR);
    }


    public void setLifetime(int lifetime) {
        this.entityData.set(LIFETIME, lifetime);
    }

    public int getLifetime() {
        return this.entityData.get(LIFETIME);
    }

    public void setColor(int color) {
        this.entityData.set(DATA_COLOR_ID, color);
    }

    public int getColor() {
        return this.entityData.get(DATA_COLOR_ID);
    }

    protected void m_7378_(CompoundTag compoundTag) {
        this.width = compoundTag.getDouble("width");
        this.height = compoundTag.getDouble("height");
        this.axisW = Helper.getVec3d(compoundTag, "axisW").normalize();
        this.axisH = Helper.getVec3d(compoundTag, "axisH").normalize();
        this.dimensionTo = DimId.getWorldId(compoundTag, "dimensionTo", this.level().isClientSide);
        this.destination = Helper.getVec3d(compoundTag, "destination");
        this.specificPlayerId = Helper.getUuid(compoundTag, "specificPlayer");
        if (compoundTag.contains("specialShape")) {
            this.specialShape = new GeometryPortalShape(compoundTag.getList("specialShape", 6));
            if (!this.specialShape.normalized) {
                boolean shapeNormalized = compoundTag.getBoolean("shapeNormalized");
                this.specialShape.normalized = shapeNormalized;
            }

            this.specialShape.normalize(this.width, this.height);
            if (this.specialShape.triangles.isEmpty()) {
                this.specialShape = null;
            } else if (!this.specialShape.isValid()) {
                Helper.err("Portal shape invalid ");
                this.specialShape = null;
            }
        } else {
            this.specialShape = null;
        }

        if (compoundTag.contains("teleportable")) {
            this.teleportable = compoundTag.getBoolean("teleportable");
        }

        if (compoundTag.contains("rotationA")) {
            this.setRotationTransformationD(new DQuaternion(compoundTag.getDouble("rotationB"), compoundTag.getDouble("rotationC"), compoundTag.getDouble("rotationD"), compoundTag.getDouble("rotationA")));
        } else {
            this.rotation = null;
        }

        if (compoundTag.contains("interactable")) {
            this.setInteractable(compoundTag.getBoolean("interactable"));
        }

        if (compoundTag.contains("scale")) {
            this.scaling = compoundTag.getDouble("scale");
        }

        if (compoundTag.contains("teleportChangesScale")) {
            this.teleportChangesScale = compoundTag.getBoolean("teleportChangesScale");
        }

        if (compoundTag.contains("teleportChangesGravity")) {
            this.teleportChangesGravity = compoundTag.getBoolean("teleportChangesGravity");
        } else {
            this.teleportChangesGravity = true;
        }

        if (compoundTag.contains("portalTag")) {
            this.portalTag = compoundTag.getString("portalTag");
        }

        if (compoundTag.contains("fuseView")) {
            this.fuseView = compoundTag.getBoolean("fuseView");
        }

        if (compoundTag.contains("renderingMergable")) {
            this.renderingMergable = compoundTag.getBoolean("renderingMergable");
        }

        if (compoundTag.contains("hasCrossPortalCollision")) {
            this.hasCrossPortalCollision = compoundTag.getBoolean("hasCrossPortalCollision");
        }

        if (compoundTag.contains("commandsOnTeleported")) {
            ListTag list = compoundTag.getList("commandsOnTeleported", 8);
            this.commandsOnTeleported = list.stream().map(Tag::toString).collect(Collectors.toList());
        } else {
            this.commandsOnTeleported = null;
        }

        if (compoundTag.contains("doRenderPlayer")) {
            this.doRenderPlayer = compoundTag.getBoolean("doRenderPlayer");
        }

        if (compoundTag.contains("isVisible")) {
            this.visible = compoundTag.getBoolean("isVisible");
        } else {
            this.visible = true;
        }

        this.animation.readFromTag(compoundTag);
        readPortalDataSignal.emit(this, compoundTag);
        this.updateCache();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.m_7378_(tag);
        this.bootleg = tag.getBoolean(TAG_ACIDIC);
        this.setColor(tag.getInt(TAG_COLOR));
        this.setLifetime(tag.getInt(TAG_OPEN));
        this.exists = tag.getBoolean(TAG_NEW);
        setPortalDirection(Direction.byName(tag.getString(TAG_DIR)));
        setPortalFacing(Direction.byName(tag.getString(TAG_FACING)));
    }

    protected void m_7380_(CompoundTag compoundTag) {
        compoundTag.putDouble("width", this.width);
        compoundTag.putDouble("height", this.height);
        Helper.putVec3d(compoundTag, "axisW", this.axisW);
        Helper.putVec3d(compoundTag, "axisH", this.axisH);
        DimId.putWorldId(compoundTag, "dimensionTo", this.dimensionTo);
        Helper.putVec3d(compoundTag, "destination", this.getDestPos());
        if (this.specificPlayerId != null) {
            Helper.putUuid(compoundTag, "specificPlayer", this.specificPlayerId);
        }

        if (this.specialShape != null) {
            this.specialShape.normalize(this.width, this.height);
            compoundTag.put("specialShape", this.specialShape.writeToTag());
            compoundTag.putBoolean("shapeNormalized", true);
        }

        compoundTag.putBoolean("teleportable", this.teleportable);

        if (this.rotation != null) {
            compoundTag.putDouble("rotationA", this.rotation.w);
            compoundTag.putDouble("rotationB", this.rotation.x);
            compoundTag.putDouble("rotationC", this.rotation.y);
            compoundTag.putDouble("rotationD", this.rotation.z);
        }

        compoundTag.putBoolean("interactable", this.isInteractable());
        compoundTag.putDouble("scale", this.scaling);
        compoundTag.putBoolean("teleportChangesScale", this.teleportChangesScale);
        compoundTag.putBoolean("teleportChangesGravity", this.teleportChangesGravity);
        if (this.portalTag != null) {
            compoundTag.putString("portalTag", this.portalTag);
        }

        compoundTag.putBoolean("fuseView", this.fuseView);
        compoundTag.putBoolean("renderingMergable", this.renderingMergable);
        compoundTag.putBoolean("hasCrossPortalCollision", this.hasCrossPortalCollision);
        compoundTag.putBoolean("doRenderPlayer", this.doRenderPlayer);
        compoundTag.putBoolean("isVisible", this.visible);
        if (this.commandsOnTeleported != null) {
            ListTag list = new ListTag();

            for(String command : this.commandsOnTeleported) {
                list.add(StringTag.valueOf(command));
            }

            compoundTag.put("commandsOnTeleported", list);
        }

        this.animation.writeToTag(compoundTag);
        writePortalDataSignal.emit(this, compoundTag);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        this.m_7380_(tag);
        tag.putBoolean(TAG_ACIDIC, this.bootleg);
        tag.putBoolean(TAG_NEW, this.exists);
        tag.putInt(TAG_COLOR, this.getColor());
        tag.putInt(TAG_OPEN, this.getLifetime());
        tag.putString(TAG_DIR, getPortalDirection().getName());
        tag.putString(TAG_FACING, getPortalFacing().getName());
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            if (!this.exists) {
                LevelHelper.playSound(this.level(), this.blockPosition(), PGSounds.PORTAL_SHOOT, SoundSource.PLAYERS);
                this.exists = true;
            } else if (getLifetime() > 0) {
                setLifetime(getLifetime() - 1);
            }
            if (getLifetime() <= 0) this.kill();
        }
    }
}
