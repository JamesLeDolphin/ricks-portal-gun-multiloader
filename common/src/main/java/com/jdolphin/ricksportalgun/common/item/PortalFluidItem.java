package com.jdolphin.ricksportalgun.common.item;

import com.jdolphin.ricksportalgun.common.init.PGDamageTypes;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;

public class PortalFluidItem extends Item {
    public PortalFluidItem(Properties pProperties) {
        super(pProperties);
    }

    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Consumable consumable = itemstack.get(DataComponents.CONSUMABLE);
        if (consumable != null) {
            return consumable.startConsuming(player, itemstack, hand);
        }
        return InteractionResult.FAIL;
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof ServerPlayer player) {
            if (!player.isCreative()) {
                stack.consume(1, entity);
                player.addItem(stack.getItem().getCraftingRemainder());
            }
            player.addEffect(new MobEffectInstance(MobEffects.POISON));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20 * 2, 2));
            ServerLevel serverLevel = (ServerLevel) level;
            player.hurtServer(serverLevel, PGDamageTypes.of(serverLevel, PGDamageTypes.TELEPORT), 3);
            LevelHelper.randomTP(player, 500);
        }
        return stack;
    }

    public int getUseDuration(ItemStack pStack, LivingEntity entity) {
        return 40;
    }

    public ItemUseAnimation getUseAnimation(ItemStack pStack) {
        return ItemUseAnimation.DRINK;
    }
}