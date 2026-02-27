package com.jdolphin.ricksportalgun.common.item;

import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public interface IPortalFluidItem {

    int getMaxFluid(ItemStack stack);

    @Nullable
    ItemStack getRemainingStack();

    default int getFluid(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains(PGNbtKeys.TAG_FUEL) ? tag.getInt(PGNbtKeys.TAG_FUEL) : getMaxFluid(stack);
    }

    default void empty(ItemStack stack, Player player, InteractionHand hand) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(PGNbtKeys.TAG_FUEL, 0);

        ItemStack stack1 = player.getItemInHand(hand);
        stack1.shrink(1);
        if (getRemainingStack() != null && !getRemainingStack().equals(ItemStack.EMPTY)) {
            player.addItem(getRemainingStack());
        }
    }

    default void lowerFuel(ItemStack stack, int amount) {
        int i = getFluid(stack);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(PGNbtKeys.TAG_FUEL, Math.max(0, i - amount));
    }

    default void setAmount(ItemStack stack, int amount) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(PGNbtKeys.TAG_FUEL, amount);
    }

    default boolean isBootleg() {
        return false;
    }

    default boolean isEmpty(ItemStack stack) {
        return getFluid(stack) > 0;
    }
}
