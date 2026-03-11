package com.jdolphin.ricksportalgun.common.item;

import com.jdolphin.ricksportalgun.common.init.PGDamageTypes;
import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.awt.*;

public class PortalFluidBottleItem extends Item implements IPortalFluidItem {

    public PortalFluidBottleItem(Properties pProperties) {
        super(pProperties);
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof ServerPlayer player) {
            if (!player.isCreative()) {
                InteractionHand hand = player.getUsedItemHand();
                ItemUtils.startUsingInstantly(level, player, hand);
                int amount = this.getFluid(stack);

                if (amount == 1) {
                    this.empty(stack, player, hand);
                } else this.lowerFuel(stack, 1);

                player.awardStat(Stats.ITEM_USED.get(this));
            }
            player.addEffect(new MobEffectInstance(MobEffects.POISON));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20 * 2, 2));
            player.hurt(PGDamageTypes.teleport(), 3);
            LevelHelper.randomTP(player, 500, true);
        }
        return stack;
    }

    @Override
    public boolean isBootleg() {
        return this.equals(PGItems.BOOTLEG_PORTAL_FLUID);
    }

    public int getUseDuration(ItemStack pStack) {
        return 40;
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Color.GREEN.getRGB();
    }

    public boolean isBarVisible(ItemStack stack) {
        return this.getFluid(stack) < this.getMaxFluid(stack);
    }

    public int getBarWidth(ItemStack stack) {
        return Math.round(getFluid(stack) * 13.0F / getMaxFluid(stack));
    }

    @Override
    public int getMaxFluid(ItemStack stack) {
        return 16;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level pLevel, java.util.List<net.minecraft.network.chat.Component> tooltips, TooltipFlag isAdvanced) {
        tooltips.add(net.minecraft.network.chat.Component.translatable("tooltip.ricksportalgun.fluid", getFluid(stack), getMaxFluid(stack)).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public @Nullable ItemStack getRemainingStack() {
        return Items.GLASS_BOTTLE.getDefaultInstance();
    }

    @Override
    public boolean canRefuelPortalGun() {
        return !PGItems.QUANTUM_LEAP_ELIXIR.equals(this);
    }
}