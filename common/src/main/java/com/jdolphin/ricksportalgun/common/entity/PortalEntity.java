package com.jdolphin.ricksportalgun.common.entity;

import com.jdolphin.ricksportalgun.common.init.PGDamageTypes;
import com.jdolphin.ricksportalgun.common.init.PGEntities;
import com.jdolphin.ricksportalgun.common.init.PGSounds;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;


public class PortalEntity extends Entity {
    private static final EntityDataAccessor<Integer> DATA_COLOR_ID = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.INT);
    public static final String TAG_DIMENSION = "PortalDimension";
    public static final String TAG_BPOS = "PortalPos";
    public static final String TAG_OPEN = "Open";
    public static final String TAG_NEW = "isSpawned";
    public static final String TAG_COOLDOWN = "Cooldown";

    private Optional<BlockPos> targetPos;
    private boolean bootleg;
    private int maxLifeTime;
    private boolean exists;

    private Vec3 pos;
    private String targetDim;
    private int delay = 0;
    public int lifetime = 20 * 10;
    private boolean flat;

    private Direction direction;

    public boolean exists() {
        return exists;
    }

    public PortalEntity(EntityType<PortalEntity> type, Level level) {
        super(type, level);
    }

    public PortalEntity(Level pLevel, Vec3 pos, Direction direction) {
        super(PGEntities.PORTAL, pLevel);
        this.direction = direction;
        this.setPos(pos);
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

    public boolean isBootleg() {
        return bootleg;
    }

    public void setPos(BlockPos pos) {
        this.setPos(pos.getX(), pos.getY(), pos.getZ());
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

    public void setHopLocation(ResourceLocation dimension, BlockPos pos) {
        this.targetDim = dimension.toString();
        this.targetPos = Optional.of(pos);
    }

    public BlockPos getHopLoc() {
        return this.targetPos.orElse(BlockPos.ZERO);
    }

    public String getHopDim() {
        return this.targetDim == null ? "minecraft:overworld" : this.targetDim;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.bootleg = tag.getBoolean(PortalGunItem.TAG_ACIDIC);
        this.targetDim = tag.getString(TAG_DIMENSION);
        this.targetPos = NbtUtils.readBlockPos(tag, TAG_BPOS);
        this.setColor(tag.getInt(PortalGunItem.TAG_COLOR));
        this.lifetime = tag.getInt(TAG_OPEN);
        this.delay = tag.getInt(TAG_COOLDOWN);
        this.exists = tag.getBoolean(TAG_NEW);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putBoolean(PortalGunItem.TAG_ACIDIC, this.bootleg);
        tag.putBoolean(TAG_NEW, this.exists);
        tag.putString(TAG_DIMENSION, getHopDim());
        tag.put(TAG_BPOS, NbtUtils.writeBlockPos(getHopLoc()));
        tag.putInt(PortalGunItem.TAG_COLOR, this.getColor());
        tag.putInt(TAG_OPEN, this.lifetime);
        tag.putInt(TAG_COOLDOWN, this.delay);
    }

    public boolean isFlat() {
        return this.flat;
    }

    public void setFlat(boolean flat) {
        this.flat = flat;
        this.recalculateBoundingBox();
    }

    protected final void recalculateBoundingBox() {
            AABB aabb = this.calculateBoundingBox(this.pos, this.direction);
            Vec3 vec3 = aabb.getCenter();
            this.setPosRaw(vec3.x, vec3.y, vec3.z);
            this.setBoundingBox(aabb);
    }

    protected AABB calculateBoundingBox(Vec3 vec3, Direction dir) {
        Direction.Axis axis = dir.getAxis();
        boolean flat = dir.equals(Direction.UP) || dir.equals(Direction.DOWN);

        double d0 = axis == Direction.Axis.X ? 0.1F : 1;
        double d1 = this.flat ? 0.0625F : 2;
        double d2 = axis == Direction.Axis.Z ? 0.1F : 1;
        System.out.println(vec3);
        return AABB.ofSize(vec3, d0, d1, d2);
    }

    @Override
    public void setPos(double x, double y, double z) {
        this.pos = new Vec3(x, y, z);
        this.setPosRaw(x, y, z);
        if (direction != null) this.recalculateBoundingBox();
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
        level().addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, this.getX(), this.getY() + 1, this.getZ(), 1, 1, 1); //Temp
        if (!this.level().isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            if (!exists) LevelHelper.playSound(this.level(), this.blockPosition(), PGSounds.PORTAL_SHOOT, SoundSource.PLAYERS);
            this.exists = true;
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