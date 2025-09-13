package com.jdolphin.ricksportalgun.common.entity;

import com.jdolphin.ricksportalgun.common.init.PGDamageTypes;
import com.jdolphin.ricksportalgun.common.init.PGEntities;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public class ExplosiveItemEntity extends ItemEntity {
    private static final EntityDataAccessor<Integer> FUSE = SynchedEntityData.defineId(ExplosiveItemEntity.class, EntityDataSerializers.INT);

    public ExplosiveItemEntity(EntityType<? extends ItemEntity> type, Level level) {
        super(type, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FUSE, PGHelper.seconds(10));
    }

    public ExplosiveItemEntity(Level level, BlockPos pos, ItemStack stack) {
        this(PGEntities.EXPLOSIVE_ITEM, level);
        setPos(pos.getX(), pos.getY(), pos.getZ());
        setItem(stack);
    }

    public int getFuse() {
        return this.entityData.get(FUSE);
    }

    public void setFuse(int i) {
        this.entityData.set(FUSE, i);
    }

    @Override
    public void tick() {
        super.tick();
        if (level() instanceof ServerLevel level) {
            if (getFuse() > 0) {
                int i = getFuse();
                i--;
                setFuse(i);
            } else {
                BlockPos pos = this.blockPosition();
                boolean kaboom = level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
                Level.ExplosionInteraction interaction = kaboom ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE;
                ExplosionDamageCalculator calc = new EntityBasedExplosionDamageCalculator(this); //Short for calculator
                level.explode(this, PGDamageTypes.of(level, PGDamageTypes.SELF_DESTRUCT), calc, pos.getX(), pos.getY(), pos.getZ(), 5, true, interaction);
                this.kill();
            }
        }
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("Fuse", getFuse());
        super.addAdditionalSaveData(compound);

    }

    public void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("Fuse")) setFuse(compound.getInt("Fuse"));
        super.readAdditionalSaveData(compound);
    }
}