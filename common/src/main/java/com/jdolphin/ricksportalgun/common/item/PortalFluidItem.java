package com.jdolphin.ricksportalgun.common.item;

import com.jdolphin.ricksportalgun.common.init.PGDamageTypes;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class PortalFluidItem extends Item {

    public PortalFluidItem(Properties pProperties) {
        super(pProperties);
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof ServerPlayer player) {
            if (!player.isCreative()) {
                ItemUtils.startUsingInstantly(level, player, player.getUsedItemHand());
                player.addItem(stack.getItem().getCraftingRemainingItem().getDefaultInstance());
                player.awardStat(Stats.ITEM_USED.get(this));
            }
            player.addEffect(new MobEffectInstance(MobEffects.POISON));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20 * 2, 2));
            ServerLevel serverLevel = (ServerLevel) level;
            player.hurt(PGDamageTypes.of(serverLevel, PGDamageTypes.TELEPORT), 3);
            LevelHelper.randomTP(player, 500, true);
        }
        return stack;
    }

    public int getUseDuration(ItemStack pStack) {
        return 40;
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }
}