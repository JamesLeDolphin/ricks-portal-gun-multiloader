package com.jdolphin.ricksportalgun.common.entity;

import com.jdolphin.ricksportalgun.common.init.PGDamageTypes;
import com.jdolphin.ricksportalgun.common.init.PGEntities;
import com.jdolphin.ricksportalgun.common.init.PGSounds;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
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
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public class PortalEntity extends Entity {
    private static final EntityDataAccessor<Integer> DATA_COLOR_ID = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Direction> DATA_DIR = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.DIRECTION);
    private static final EntityDataAccessor<Direction> DATA_FACING = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.DIRECTION);
    private static final EntityDataAccessor<Float> DATA_SIZE = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.FLOAT);
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
    private int maxLifeTime;
    private boolean exists;

    private Vec3 pos;
    private String targetDim;
    private int delay = 0;
    public int lifetime = 20 * 10;

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

    public void setMaxLifeTime(int lifetime) {
        this.maxLifeTime = lifetime;
    }

    public int getMaxLifeTime() {
        return this.maxLifeTime;
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
    public void kill(ServerLevel level) {
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
    public boolean hurtServer(@NotNull ServerLevel serverLevel, @NotNull DamageSource damageSource, float v) {
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
        this.targetPos = NbtUtils.readBlockPos(tag, TAG_BPOS).orElse(BlockPos.ZERO);
        this.setColor(tag.getInt(TAG_COLOR));
        this.lifetime = tag.getInt(TAG_OPEN);
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
        tag.putInt(TAG_OPEN, this.lifetime);
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
            entities.removeIf(e -> e instanceof PortalEntity);
            entities.removeIf(e -> e instanceof EnderDragon);
            entities.removeIf(e -> e instanceof WitherBoss);
            entities.removeIf(e -> e instanceof Warden);
//            entities.removeIf(e -> BuiltInRegistries.ENTITY_TYPE.getKey(e.getType()));
            entities.removeIf(e -> {
                if (e instanceof ServerPlayer player) {
                    return player.isOnPortalCooldown() || player.isChangingDimension() || !player.canUsePortal(false);
                }  return false;
            });
            return entities;
        }
        return null;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_COLOR_ID, Color.GREEN.getRGB());
        builder.define(DATA_DIR, Direction.SOUTH);
        builder.define(DATA_FACING, Direction.SOUTH);
        builder.define(DATA_SIZE, 1.0f);
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
            if (lifetime > 0) lifetime--;
            if (delay > 0) delay--;
            if (!firstTick && lifetime == 0) {
                this.kill(serverLevel);
                return;
            }
            List<Entity> entityList = getEntitiesNearby(this, 0.3D);
            if (entityList != null) {
                for (Entity nearby : entityList) {
                    ServerLevel destinationDim;
                    BlockPos destinationPos;
                    if (!this.bootleg) {
                        ResourceKey<Level> key = LevelHelper.getWorldKey(ResourceLocation.parse(getHopDim()));
                        destinationDim = LevelHelper.getServerWorld(this.level(), key);
                        destinationPos = getHopLoc();
                    } else {
                        destinationDim = LevelHelper.getRandomServerLevel(serverLevel.getServer());
                        destinationPos = LevelHelper.getSafePos(LevelHelper.getRandomCoord(serverLevel, 5000), serverLevel);
                    }
                    if (colliding(this, nearby) && !nearby.is(this) && !nearby.isOnPortalCooldown() && !nearby.isPassenger()) {
                        if (this.bootleg) {
                            nearby.hurtServer(serverLevel, PGDamageTypes.of(serverLevel, PGDamageTypes.BOOTLEG), this.random.nextInt(10) == 1 ? Integer.MAX_VALUE : 5);
                        }
                        if (destinationDim != null && !destinationDim.isClientSide()) {
                            if (nearby.canUsePortal(false) && delay == 0) {
                                Vec3 look = Vec3.directionFromRotation(new Vec2(45.0F, this.getYRot() + 180.0F));
                                double dx = (double) destinationPos.getX() + look.x * 2d;
                                double dz = (double) destinationPos.getZ() + look.z * 2d;
                                Set<Relative> relativeSet = new HashSet<>();
                                relativeSet.add(Relative.Y_ROT);
                                nearby.teleportTo(destinationDim, dx, destinationPos.getY(), dz, relativeSet, nearby.getYRot(), nearby.getXRot(), false);

                                nearby.setPortalCooldown();
                            }
                        }
                    }
                }
            }
        }
    }
}