package com.jdolphin.ricksportalgun.common.item;

import com.jdolphin.ricksportalgun.common.init.PGDamageTypes;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class PortalFluidItem extends Item {

    public PortalFluidItem(Properties pProperties) {
        super(pProperties);
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof ServerPlayer player) {
            if (!player.isCreative()) {
                ItemUtils.startUsingInstantly(level, player, player.getUsedItemHand());
                Optional<Item> remainder = Optional.ofNullable(stack.getItem().getCraftingRemainingItem());
                remainder.ifPresent(item -> player.addItem(item.getDefaultInstance()));
                player.awardStat(Stats.ITEM_USED.get(this));
            }
            player.addEffect(new MobEffectInstance(MobEffects.POISON));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20 * 2, 2));
            player.hurt(PGDamageTypes.teleport(), 3);
            LevelHelper.randomTP(player, 100, 1000, true);
        }
        return stack;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack oppositeStack = player.getItemInHand(PGHelper.getOppositeHand(hand));

        if (oppositeStack.is(PGTags.Items.PORTAL_GUNS)) {
            //Setup for fluid changes
        }
        return super.use(level, player, hand);
    }

    public int getUseDuration(ItemStack pStack) {
        return 40;
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }
}