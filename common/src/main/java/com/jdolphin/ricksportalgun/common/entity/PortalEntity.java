package com.jdolphin.ricksportalgun.common.entity;

import com.jdolphin.ricksportalgun.common.init.PGDamageTypes;
import com.jdolphin.ricksportalgun.common.init.PGEntities;
import com.jdolphin.ricksportalgun.common.init.PGSounds;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGConfigHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PortalEntity extends Entity {
    private static final EntityDataAccessor<Integer> DATA_COLOR_ID = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Direction> DATA_DIR = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.DIRECTION);
    private static final EntityDataAccessor<Direction> DATA_FACING = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.DIRECTION);
    private static final EntityDataAccessor<Float> DATA_SIZE = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> LIFETIME = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.INT);

    public static final String TAG_DIMENSION = "PortalDimension";
    public static final String TAG_BPOS = "PortalPos";
    public static final String TAG_OPEN = "Open";
    public static final String TAG_NEW = "isSpawned";
    public static final String TAG_COOLDOWN = "Cooldown";
    public static final String TAG_DIR = "Direction";
    public static final String TAG_FACING = "Facing";
    public static final String TAG_SIZE = "Size";
    public static final String TAG_ACIDIC = "Bootleg";
    public static final String TAG_COLOR = "Color";

    private BlockPos targetPos;
    private boolean bootleg;
    private boolean exists;


    private Vec3 pos;
    private Vec3 targetVec;
    private String targetDim;
    private ServerLevel destinationLevel;
    private int delay = 0;

    public boolean exists() {
        return exists;
    }

    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    public PortalEntity(EntityType<PortalEntity> type, Level level) {
        super(type, level);
    }

    public PortalEntity(Level pLevel, Vec3 pos, Direction direction, Direction facing, float size) {
        super(PGEntities.PORTAL, pLevel);
        this.setPos(pos);
        setPortalDirection(direction);
        setPortalFacing(facing);
        setSize(size);
        this.pos = pos;
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

    public int getColor() {
        return this.entityData.get(DATA_COLOR_ID);
    }

    public float getSize() {
        return this.entityData.get(DATA_SIZE);
    }

    public void setSize(float size) {
        this.entityData.set(DATA_SIZE, size);
    }

    public boolean isBootleg() {
        return bootleg;
    }

    public void setBootleg(boolean bootleg) {
        this.bootleg = bootleg;
    }

    public static boolean colliding(Entity entity1, Entity entity2) {
        return entity1.getBoundingBox().intersects(entity2.getBoundingBox());
    }

    @Override
    public void kill() {
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float v) {
        return false;
    }

    public boolean isFlat() {
        Direction direction = this.entityData.get(DATA_DIR);
        return direction.equals(Direction.UP) || direction.equals(Direction.DOWN);
    }

    public void setHopLocation(ResourceLocation dimension, BlockPos pos) {
        this.targetDim = dimension.toString();
        this.targetPos = pos;
    }

    public BlockPos getHopLoc() {
        return this.targetPos == null ? BlockPos.ZERO : targetPos;
    }

    public String getHopDim() {
        return this.targetDim == null ? "minecraft:overworld" : this.targetDim;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.bootleg = tag.getBoolean(TAG_ACIDIC);
        this.targetDim = tag.getString(TAG_DIMENSION);
        CompoundTag bpTag = tag.getCompound(TAG_BPOS);
        this.targetPos = NbtUtils.readBlockPos(bpTag);
        this.setColor(tag.getInt(TAG_COLOR));
        this.setLifetime(tag.getInt(TAG_OPEN));
        this.delay = tag.getInt(TAG_COOLDOWN);
        this.exists = tag.getBoolean(TAG_NEW);
        setPortalDirection(Direction.byName(tag.getString(TAG_DIR)));
        setPortalFacing(Direction.byName(tag.getString(TAG_FACING)));
        setSize(tag.getFloat(TAG_SIZE));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putBoolean(TAG_ACIDIC, this.bootleg);
        tag.putBoolean(TAG_NEW, this.exists);
        tag.putString(TAG_DIMENSION, getHopDim());
        tag.put(TAG_BPOS, NbtUtils.writeBlockPos(getHopLoc()));
        tag.putInt(TAG_COLOR, this.getColor());
        tag.putInt(TAG_OPEN, this.getLifetime());
        tag.putInt(TAG_COOLDOWN, this.delay);
        tag.putString(TAG_DIR, getPortalDirection().getName());
        tag.putString(TAG_FACING, getPortalFacing().getName());
        tag.putFloat(TAG_SIZE, getSize());
    }

    @Override
    public void setPos(double x, double y, double z) {
        this.pos = new Vec3(x, y, z);
        this.setPosRaw(x, y, z);
        recalculateBoundingBox();
    }

    protected final void recalculateBoundingBox() {
        Direction direction = this.entityData.get(DATA_DIR);
        Direction facing = this.entityData.get(DATA_FACING);
        AABB aabb = calculateBoundingBox(this.pos, direction, facing);
        Vec3 vec3 = aabb.getCenter();
        this.setPosRaw(vec3.x, vec3.y, vec3.z);
        this.setBoundingBox(aabb);
    }

    protected AABB calculateBoundingBox(Vec3 vec3, Direction dir, Direction facing) {
        Direction.Axis axis = dir.getAxis();
        boolean flat = axis.equals(Direction.Axis.Y);
        double height = this.getSize() > 2 ? this.getSize() : 2;
        double d0 = axis.equals(Direction.Axis.X) ? 0.1 : this.getSize();
        double d1 = flat ? 0.1 : height;
        double d2 = axis.equals(Direction.Axis.Z) ? 0.1 : this.getSize();

        if (flat) {
            Direction.Axis axis2d = facing.getAxis();
            d0 = axis2d.equals(Direction.Axis.X) ? height : this.getSize();
            d2 = axis2d.equals(Direction.Axis.Z) ? height : this.getSize();
        }
        return AABB.ofSize(vec3, d0, d1, d2);
    }

    public static List<Entity> getEntitiesNearby(Entity entity, double range) {
        if (!entity.level().isClientSide()) {
            AABB boundingBox = entity.getBoundingBox().inflate(range);
            List<Entity> entities = entity.level().getEntitiesOfClass(Entity.class, boundingBox);
            entities.remove(entity);
            entities.removeIf(e -> {
                String entityAsString = PGHelper.getEntityAsString(e.getType());
                return PGConfigHelper.getDisabledEntities().contains(entityAsString);
            });
            entities.removeIf(e -> {
                if (e instanceof ServerPlayer player) {
                    return player.isOnPortalCooldown() || player.isChangingDimension();
                }
                return false;
            });
            return entities;
        }
        return null;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_COLOR_ID, Color.GREEN.getRGB());
        this.entityData.define(DATA_DIR, Direction.SOUTH);
        this.entityData.define(DATA_FACING, Direction.SOUTH);
        this.entityData.define(DATA_SIZE, 1.0f);
        this.entityData.define(LIFETIME, PGHelper.seconds(10));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            if (!exists) {
                LevelHelper.playSound(this.level(), this.blockPosition(), PGSounds.PORTAL_SHOOT, SoundSource.PLAYERS);
                this.exists = true;
            }
            if (getLifetime() > 0) {
                int l = getLifetime();
                setLifetime(l - 1);
            }
            if (delay > 0) delay--;
            if (!firstTick && getLifetime() == 0) {
                level().getChunkSource().updateChunkForced(new ChunkPos(this.blockPosition()), false);
                this.kill();
                return;
            }
            if (destinationLevel == null) {
                if (!isBootleg() && !LevelHelper.isBlenderDestination(getHopDim())) {
                    ResourceKey<Level> key = LevelHelper.getWorldKey(new ResourceLocation(getHopDim()));
                    destinationLevel = LevelHelper.getServerWorld(this.level(), key);
                } else {
                    destinationLevel = (ServerLevel) this.level();

                }
                if (targetPos == null) {
                    targetPos = getHopLoc();
                }
            }
            if (targetVec == null) {
                targetVec = Vec3.directionFromRotation(new Vec2(45.0F, this.getYRot() + 180.0F));
            }

            boolean shouldHurt = isBootleg() || LevelHelper.isBlenderDestination(getHopDim());

            if (shouldHurt || (destinationLevel != null && targetPos != null)) {
                List<Entity> entityList = getEntitiesNearby(this, 0.3D);
                if (entityList != null && !entityList.isEmpty()) {
                    for (Entity nearby : entityList) {
                        if (colliding(this, nearby) && !nearby.isOnPortalCooldown() && !nearby.isPassenger() && delay == 0) {
                            if (!shouldHurt) {
                                Set<RelativeMovement> relativeSet = new HashSet<>();
                                relativeSet.add(RelativeMovement.Y_ROT);
                                nearby.teleportTo(destinationLevel, targetPos.getX() + targetVec.x * 2, targetPos.getY(), targetPos.getZ() + targetVec.z * 2, relativeSet, nearby.getYRot(), nearby.getXRot());

                                nearby.resetFallDistance();
                                nearby.setPortalCooldown();
                            } else {
                                if (nearby instanceof ServerPlayer player) player.hurt(LevelHelper.isBlenderDestination(getHopDim()) ? PGDamageTypes.blender() : PGDamageTypes.bootleg(), Float.MAX_VALUE);
                                else nearby.kill();
                            }
                        }
                    }
                }
            }
        }
    }
}