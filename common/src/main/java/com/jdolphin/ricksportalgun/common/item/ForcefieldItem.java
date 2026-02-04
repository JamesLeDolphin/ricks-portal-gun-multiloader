package com.jdolphin.ricksportalgun.common.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ForcefieldItem extends Item {

    public ForcefieldItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide) {
            ItemStack stack = player.getItemInHand(usedHand);
            CompoundTag tag = stack.getOrCreateTag();
            boolean enabled = tag.contains("Active") && tag.getBoolean("Active");
            MutableComponent on = Component.translatable("ricksportalgun.enabled");
            MutableComponent off = Component.translatable("ricksportalgun.disabled");
            player.displayClientMessage(Component.translatable("item.ricksportalgun.pocket_forcefield").append(": ").append(!enabled ? on : off), true);
            tag.putBoolean("Active", !enabled);
        }
        return super.use(level, player, usedHand);
    }

    public static boolean isEnabled(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains("Active") && tag.getBoolean("Active");
    }
}
