package com.jdolphin.ricksportalgun.common.entity;
import com.jdolphin.ricksportalgun.common.init.PGSounds;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helpers.LevelHelper;
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
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public class PortalEntity extends Entity {
    private static final EntityDataAccessor<Integer> DATA_COLOR_ID = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.INT);
    public static final String TAG_DIMENSION = "PortalDimension";
    public static final String TAG_BPOS = "PortalPos";
    public static final String TAG_OPEN = "Open";
    public static final String TAG_NEW = "isSpawned";
    public static final String TAG_COOLDOWN = "Cooldown";

    private Optional<BlockPos> bPos;
    private boolean acid;
    private int maxLifeTime;
    private boolean exists;

    private String dim;
    private int delay = 0;
    public int lifetime = 20 * 10;

    public boolean exists() {
        return exists;
    }

    public PortalEntity(EntityType<? extends PortalEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
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

    public int getColor() {
        return this.entityData.get(DATA_COLOR_ID);
    }

    public boolean isAcid() {
        return acid;
    }

    public void setPos(BlockPos pos) {
        this.setPos(pos.getX(), pos.getY(), pos.getZ());
    }

    public void setAcid(boolean acid) {
        this.acid = acid;
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
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
        return false;
    }

    public void setHopLocation(ResourceLocation dimension, BlockPos pos) {
        this.dim = dimension.toString();
        this.bPos = Optional.of(pos);
    }

    public BlockPos getHopLoc() {
        return this.bPos.orElse(BlockPos.ZERO);
    }

    public String getHopDim() {
        return this.dim == null ? "minecraft:overworld" : this.dim;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.acid = tag.getBoolean(PortalGunItem.TAG_ACIDIC);
        this.dim = tag.getString(TAG_DIMENSION);
        this.bPos = NbtUtils.readBlockPos(tag, TAG_BPOS);
        this.setColor(tag.getInt(PortalGunItem.TAG_COLOR));
        this.lifetime = tag.getInt(TAG_OPEN);
        this.delay = tag.getInt(TAG_COOLDOWN);
        this.exists = tag.getBoolean(TAG_NEW);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putBoolean(PortalGunItem.TAG_ACIDIC, this.acid);
        tag.putBoolean(TAG_NEW, this.exists);
        tag.putString(TAG_DIMENSION, getHopDim());
        tag.put(TAG_BPOS, NbtUtils.writeBlockPos(getHopLoc()));
        tag.putInt(PortalGunItem.TAG_COLOR, this.getColor());
        tag.putInt(TAG_OPEN, this.lifetime);
        tag.putInt(TAG_COOLDOWN, this.delay);
    }

    protected final void recalculateBoundingBox() {
        this.getDirection();
        AABB aabb = this.calculateBoundingBox(this.blockPosition(), this.getDirection());
        Vec3 vec3 = aabb.getCenter();
        this.setPosRaw(vec3.x, vec3.y, vec3.z);
        this.setBoundingBox(aabb);
    }

    protected AABB calculateBoundingBox(BlockPos pos, Direction dir) {
        Vec3 vec3 = Vec3.atBottomCenterOf(pos).relative(Direction.UP, 0.99);
        Direction.Axis direction$axis = dir.getAxis();
        double d2 = direction$axis == Direction.Axis.X ? (double)0.0625F : 1;
        double d3 = 2;
        double d4 = direction$axis == Direction.Axis.Z ? (double)0.0625F : 1;

        return AABB.ofSize(vec3, d2, d3, d4);
    }

    @Override
    public void setPos(double x, double y, double z) {
        this.setPosRaw(x, y, z);
        this.recalculateBoundingBox();
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
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (!exists) LevelHelper.playSound(this.level(), this.blockPosition(), PGSounds.PORTAL_SHOOT, SoundSource.PLAYERS);
            this.exists = true;
            if (lifetime > 0) lifetime--;
            if (delay > 0) delay--;
            if (!firstTick && lifetime == 0) {
                this.kill((ServerLevel) level());
                return;
            }
            List<Entity> entityList = getEntitiesNearby(this, 0.3D);
            if (entityList != null) {
                for (Entity nearby : entityList) {
                    if (!this.acid) {
                        ResourceKey<Level> key = LevelHelper.getWorldKey(ResourceLocation.parse(getHopDim()));
                        ServerLevel serverlevel = LevelHelper.getServerWorld(this.level(), key);
                        BlockPos pos = getHopLoc();
                        if (colliding(this, nearby) && !nearby.is(this) && !nearby.isOnPortalCooldown() && !nearby.isPassenger()) {
                            if (serverlevel != null && !serverlevel.isClientSide()) {
                                if (nearby.canUsePortal(false) && delay == 0) {
                                    Vec3 look = Vec3.directionFromRotation(new Vec2(45.0F, this.getYRot() + 180.0F));
                                    double dx = (double) pos.getX() + look.x * 2d;
                                    double dz = (double) pos.getZ() + look.z * 2d;
                                    Set<Relative> relativeSet = new HashSet<>();
                                    relativeSet.add(Relative.Y_ROT);
                                    nearby.teleportTo(serverlevel, dx, pos.getY(), dz, relativeSet, nearby.getYRot(), nearby.getXRot(), false);

                                    nearby.setPortalCooldown();
                                } else return;
                            }
                        }
                    } else {
                        nearby.kill((ServerLevel) level());
                    }
                }
            }
        }
    }
}