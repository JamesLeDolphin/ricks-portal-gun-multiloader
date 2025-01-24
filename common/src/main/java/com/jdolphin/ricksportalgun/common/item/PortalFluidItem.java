package com.jdolphin.ricksportalgun.common.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class PortalFluidItem extends Item {
    public PortalFluidItem(Properties pProperties) {
        super(pProperties);
    }

    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        super.finishUsingItem(pStack, pLevel, pEntityLiving);
        if (pEntityLiving instanceof ServerPlayer serverplayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, pStack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!pLevel.isClientSide) {
            pEntityLiving.removeEffect(MobEffects.POISON);
        }

        if (pStack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        } else {
            if (pEntityLiving instanceof Player && !((Player)pEntityLiving).getAbilities().instabuild) {
                ItemStack itemstack = new ItemStack(Items.GLASS_BOTTLE);
                Player player = (Player)pEntityLiving;
                if (!player.getInventory().add(itemstack)) {
                    player.drop(itemstack, false);
                }
            }

            return pStack;
        }
    }


    public int getUseDuration(ItemStack pStack, LivingEntity entity) {
        return 40;
    }


    public ItemUseAnimation getUseAnimation(ItemStack pStack) {
        return ItemUseAnimation.DRINK;
    }

    public Holder.Reference<SoundEvent> getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    public Holder.Reference<SoundEvent> getEatingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    public InteractionResult use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pHand);
    }
}